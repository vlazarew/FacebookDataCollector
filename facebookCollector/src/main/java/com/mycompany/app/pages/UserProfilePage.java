package com.mycompany.app.pages;

import com.google.errorprone.annotations.concurrent.LazyInit;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class UserProfilePage {
    public UserProfilePage(OperaDriver browser) {
        PageFactory.initElements(browser, this);
    }

    @FindBy(xpath = "//span[(text() = 'Информация')]/ancestor::a")
    private WebElement informationButton;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Общие сведения')]/ancestor::a")
    private WebElement generalInfoButton;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Работа и образование')]/ancestor::a")
    private WebElement workAndEducationInfoButton;

    @LazyInit
    @FindBy(xpath = "//img[(contains(@src, 'https://static.xx.fbcdn.net/rsrc.php/v3/yt/r/Bo7x4xsiTje.png'))]/parent::div/following-sibling::div/div")
    private WebElement workInfo;

    @LazyInit
    @FindBy(xpath = "//img[(contains(@src, 'https://static.xx.fbcdn.net/rsrc.php/v3/yN/r/j-QTXcNyQBK.png'))]/parent::div/following-sibling::div/div")
    private WebElement professionEducationInfo;

    @LazyInit
    @FindBy(xpath = "//img[(contains(@src, 'https://static.xx.fbcdn.net/rsrc.php/v3/yS/r/poZ_P5BwYaV.png'))]/parent::div/following-sibling::div/div")
    private WebElement homePlaceInfo;

    @LazyInit
    @FindBy(xpath = "//h1")
    private WebElement NamesInfo;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Места проживания')]/ancestor::a")
    private WebElement livesPlacesInfoButton;

    @LazyInit
    @FindBy(xpath = "//span[(text() = 'Контактная и основная информация')]/ancestor::a")
    private WebElement contactInfoButton;

    @LazyInit
    @FindBy(xpath = "//img[(contains(@src, 'https://static.xx.fbcdn.net/rsrc.php/v3/yI/r/lzvufuLgbzd.png'))]/parent::div/following-sibling::div/div/div/div/div/div/span")
    private List<WebElement> phoneNumberInfo;

    @LazyInit
    @FindBy(xpath = "//img[(contains(@src, 'https://static.xx.fbcdn.net/rsrc.php/v3/yk/r/9p_UfjPVxUp.png'))]/parent::div/following-sibling::div/div/div/div/div/div/span")
    private List<WebElement> emailInfo;

    @LazyInit
    @FindBy(xpath = "//img[(contains(@src, 'https://static.xx.fbcdn.net/rsrc.php/v3/yk/r/lDkqhYEMOUY.png'))]/parent::div/following-sibling::div/descendant::a")
    private WebElement websiteInfo;

    @LazyInit
    @FindBy(xpath = "//*[local-name() = 'svg'][contains(@role, 'img')]/*[local-name() = 'g']/*[local-name() = 'image']")
    private WebElement userPhoto;
}
