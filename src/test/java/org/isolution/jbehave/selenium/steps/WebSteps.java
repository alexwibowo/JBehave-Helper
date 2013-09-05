package org.isolution.jbehave.selenium.steps;

import org.isolution.jbehave.Steps;
import org.isolution.jbehave.web.selenium.BaseWebSteps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertTrue;

@Steps
public class WebSteps extends BaseWebSteps{

    @Given("I am visiting '$address'")
    public void visitingAWebPage(String address) {
        WebDriver webDriver = getWebDriver();
        webDriver.navigate().to(address);
    }

    @Then("I should see the '$buttonLabel' button")
    public void assertButtonExists(String buttonLabel) {
        WebDriver webDriver = getWebDriver();
        WebElement element = webDriver.findElement(By.xpath("//button/span[text()='" + buttonLabel +"']"));
        assertTrue(element.isDisplayed());
        assertTrue(element.isEnabled());
    }
}
