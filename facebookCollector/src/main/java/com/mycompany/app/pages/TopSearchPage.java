package com.mycompany.app.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class TopSearchPage {

    public TopSearchPage(OperaDriver browser) {
        PageFactory.initElements(browser, this);
    }

    @FindBy(xpath = "//div[contains(@aria-label, 'Предпросмотр результата поиска')]/descendant::a[contains(@role, 'link')]")
    private List<WebElement> peopleNameLinks;

    @FindBy(xpath = "//*[contains(@href, '/search/people/?')]")
    private WebElement searchByPeopleLink;

    @FindBy(xpath = "//span[(text() = 'Город')]/ancestor::div[contains(@role, 'button')]")
    private WebElement cityPreFilter;

    @FindBy(xpath = "//*[contains(@placeholder, 'Выберите город...')]")
    private WebElement cityFilter;

    @FindBy(xpath = "//*[contains(@id, 'jsc_c_1f')]")
    private WebElement educationFilter;

    @FindBy(xpath = "//*[contains(@id, 'jsc_c_1i')]")
    private WebElement workFilter;

    @FindBy(xpath = "//*[contains(@href, '/search/photos/?')]")
    private WebElement searchByPhotoLink;

    @FindBy(xpath = "//*[contains(@id, 'jsc_c_n')]")
    private WebElement photoAuthorFilter;

    @FindBy(xpath = "//*[contains(@id, 'jsc_c_q')]")
    private WebElement photoPlaceFilter;

    public void clickSearchByPeopleLink(){
        searchByPeopleLink.click();
    }

    public void clickAll(String city, WebDriver browser){

        // Фильтр на город
        cityPreFilter.click();
        cityFilter.sendKeys("Москва");
        WebElement cityPostFilter = browser.findElement(By.xpath("//li[contains(@role, 'option')]//span[contains(text(), " + city + ")]"));
        cityPostFilter.click();
        //

        /*educationFilter.click();
        workFilter.click();
        searchByPhotoLink.click();
        photoAuthorFilter.click();
        photoPlaceFilter.click();*/
    }
}
