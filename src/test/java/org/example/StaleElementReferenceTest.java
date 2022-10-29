package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v104.dom.DOM;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StaleElementReferenceTest {

    WebDriver driver;
    int timeoutInSeconds = 10;

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
    public void staleElementHandlingByCatchTest() {
        String url = "https://www.wikipedia.org/";
        String xpath = "//h1[@class='central-textlogo-wrapper']";
        String text;
        driver.get(url);
        WebElement element = getElement(xpath);
        text = getTextOfAPossibleStaleElement(element, xpath);
        printText(text, "1");
        makeElementsStale();
        text = getTextOfAPossibleStaleElement(element, xpath);
        printText(text, "2");
    }

//    @Test
    public void staleElementHandlingByDOMChangeWatchingTest() {
        String url = "https://www.wikipedia.org/";
        String xpath = "//h1[@class='central-textlogo-wrapper']";
        String text;
        driver.get(url);
        WebElement element = getElement(xpath);
        text = element.getText();
        printText(text, "1");

        //TODO Doesn't work that way, need to search for more info.
        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.addListener(DOM.documentUpdated(), event -> {
            System.out.println("=== Caught DOM change! ===");
        });
        makeElementsStale();
    }

    private WebElement getElement(String xpath) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    private void makeElementsStale() {
        driver.navigate().refresh();
    }

    private void printText(String text, String addMessage) {
        System.out.println(addMessage);
        System.out.println("---------------------------------");
        System.out.println(text);
        System.out.println("---------------------------------");
    }

    private String getTextOfAPossibleStaleElement(WebElement element, String xpath) {
        String result;
        try {
            result = element.getText();
            printText("The element was not stale.", "getTextOfAPossibleStaleElement");
        } catch(StaleElementReferenceException e) {
            printText("Caught StaleElementReferenceException.", "getTextOfAPossibleStaleElement");
            WebElement refreshedElement = getElement(xpath);
            result = refreshedElement.getText();
        }
        return result;
    }
}
