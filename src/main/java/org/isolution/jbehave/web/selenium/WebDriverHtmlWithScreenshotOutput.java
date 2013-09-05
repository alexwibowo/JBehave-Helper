package org.isolution.jbehave.web.selenium;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.web.selenium.WebDriverHtmlOutput;
import org.jbehave.web.selenium.WebDriverProvider;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

public class WebDriverHtmlWithScreenshotOutput extends WebDriverHtmlOutput {

    private static final String SCREENSHOT_PATH_PATTERN = "{0}/screenshots/failed-scenario-{1}.png";
    private static final String SCREENSHOT_LINK_PATTERN = "../screenshots/failed-scenario-{3}.png";

    private WebDriverProvider driverProvider;
    private File outputDirectory;

    public WebDriverHtmlWithScreenshotOutput(PrintStream output, StoryReporterBuilder storyReporterBuilder, WebDriverProvider driverProvider) {
        super(output, storyReporterBuilder.keywords());
        this.outputDirectory = storyReporterBuilder.outputDirectory();
        this.driverProvider = driverProvider;
        overwritePattern("failed", "<div class=\"step failed\">{0} <span class=\"keyword failed\">({1})</span><br/><span class=\"message failed\">$now - {2}</span><br/><a color=\"black\" href=\""+
                SCREENSHOT_LINK_PATTERN + "\"><img src=\"images/failing_screenshot.png\" alt=\"failing screenshot\"/></a></div>\n");
    }

    public void failed(String step, Throwable storyFailure) {
        super.failed(step, storyFailure);

        saveScreenShot((UUIDExceptionWrapper)storyFailure);
    }

    /**
     * Add support for "$now" which will resolve into current date time
     */
    protected String format(String key, String defaultPattern, Object... args) {
        String text = super.format(key, defaultPattern, args);

        text = text.replaceAll("\\$now", ISODateTimeFormat.dateTime().print(new DateTime()));

        return text;
    }


    public void saveScreenShot(UUIDExceptionWrapper uuidWrappedFailure) {
        String screenshotPath = screenshotPath(uuidWrappedFailure.getUUID());
        try {
            WebDriver driver = driverProvider.get();

            if ( driverProvider.saveScreenshotTo(screenshotPath) ) {
                System.out.println("Screenshot has been saved to '" + screenshotPath +"'");
            } else {
                System.out.println(driver.getClass().getName() + " does not support taking screenshots.");
            }
            if( driver instanceof RemoteWebDriver){
                RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(screenshotPath + ".log"));
                    List<LogEntry> logEntries = remoteWebDriver.manage().logs().get(LogType.DRIVER).getAll();
                    for (LogEntry logEntry : logEntries) {
                        pw.println(logEntry.toString());
                    }
                    System.out.println("Logs has been saved to '" + screenshotPath +".log'");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(pw);
                }
                try {
                    pw = new PrintWriter(new FileWriter(screenshotPath + ".html"));
                    pw.print(remoteWebDriver.getPageSource());
                    System.out.println("Page source has been saved to '" + screenshotPath +".html'");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(pw);
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    protected String screenshotPath(UUID uuid) {
        return MessageFormat.format(SCREENSHOT_PATH_PATTERN, outputDirectory, uuid);
    }

}
