package com.mycompany.app.pages;

import com.google.errorprone.annotations.concurrent.LazyInit;
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

    @LazyInit
    @FindBy(xpath = "//div[contains(@aria-label, 'Предпросмотр результата поиска')]/descendant::a[contains(@role, 'link')]")
    private List<WebElement> peopleNameLinks;

    @LazyInit
    @FindBy(xpath = "//*[contains(@href, '/search/people/?')]")
    private WebElement searchByPeopleLink;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Город')]/ancestor::div[contains(@role, 'button')]")
    private WebElement cityPreFilter;

    @LazyInit
    @FindBy(xpath = "//*[contains(@placeholder, 'Выберите город...')]")
    private WebElement cityFilter;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Образование')]/ancestor::div[contains(@role, 'button')]")
    private WebElement educationPreFilter;

    @LazyInit
    @FindBy(xpath = "//*[contains(@placeholder, 'Выберите учебное заведение...')]")
    private WebElement educationFilter;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Работа')]/ancestor::div[contains(@role, 'button')]")
    private WebElement workPreFilter;

    @LazyInit
    @FindBy(xpath = "//*[contains(@placeholder, 'Выберите компанию...')]")
    private WebElement workFilter;

    @LazyInit
    @FindBy(xpath = "//*[contains(@href, '/search/photos/?')]")
    private WebElement searchByPhotoLink;

    @LazyInit
    @FindBy(xpath = "//*[contains(@id, 'jsc_c_n')]")
    private WebElement photoAuthorFilter;

    @LazyInit
    @FindBy(xpath = "//*[contains(@id, 'jsc_c_q')]")
    private WebElement photoPlaceFilter;

    public void clickSearchByPeopleLink(){
        searchByPeopleLink.click();
    }

    public void fillAndClickCityFilter(String city, WebDriver browser){
        // Фильтр на город
        cityPreFilter.click();
        cityFilter.sendKeys(city);
        WebElement cityPostFilter = browser.findElement(By.xpath("//li[contains(@role, 'option')]//span[contains(text(), " + city + ")]"));
        cityPostFilter.click();
        //
    }

    public void fillAndClickPlaceOfStudyFilter(String placeOfStudy, WebDriver browser){
        // Фильтр на город
        educationPreFilter.click();
        educationFilter.sendKeys(placeOfStudy);
        WebElement educationPostFilter = browser.findElement(By.xpath("//li[contains(@role, 'option')]//span[contains(text(), " + placeOfStudy + ")]"));
        educationPostFilter.click();
        //
    }

    public void fillAndClickPlaceOfWorkFilter(String placeOfWork, WebDriver browser){
        // Фильтр на город
        workPreFilter.click();
        workFilter.sendKeys(placeOfWork);
        WebElement workPostFilter = browser.findElement(By.xpath("//li[contains(@role, 'option')]//span[contains(text(), " + placeOfWork + ")]"));
        workPostFilter.click();
        //
    }
}
