package com.mycompany.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.app.facebookUser.FacebookUser;
import com.mycompany.app.pages.LoginPage;
import com.mycompany.app.pages.ProfilePage;
import com.mycompany.app.pages.TopSearchPage;
import com.mycompany.app.pages.UserProfilePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class App {
    static final String OPERADRIVERPATH = "C:/Program Files/Opera/operadriver_win64/operadriver.exe";
    static final String LOGINPAGEURL = "https://www.facebook.com";
    static final String EMAIL = "s02200122@gse.cs.msu.ru";
    static final String PASSWORD = "123QWEasd";
    static final String NAME = "Владимир";
    static final String LASTNAME = "Лазарев";
    static final String CITY = "Москва";
    static final int COUNT_OF_USERS = 5;

    public static void main(String[] args) {
        OperaDriver browser = initializeOperaDriver();
        loginIntoFacebook(browser);
        fillNameLastName(browser);

        Search(browser);
    }

    private static OperaDriver initializeOperaDriver() {
        System.setProperty("webdriver.opera.driver", OPERADRIVERPATH);
        OperaOptions options = new OperaOptions();
        options.addArguments("--headless");
        OperaDriver browser = new OperaDriver(options);
        browser.get(LOGINPAGEURL);
        browser.manage().window().maximize();

        // На прогрузку страницы
        browser.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return browser;
    }

    private static void loginIntoFacebook(OperaDriver browser) {
        LoginPage loginPage = new LoginPage(browser, EMAIL, PASSWORD);
        loginPage.FillFields();
    }

    private static void fillNameLastName(OperaDriver browser) {
        ProfilePage profilePage = new ProfilePage(browser);
        profilePage.goToFindFriends();
        profilePage.fillFindFriendsInput(NAME, LASTNAME);
        browser.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @SneakyThrows
    private static void addFilters(OperaDriver browser, TopSearchPage topSearchPage) {
        topSearchPage.clickSearchByPeopleLink();
        topSearchPage.clickAll(CITY, browser);
        TimeUnit.SECONDS.sleep(3);
    }

    @SneakyThrows
    private static void visitFoundUser(OperaDriver browser, WebElement foundUser, String findPeopleURL,
                                       ArrayList<FacebookUser> foundFacebookUsers) {
        foundUser.click();
        TimeUnit.SECONDS.sleep(1);

        foundFacebookUsers.add(getInfoAboutCurrentUser(browser));
        browser.get(findPeopleURL);
    }

    @SneakyThrows
    private static FacebookUser getInfoAboutCurrentUser(OperaDriver browser) {
        UserProfilePage userProfilePage = new UserProfilePage(browser);

        String profileURL = browser.getCurrentUrl();
        String photoURL = userProfilePage.getUserPhoto().getAttribute("xlink:href");

        String userInfoURL = userProfilePage.getInformationButton().getAttribute("href");
        browser.get(userInfoURL);

        TimeUnit.SECONDS.sleep(3);
        String placeOfWork = "";
        String study = "";
        String homePlace = "";
        String email = "";
        String phoneNumber = "";
        String website = "";

        try {
            placeOfWork = userProfilePage.getWorkInfo().getText();
        } catch (Exception ignored) {
        }

        try {
            study = userProfilePage.getProfessionEducationInfo().getText();
        } catch (Exception ignored) {
        }

        try {
            homePlace = userProfilePage.getHomePlaceInfo().getText();
        } catch (Exception ignored) {
        }

        String userContactURL = userProfilePage.getContactInfoButton().getAttribute("href");
        browser.get(userContactURL);
        TimeUnit.SECONDS.sleep(3);

        try {
            email = userProfilePage.getEmailInfo().get(0).getText();
        } catch (Exception ignored) {
        }

        try {
            phoneNumber = userProfilePage.getPhoneNumberInfo().get(0).getText();
        } catch (Exception ignored) {
        }

        try {
            website = userProfilePage.getWebsiteInfo().getText();
        } catch (Exception ignored) {
        }

        String[] names = userProfilePage.getNamesInfo().getText().split(" ");
        return new FacebookUser(names[0], names[1], photoURL, placeOfWork, study, email, phoneNumber,
                profileURL, homePlace, website);
    }

    @SneakyThrows
    private static void Search(OperaDriver browser) {
        ArrayList<FacebookUser> foundFacebookUsers = new ArrayList<>();

        TopSearchPage topSearchPage = new TopSearchPage(browser);
        addFilters(browser, topSearchPage);

        String findPeopleURL = browser.getCurrentUrl();
        List<WebElement> foundUsers = topSearchPage.getPeopleNameLinks();

        for (int i = 0; i < COUNT_OF_USERS; i++) {
            visitFoundUser(browser, foundUsers.get(i), findPeopleURL, foundFacebookUsers);
        }

        makeOutputJSONFiles(foundFacebookUsers);
        browser.close();
    }

    @SneakyThrows
    private static void makeOutputJSONFiles(ArrayList<FacebookUser> foundFacebookUsers) {
        ObjectMapper mapper = new ObjectMapper();
        String workingDir = System.getProperty("user.dir");
        String outputDir = workingDir + "/outputJSON";

        File outputCalalog = new File(outputDir);
        if (!outputCalalog.exists()) {
            outputCalalog.mkdir();
        }

        for (FacebookUser facebookUser : foundFacebookUsers) {
            String uid = facebookUser.getUID();
            String outputJSON = outputDir + "/" + uid + ".json";

            mapper.writeValue(new File(outputJSON), facebookUser);
            System.out.println(outputJSON);
        }

    }
}
