package com.mycompany.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.app.data.FacebookUser;
import com.mycompany.app.data.InputFilters;
import com.mycompany.app.pages.LoginPage;
import com.mycompany.app.pages.ProfilePage;
import com.mycompany.app.pages.TopSearchPage;
import com.mycompany.app.pages.UserProfilePage;
import com.mycompany.app.threads.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Getter
public class Crawler {

    private String operaDriverPath;
    private String loginPageURL;
    private String email;
    private String password;
    private String pathToQueryFiles;
    private String pathToHandledFiles;
    private String pathToLoadingFiles;
    private OperaDriver browser;

    public Crawler() {
        String workingDir = System.getProperty("user.dir");
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(workingDir + "/facebookCollector/src/main/resources/init.properties");
            property.load(fis);

            this.operaDriverPath = property.getProperty("init.operaDriverPath");
            this.loginPageURL = property.getProperty("init.loginPageURL");
            this.email = property.getProperty("init.email");
            this.password = property.getProperty("init.password");
            this.pathToQueryFiles = property.getProperty("init.pathToQueryFiles");
            this.pathToHandledFiles = property.getProperty("init.pathToHandledFiles");
            this.pathToLoadingFiles = property.getProperty("init.pathToLoadingFiles");

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    public void initializeOperaDriver(String websiteURL) {
        System.setProperty("webdriver.opera.driver", this.operaDriverPath);
        OperaOptions options = new OperaOptions();
        options.addArguments("--headless");
        OperaDriver browser = new OperaDriver(options);
        browser.get(websiteURL);
        browser.manage().window().maximize();

        // На прогрузку страницы
        browser.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        this.browser = browser;
    }

    public void loginIntoFacebook() {
        LoginPage loginPage = new LoginPage(browser, email, password);
        loginPage.FillFields();
    }

    @SneakyThrows
    public void fillNameLastName(String name, String lastname) {
        ProfilePage profilePage = new ProfilePage(browser);
        profilePage.goToFindFriends();
        profilePage.fillFindFriendsInput(name, lastname);
    }

    @SneakyThrows
    private void addCity(TopSearchPage topSearchPage, String city) {
        topSearchPage.clickSearchByPeopleLink();
        topSearchPage.fillAndClickCityFilter(city, browser);
        TimeUnit.SECONDS.sleep(1);
    }

    @SneakyThrows
    private void addPlaceOfWork(TopSearchPage topSearchPage, String placeOfWork) {
        topSearchPage.clickSearchByPeopleLink();
        topSearchPage.fillAndClickPlaceOfWorkFilter(placeOfWork, browser);
        TimeUnit.SECONDS.sleep(1);
    }

    @SneakyThrows
    private void addPlaceOfStudy(TopSearchPage topSearchPage, String placeOfStudy) {
        topSearchPage.clickSearchByPeopleLink();
        topSearchPage.fillAndClickPlaceOfStudyFilter(placeOfStudy, browser);
        TimeUnit.SECONDS.sleep(1);
    }

    @SneakyThrows
    void visitFoundUser(WebElement foundUser, String findPeopleURL,
                        ArrayList<FacebookUser> foundFacebookUsers) {
        foundUser.click();
        TimeUnit.SECONDS.sleep(1);

        try {
            foundFacebookUsers.add(getInfoAboutCurrentUser());
        } catch (Exception ignored) {
        }

        browser.get(findPeopleURL);
    }

    @SneakyThrows
    private FacebookUser getInfoAboutCurrentUser() {
        UserProfilePage userProfilePage = new UserProfilePage(browser);

        String profileURL = browser.getCurrentUrl();
        String photoURL = userProfilePage.getUserPhoto().getAttribute("xlink:href");

        String userInfoURL = userProfilePage.getInformationButton().getAttribute("href");
        browser.get(userInfoURL);

        MyThreadCheckPlaceOfWork runnablePlaceOfWork = new MyThreadCheckPlaceOfWork(userProfilePage, browser);
        Thread threadPlaceOfWork = new Thread(runnablePlaceOfWork);
        threadPlaceOfWork.start();

        MyThreadCheckPlaceOfStudy runnablePlaceOfStudy = new MyThreadCheckPlaceOfStudy(userProfilePage, browser);
        Thread threadPlaceOfStudy = new Thread(runnablePlaceOfStudy);
        threadPlaceOfStudy.start();

        MyThreadCheckPlaceOfHome runnablePlaceOfHome = new MyThreadCheckPlaceOfHome(userProfilePage, browser);
        Thread threadPlaceOfHome = new Thread(runnablePlaceOfHome);
        threadPlaceOfHome.start();

        threadPlaceOfWork.join();
        threadPlaceOfStudy.join();
        threadPlaceOfHome.join();

        String placeOfWork = runnablePlaceOfWork.getResult();
        String study = runnablePlaceOfStudy.getResult();
        String homePlace = runnablePlaceOfHome.getResult();

        String userContactURL = userProfilePage.getContactInfoButton().getAttribute("href");
        browser.get(userContactURL);

        MyThreadCheckEmail runnableEmail = new MyThreadCheckEmail(userProfilePage, browser);
        Thread threadEmail = new Thread(runnableEmail);
        threadEmail.start();

        MyThreadCheckPhoneNumber runnablePhoneNumber = new MyThreadCheckPhoneNumber(userProfilePage, browser);
        Thread threadPhoneNumber = new Thread(runnablePhoneNumber);
        threadPhoneNumber.start();

        MyThreadCheckWebsite runnableWebsite = new MyThreadCheckWebsite(userProfilePage, browser);
        Thread threadWebsite = new Thread(runnableWebsite);
        threadWebsite.start();

        threadEmail.join();
        threadPhoneNumber.join();
        threadWebsite.join();

        String email = runnableEmail.getResult();
        String phoneNumber = runnablePhoneNumber.getResult();
        String website = runnableWebsite.getResult();

        String[] names = userProfilePage.getNamesInfo().getText().split("\n");
        String[] goodNames = names[0].split(" ");
        return new FacebookUser(goodNames[0], goodNames[1], photoURL, placeOfWork, study, email, phoneNumber,
                profileURL, homePlace, website);
    }

    @SneakyThrows
    public void search(InputFilters inputFilter) {
        ArrayList<FacebookUser> foundFacebookUsers = new ArrayList<>();

        TopSearchPage topSearchPage = new TopSearchPage(browser);
        String city = inputFilter.getCity();
        if (!city.equals("")) {
            addCity(topSearchPage, city);
        }

        String study = inputFilter.getStudy();
        if (!study.equals("")) {
            addPlaceOfStudy(topSearchPage, study);
        }

        String work = inputFilter.getWork();
        if (!work.equals("")) {
            addPlaceOfWork(topSearchPage, work);
        }

        String findPeopleURL = browser.getCurrentUrl();
        List<WebElement> foundUsers = topSearchPage.getPeopleNameLinks();
        int countOfUsers = Math.min(inputFilter.getCountOfUsers(), foundUsers.size());

        for (int i = 0; i < countOfUsers; i++) {
            visitFoundUser(foundUsers.get(i), findPeopleURL, foundFacebookUsers);
        }
        browser.quit();

        makeOutputJSONFiles(foundFacebookUsers, inputFilter);
        moveInputJSONToHandledFolder(inputFilter);
    }

    private void moveInputJSONToHandledFolder(InputFilters inputFilter) {
        String loadingDir = inputFilter.getFolderWithLoadingFiles();
        String fileName = inputFilter.getFileName();

        File foundFile = FileUtils.listFiles(new File(loadingDir), null, true).stream().filter(f -> f.getName()
                .equals(fileName)).findFirst().orElse(null);

        if (foundFile != null) {
            foundFile.renameTo(new File(inputFilter.getFolderWithHandledFiles(), foundFile.getName()));
        }
    }

    @SneakyThrows
    private void makeOutputJSONFiles(ArrayList<FacebookUser> foundFacebookUsers, InputFilters inputFilters) {
        ObjectMapper mapper = new ObjectMapper();
        String outputDir = inputFilters.getOutputPath();

        File outputCatalog = new File(outputDir);
        if (!outputCatalog.exists()) {
            outputCatalog.mkdir();
        }

        for (FacebookUser facebookUser : foundFacebookUsers) {
            writeToJson(mapper, outputDir, facebookUser);
        }

    }

    @SneakyThrows
    void writeToJson(ObjectMapper mapper, String outputDir, FacebookUser facebookUser) {
        String uid = facebookUser.getUID();
        String outputJSON = outputDir + "/" + uid + ".json";

        mapper.writeValue(new File(outputJSON), facebookUser);
        System.out.println(outputJSON);
    }

}
