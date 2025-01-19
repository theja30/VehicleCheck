package com.vehiclecheck.test.pageObjects;

import com.vehiclecheck.test.helpers.PageObject;
import org.openqa.selenium.By;

public class MileagePage extends PageObject {


    private final By vehicleMakeAndModel = By.cssSelector("h1.HeroVehicle__title-FAmG");

    private final By vehicleSpecificsYear = By.cssSelector("ul.HeroVehicle__details-XpAI>li:nth-child(1)");

    public String getVehicleMakeAndModel() {
        return getText(vehicleMakeAndModel);
    }

    public String getVehicleyear() {
        return getText(vehicleSpecificsYear);
    }

    public boolean isUserOnMileagePage() {
        return getWebDriver().getCurrentUrl().contains("/mileage");
    }
}
