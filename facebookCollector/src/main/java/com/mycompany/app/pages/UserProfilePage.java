package com.mycompany.app.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class UserProfilePage {
    public UserProfilePage(OperaDriver browser) {
        PageFactory.initElements(browser, this);
    }

    @FindBy(xpath = "//span[(text() = 'Информация')]/ancestor::a")
    private WebElement informationButton;
}
