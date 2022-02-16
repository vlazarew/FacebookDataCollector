package com.mycompany.app.pages;

import com.google.errorprone.annotations.concurrent.LazyInit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private final String email;
    private final String password;

    public LoginPage(OperaDriver browser, String email, String password) {
        PageFactory.initElements(browser, this);
        this.email = email;
        this.password = password;
    }

    @LazyInit
    @FindBy(xpath = "//*[contains(@id, 'email')]")
    private WebElement emailTextBox;

    @LazyInit
    @FindBy(xpath = "//*[contains(@id, 'pass')]")
    private WebElement passwordTextBox;

    @LazyInit
    @FindBy(xpath = "//*[contains(@id, 'u_0_b')]")
    private WebElement loginButton;

    public void FillFields() {
        emailTextBox.sendKeys(email);
        passwordTextBox.sendKeys(password);
        loginButton.click();
    }

}
