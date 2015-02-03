package com.xero.SampleAppUI;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;


public class xeroTest {
    WebDriver driver = new FirefoxDriver();
    
    @Test
   public void login(){
     driver.get("https://login.xero.com");
     (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
	 driver.findElement(By.id("email")).sendKeys("py.111984@gmail.com");;
	 driver.findElement(By.id("password")).sendKeys("test12345");
	 driver.findElement(By.id("submitButton")).click();;
    }
    @Test
    public void navigateToRepeatingTab() throws InterruptedException{
    	(new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='Accounts']/.."))); //div[@id='xero-nav']//div[contains(@class, 'blue-header')]//div[2]//li[2]/a")));

        WebElement e = driver.findElement(By.xpath("//a[@id='Accounts']/.."));
        e.click();           
        driver.findElement(By.linkText("Sales")).click();
        Thread.sleep(4000);
        driver.findElement(By.xpath("//*[@id='ext-gen1039']/span")).click();
        //WebElement b = driver.findElement(By.xpath("//span[@class='sq-new-item-list']"));
        //b.click();  
        //driver.findElement(By.xpath("//ul[@class='x-list']/li[@class='ico-repeating-invoice']")).click();
        driver.findElement(By.xpath("//*[@id='ext-gen1038']/li[3]/a")).click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("PeriodUnit")));
        driver.findElement(By.id("PeriodUnit")).clear();
        driver.findElement(By.id("PeriodUnit")).sendKeys("2");
        Thread.sleep(2000);
        driver.findElement(By.id("TimeUnit_value")).clear();
        driver.findElement(By.id("TimeUnit_value")).sendKeys("Month(s)");
        driver.findElement(By.id("TimeUnit_value")).sendKeys(Keys.TAB);
        Thread.sleep(2000);
        driver.findElement(By.id("StartDate")).clear();
        driver.findElement(By.id("StartDate")).sendKeys("11 Feb 2015");
        Thread.sleep(2000);
        driver.findElement(By.id("DueDateDay")).sendKeys("23");
        Thread.sleep(2000);
        driver.findElement(By.id("DueDateType_value")).clear();
        driver.findElement(By.id("DueDateType_value")).sendKeys("of the current month");
        driver.findElement(By.id("DueDateType_value")).sendKeys(Keys.TAB);
        Thread.sleep(2000);
        driver.findElement(By.id("EndDate")).sendKeys("19 Mar 2015");
        Thread.sleep(2000);
        driver.findElement(By.id("saveAsDraft")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[contains(@id,'PaidToName_')][1]")).sendKeys("xero");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[contains(@id,'Reference_')][1]")).sendKeys("Demo NZ)");
        Thread.sleep(2000);
        driver.findElement(By.xpath(".//*[@id='ext-gen99']/div[3]/div/span[1]/button")).click();
        
        
    }
}
