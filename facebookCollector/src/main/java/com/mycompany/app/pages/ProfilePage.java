package com.mycompany.app.pages;

import com.google.errorprone.annotations.concurrent.LazyInit;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProfilePage {

    public ProfilePage(OperaDriver browser) {
        PageFactory.initElements(browser, this);
    }

    @LazyInit
    @FindBy(xpath = "//*[contains(@href, 'https://www.facebook.com/friends/')]")
    private WebElement findFriendsButton;

    @LazyInit
    @FindBy(xpath = "//*[contains(@placeholder, 'Поиск на Facebook')]")
    private WebElement findFriendsInput;

    public void goToFindFriends(){
        findFriendsButton.click();
    }

    public void fillFindFriendsInput(String name, String lastName){
        findFriendsInput.click();
        findFriendsInput.sendKeys(name + " " + lastName);
        findFriendsInput.sendKeys(Keys.RETURN);
    }
}
