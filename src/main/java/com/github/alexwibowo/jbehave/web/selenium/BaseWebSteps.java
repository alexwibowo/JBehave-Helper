package com.github.alexwibowo.jbehave.web.selenium;

import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseWebSteps {

    @Autowired
    protected WebDriverProvider webDriverProvider;


    public WebDriver getWebDriver() {
        return webDriverProvider.get();
    }
}
