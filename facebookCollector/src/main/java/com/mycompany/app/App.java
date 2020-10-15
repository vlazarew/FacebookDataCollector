package com.mycompany.app;

import com.mycompany.app.facebookUser.FacebookUser;
import com.mycompany.app.pages.LoginPage;
import com.mycompany.app.pages.ProfilePage;
import com.mycompany.app.pages.TopSearchPage;
import com.mycompany.app.pages.UserProfilePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

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
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return browser;
    }

    private static void loginIntoFacebook(OperaDriver browser) {
        LoginPage loginPage = new LoginPage(browser, EMAIL, PASSWORD);
        loginPage.FillFields();

        // На прогрузку страницы
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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

        foundFacebookUsers.add(getInfoAboutCurrentUser(browser));

        TimeUnit.SECONDS.sleep(3);
        browser.get(findPeopleURL);
    }

    @SneakyThrows
    private static FacebookUser getInfoAboutCurrentUser(OperaDriver browser) {
        FacebookUser user = new FacebookUser();

        UserProfilePage userProfilePage = new UserProfilePage(browser);
        TimeUnit.SECONDS.sleep(3);
        String userInfoURL = userProfilePage.getInformationButton().getAttribute("href");
        browser.get(userInfoURL);

        return user;
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
    }
}
