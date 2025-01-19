package com.vehiclecheck.test.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", tags = "@vehicleCheck and not @bug and not @wip and not @ignore", monochrome = true, plugin = {
        "pretty", "html:target/cucumber-report/cucumber.html",
        "json:target/cucumber-report/cucumber.json",
        "rerun:target/cucumber-report/rerun.txt"},
        glue = "com.vehiclecheck.test.stepDefs")
public class RunCucumberTest {
}
