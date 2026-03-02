package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProductNewProductAppearsInProductList(final ChromeDriver driver) {
        final String productName = "Selenium Product " + UUID.randomUUID();
        final String productQuantity = "7";

        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl + "/product/create");

        final WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("nameInput"))
        );
        nameInput.clear();
        nameInput.sendKeys(productName);

        final WebElement qtyInput = driver.findElement(By.id("quantityInput"));
        qtyInput.clear();
        qtyInput.sendKeys(productQuantity);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/product/list"),
                ExpectedConditions.visibilityOfElementLocated(By.tagName("table"))
        ));

        final String pageSource = driver.getPageSource();
        final boolean isSuccessful =
                pageSource.contains(productName) && pageSource.contains(productQuantity);

        assertTrue(isSuccessful, "Created product should appear in Product List page");
    }
}