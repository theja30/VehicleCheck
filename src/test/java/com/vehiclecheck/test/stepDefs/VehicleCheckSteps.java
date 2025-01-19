package com.vehiclecheck.test.stepDefs;

import com.opencsv.exceptions.CsvValidationException;
import com.vehiclecheck.test.helpers.Constants;
import com.vehiclecheck.test.helpers.TestUtils;
import com.vehiclecheck.test.helpers.WebDriverHelper;
import com.vehiclecheck.test.pageObjects.MileagePage;
import com.vehiclecheck.test.pageObjects.SearchPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.*;
import org.assertj.core.api.SoftAssertions;

@Slf4j
public class VehicleCheckSteps {
    public static Set<String> regDetails = new HashSet<>();
    public static List<Map<String, String>> vehicleDetailsList = new ArrayList<>();
    private final SearchPage searchPage;
    private final MileagePage mileagePage;

    SoftAssertions softly = new SoftAssertions();

    public VehicleCheckSteps(SearchPage searchPage, MileagePage mileagePage) {
        this.searchPage = searchPage;
        this.mileagePage = mileagePage;
    }

    @Given("fetch vehicle reg numbers from {string} file")
    public void fetchVehicleRegNumbersFromFile(String inputFileName) throws IOException {
        regDetails = TestUtils.fetchSetOfPatternMatchesFromFile(Constants.VEHICLE_REGISTRATION_NUMBER_PATTERN, Constants.INPUT_DIRECTORY_PATH + inputFileName);
        log.info("fetched the Vehicle registration number from input file : {}", regDetails);
    }

    @When("fetch vehicle details from vehicle valuation site")
    public void fetchVehicleDetailsFromVehicleValuationSite() {
        vehicleDetailsList = regDetails.stream().map(this::fetchVehicleDetails).toList();
    }

    private Map<String, String> fetchVehicleDetails(String regNo) {
        log.info("fetching the Vehicle details from vehicle valuation Site for Registration number : {}", regNo);
        navigateToVehicleValuationSite();
        Map<String, String> vehicleDetails = new HashMap<>();
        vehicleDetails.put("VARIANTREG", regNo);
        searchPage.enterVehicleRegistration(regNo);
        searchPage.clickOnSubmit();
        String errorMessage = searchPage.getErrorMessage();
        if (StringUtils.isNotBlank(errorMessage)) {
            softly.fail("Error to fetch Vehicle details with Registration number : " + regNo + " on Vehicle valuation site with error :" + errorMessage);
            log.info("Error to fetch Vehicle details with Registration number : {}", regNo);
            vehicleDetails.put("MAKEMODEL", errorMessage);
            vehicleDetails.put("YEAR", errorMessage);
        } else {
            vehicleDetails.put("MAKEMODEL", mileagePage.getVehicleMakeAndModel());
            vehicleDetails.put("YEAR", mileagePage.getVehicleyear());
        }
        return vehicleDetails;
    }

    private void navigateToVehicleValuationSite() {
        searchPage.getWebDriver().navigate().to(WebDriverHelper.getUrl());
    }

    @Then("Compare the vehicle details with {string} file")
    public void compareTheVehicleDetailsWithFile(String ouputFileName) throws CsvValidationException, IOException {
        List<Map<String, String>> actualOutputVehicleDetailsList = TestUtils.readCSVFileFromPath(Constants.OUTPUT_DIRECTORY_PATH + ouputFileName);
        vehicleDetailsList.forEach(expectedOutPutVehicleDetails -> {
            String regNo = expectedOutPutVehicleDetails.get("VARIANTREG").replaceAll("\\s+", "");
            Map<String, String> actualOutputVehicleDetails = actualOutputVehicleDetailsList.stream().filter(rec -> rec.containsKey("VARIANTREG") && regNo.equalsIgnoreCase(rec.get("VARIANTREG").replaceAll("\\s+", ""))).findFirst().orElse(null);
            if (actualOutputVehicleDetails == null) {
                softly.fail("Vehicle with Registration number : " + regNo + " present in Input File but missing in output file" /*expectedOutPutVehicleDetails*/);
                log.info("Vehicle with Registration number : {} present in Input File but missing in output file", regNo);
            } else {
                softly.assertThat(actualOutputVehicleDetails.get("MAKEMODEL"))
                        .as("Vehicle Make and model mismatch for car with Registration No : " + regNo)
                        .isEqualTo(expectedOutPutVehicleDetails.get("MAKEMODEL"));
                softly.assertThat(actualOutputVehicleDetails.get("YEAR"))
                        .as("Vehicle year mismatch for car with Registration No : " + regNo)
                        .isEqualTo(expectedOutPutVehicleDetails.get("YEAR"));
            }
        });
        List<Map<String, String>> extraVehicleDetails = actualOutputVehicleDetailsList.stream().filter(vehicleDetails -> !regDetails.contains(vehicleDetails.get("VARIANTREG").replaceAll("\\s+", ""))).toList();
        if (!extraVehicleDetails.isEmpty()) {
            softly.fail("Vehicle details present in OutPut file but not present in Input File : " + extraVehicleDetails.stream().map(vehicleDetails -> vehicleDetails.get("VARIANTREG")).toList());
            log.info("Vehicle details present in OutPut file but not present in Input File : {}", extraVehicleDetails.stream().map(vehicleDetails -> vehicleDetails.get("VARIANTREG")).toList());
        }
        softly.assertAll();
    }
}
