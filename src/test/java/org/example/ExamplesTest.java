package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

public class ExamplesTest {

    WebDriver driver;

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
    public void staleElementTest() {
        System.out.println("It's ok!");
    }
}
