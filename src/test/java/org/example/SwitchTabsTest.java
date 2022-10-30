package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchTabsTest {
    WebDriver driver;
    int demoPauseInSeconds = 3;
    Logger logger = LoggerFactory.getLogger(SwitchTabsTest.class);

    @BeforeAll
    public static void driverSetup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void driverInit() {
        driver = new ChromeDriver();
    }

    @AfterEach
    public void driverQuit() {
        driver.quit();
    }

    @Test
    public void switchToTheNewTabTest() throws InterruptedException{
        //Save the first tab's ID
        String firstTab = driver.getWindowHandle();
        //Switch to the new tab
        driver.switchTo().newWindow(WindowType.TAB);
        //Pause to witness the switch
        demoPause();
        //Save the second tab's ID
        String secondTab = driver.getWindowHandle();
        //Switch back to the first tab
        driver.switchTo().window(firstTab);
        demoPause();
        //Switch to the second tab again
        driver.switchTo().window(secondTab);
        demoPause();
    }

    @Test
    public void switchBetweenExistingTabsTest() throws InterruptedException {
        //Get two opened tabs
        prepareTabs();
        //Save the current tab's ID
        String originalTab = driver.getWindowHandle();
        //Save other tab's ID
        String otherTab = driver.getWindowHandles() //get IDs of all opened tabs
                .stream()
                .filter(handle -> !originalTab.contentEquals(handle)) //search for the ID, that is not the ID of the current tab
                .findFirst()
                .get();
        //Pause to witness the switch
        demoPause();
        //Switch to the other tab
        driver.switchTo().window(otherTab);
        demoPause();
        //Switch back to the original tab
        driver.switchTo().window(originalTab);
        demoPause();
        //Switch to the second tab again
        driver.switchTo().window(otherTab);
        demoPause();
    }


    private void demoPause() throws InterruptedException {
        try {
            Thread.sleep(demoPauseInSeconds * 1000);
        } catch (InterruptedException e) {
            logger.error("Got InterruptedException while pausing.");
            throw e;
        }
    }

    private void prepareTabs() {
        driver.switchTo().newWindow(WindowType.TAB);
        assert driver.getWindowHandles().size() == 2;
    }
}
