package com.test.katalon;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class SearchFilters {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.gecko.driver", "geckodriver");
    driver = new FirefoxDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testSearchFilters() throws Exception {
    driver.get("https://feedback.hschat.app/#/");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[1]")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [doubleClick | xpath=(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[1] | ]]
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[1]")).click();
    driver.findElement(By.xpath("//div[@id='app']/div/div/div")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[2]")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [doubleClick | xpath=(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[2] | ]]
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::button[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[1]")).clear();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[1]")).sendKeys("admin@hschat.app");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[2]")).clear();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::input[2]")).sendKeys("Admin123");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Log-in to the HSChat Admin Tool'])[1]/following::span[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Feedback'])[1]/following::button[1]")).click();
    driver.findElement(By.id("userStatusFilter")).click();
    new Select(driver.findElement(By.id("userStatusFilter"))).selectByVisibleText("Egal");
    driver.findElement(By.id("userStatusFilter")).click();
    new Select(driver.findElement(By.id("userStatusFilter"))).selectByVisibleText("Aktiv");
    driver.findElement(By.id("userStatusFilter")).click();
    new Select(driver.findElement(By.id("userStatusFilter"))).selectByVisibleText("Deaktiviert");
    driver.findElement(By.id("userStatusFilter")).click();
    new Select(driver.findElement(By.id("userStatusFilter"))).selectByVisibleText("Seit 1j deaktiviert");
    driver.findElement(By.id("verifiedFilter")).click();
    driver.findElement(By.id("userStatusFilter")).click();
    new Select(driver.findElement(By.id("userStatusFilter"))).selectByVisibleText("Egal");
    driver.findElement(By.id("verifiedFilter")).click();
    new Select(driver.findElement(By.id("verifiedFilter"))).selectByVisibleText("Verifiziert");
    driver.findElement(By.id("verifiedFilter")).click();
    new Select(driver.findElement(By.id("verifiedFilter"))).selectByVisibleText("Nicht Verifiziert");
    driver.findElement(By.id("verifiedFilter")).click();
    new Select(driver.findElement(By.id("verifiedFilter"))).selectByVisibleText("Egal");
    driver.findElement(By.id("roleFilter")).click();
    new Select(driver.findElement(By.id("roleFilter"))).selectByVisibleText("Keine Rolle");
    driver.findElement(By.id("roleFilter")).click();
    new Select(driver.findElement(By.id("roleFilter"))).selectByVisibleText("Admin");
    driver.findElement(By.id("verifiedFilter")).click();
    new Select(driver.findElement(By.id("verifiedFilter"))).selectByVisibleText("Verifiziert");
    driver.findElement(By.id("userStatusFilter")).click();
    new Select(driver.findElement(By.id("userStatusFilter"))).selectByVisibleText("Deaktiviert");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Oliver'])[1]/following::td[1]")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
