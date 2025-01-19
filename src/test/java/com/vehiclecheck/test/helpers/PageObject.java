package com.vehiclecheck.test.helpers;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class PageObject {

    private static final Duration DRIVER_WAIT_TIME = Duration.ofSeconds(10);
    private final WebDriverWait wait = new WebDriverWait(getWebDriver(), DRIVER_WAIT_TIME);

    public WebDriver getWebDriver() {
        return WebDriverHelper.getWebDriver();
    }

    public WebElement waitForExpectedElement(final By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void enterText(final By by, String value) {
        enterText(waitForExpectedElement(by), value);
    }

    public void enterText(WebElement ele, String value) {
        ele.clear();
        ele.sendKeys(Keys.BACK_SPACE);
        ele.sendKeys(value);
    }

    public String getText(By by) {
        try {
            WebElement ele = waitForExpectedElement(by);
            return ele.getText();
        } catch (NoSuchElementException | TimeoutException e) {
            return StringUtils.EMPTY;
        }
    }
}
