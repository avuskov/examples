package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnloadPageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final int timeoutInSeconds = 3;
    private final Logger logger = LoggerFactory.getLogger(UnloadPageTest.class);


    @BeforeAll
    public static void driverSetup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void driverInit() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    @AfterEach
    public void driverQuit() {
        driver.quit();
    }

    @Test
    public void waitForUnloadByKeyElementsTest() {
        String url = "https://www.wikipedia.org/";
        String otherUrl = "https://ru.wikipedia.org/wiki";
        List<String> keyElements = List.of(
                "//h1[@class='central-textlogo-wrapper']",
                "//div[@class='lang-list-button-wrapper']",
                "//div[@id='search-input']"
        );

        driver.get(url);
        assertTrue(waitForThePageToLoad(keyElements));
        assertFalse(waitForThePageToUnload(keyElements));

        driver.get(otherUrl);
        assertTrue(waitForThePageToUnload(keyElements));
        assertFalse(waitForThePageToLoad(keyElements));
    }

    @Test
    public void waitForUnloadByTitleTest() {
        String url = "https://www.wikipedia.org/";
        String otherUrl = "https://ru.wikipedia.org/wiki";

        driver.get(url);
        String originalTitle = driver.getTitle();

        driver.get(otherUrl);
        String currentTitle = driver.getTitle();

        assertNotEquals(originalTitle, currentTitle);
    }

    @SuppressWarnings("unchecked")
    private boolean waitForThePageToLoad(List<String> keyElements) {
        try {
            ExpectedCondition<WebElement>[] conditions = keyElements
                    .stream()
                    .map(xpath -> ExpectedConditions.elementToBeClickable(By.xpath(xpath)))
                    .toArray(ExpectedCondition[]::new);
            wait.until(ExpectedConditions.and(conditions));
            logger.info("The page is loaded.");
            return true;
        } catch(TimeoutException e) {
            logger.info("The page is not loaded!");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean waitForThePageToUnload(List<String> keyElements) {
        try {
            ExpectedCondition<WebElement>[] conditions = keyElements
                    .stream()
                    .map(xpath -> ExpectedConditions.numberOfElementsToBe(By.xpath(xpath), 0))
                    .toArray(ExpectedCondition[]::new);
            wait.until(ExpectedConditions.and(conditions));
            logger.info("The page is unloaded.");
            return true;
        } catch(TimeoutException e) {
            logger.info("The page is not unloaded!");
            return false;
        }
    }

}
