package com.vehiclecheck.test.pageObjects;

import com.vehiclecheck.test.helpers.PageObject;
import org.openqa.selenium.By;

public class SearchPage extends PageObject {
    private final By vehicleRegistration = By.cssSelector("#vrm-input");
    private final By submit = By.cssSelector("form.HeroInputVrm__component-E1zj button[type='submit']");
    private final By errorMessage = By.cssSelector("div.Toast-shared-module__toastContainer-e8ni");

    public void enterVehicleRegistration(String value){
        enterText(vehicleRegistration,value);
    }

    public void clickOnSubmit(){
        waitForExpectedElement(submit).click();
    }

    public String getErrorMessage(){
        return getText(errorMessage);
    }

}
