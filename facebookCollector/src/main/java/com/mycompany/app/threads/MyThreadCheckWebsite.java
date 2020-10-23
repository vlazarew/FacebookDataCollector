package com.mycompany.app.threads;

import com.mycompany.app.pages.UserProfilePage;
import lombok.Getter;
import org.openqa.selenium.opera.OperaDriver;

@Getter
public class MyThreadCheckWebsite implements Runnable {
    private UserProfilePage userProfilePage;
    private volatile String result;
    private OperaDriver browser;

    public MyThreadCheckWebsite(UserProfilePage userProfilePage, OperaDriver browser) {
        this.userProfilePage = userProfilePage;
        this.browser = browser;
    }

    @Override
    public void run() {
        result = "";

        if (!browser.findElementsByXPath(userProfilePage.getWebsiteXPath()).isEmpty()) {
            result = userProfilePage.getWebsiteInfo().getText();
        }
    }
}
