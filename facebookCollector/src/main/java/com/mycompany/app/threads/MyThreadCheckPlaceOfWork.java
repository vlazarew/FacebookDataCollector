package com.mycompany.app.threads;

import com.mycompany.app.pages.UserProfilePage;
import lombok.Getter;
import org.openqa.selenium.opera.OperaDriver;

@Getter
public class MyThreadCheckPlaceOfWork implements Runnable {
    private UserProfilePage userProfilePage;
    private volatile String result;
    private OperaDriver browser;

    public MyThreadCheckPlaceOfWork(UserProfilePage userProfilePage, OperaDriver browser) {
        this.userProfilePage = userProfilePage;
        this.browser = browser;
    }

    @Override
    public void run() {
        result = "";

        if (!browser.findElementsByXPath(userProfilePage.getWorkInfoXPath()).isEmpty()) {
            result = userProfilePage.getWorkInfo().getText();
        }
    }
}
