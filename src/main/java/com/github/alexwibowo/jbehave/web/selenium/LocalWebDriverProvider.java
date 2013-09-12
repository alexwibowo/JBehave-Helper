package com.github.alexwibowo.jbehave.web.selenium;

import org.jbehave.web.selenium.DelegatingWebDriverProvider;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class LocalWebDriverProvider extends DelegatingWebDriverProvider {

    private WebDriver webDriver;

    public String browser = BrowserType.FIREFOX; // default

    private String baseURL;

    @Override
    public synchronized WebDriver get() {
        return webDriver;
    }

    @Override
    @PreDestroy
    public synchronized void end() {
        webDriver.hashCode();
        //don't quit

        webDriver.quit();
    }

    @PostConstruct
    @Override
    public synchronized void initialize() {
        if (browser.equals(BrowserType.FIREFOX)) {
            LoggingPreferences logs = new LoggingPreferences();
            DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
            desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
            webDriver = new FirefoxDriver(desiredCapabilities);
            webDriver.manage().window().setSize(new Dimension(1100, 1000));
        }
        else if (browser.equals(BrowserType.CHROME)) {
            LoggingPreferences logs = new LoggingPreferences();
            DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
            desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
            webDriver = new ChromeDriver(desiredCapabilities);
        }
    }
}
