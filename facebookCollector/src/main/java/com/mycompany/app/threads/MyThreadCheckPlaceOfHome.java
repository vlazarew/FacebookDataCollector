package com.mycompany.app.threads;

import com.mycompany.app.pages.UserProfilePage;
import lombok.Getter;
import org.openqa.selenium.opera.OperaDriver;

@Getter
public class MyThreadCheckPlaceOfHome implements Runnable {
    private UserProfilePage userProfilePage;
    private volatile String result;
    private OperaDriver browser;

    public MyThreadCheckPlaceOfHome(UserProfilePage userProfilePage, OperaDriver browser) {
        this.userProfilePage = userProfilePage;
        this.browser = browser;
    }

    @Override
    public void run() {
        result = "";

        if (!browser.findElementsByXPath(userProfilePage.getHomeXPath()).isEmpty()) {
            result = userProfilePage.getHomePlaceInfo().getText();
        }
    }
}
