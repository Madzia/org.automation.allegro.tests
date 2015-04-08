package org.kainos.automation.allegro.tests;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AutomationTests {

	private WebDriver driver;
	private String websiteUrl = new String("http://www.allegro.pl");
	private String searchedPhrase = new String("smartwatch");

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage()
			.timeouts()
			.implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	@BeforeMethod(alwaysRun = true)
	public void openHomePage() {
		driver.get(websiteUrl);
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}
	
	@Test
	public void testSupplyOptionsForMostExpensiveSmartwatch() {
		//Enter into text field searched phrase
		driver.findElement(By.id("main-search-text")).sendKeys(searchedPhrase);
		
		//Click the magnifier button
		driver.findElement(By.xpath("//input[@class='sprite search-btn']")).click();
		
		//Locate the web elements
		WebElement sortPicklist = driver.findElement(By.xpath("//div[@class='listing-sort-dropdown']/descendant::span[@class='arrow']"));
        WebElement sortHighest = driver.findElement(By.xpath("//dd[2]/child::a[1]"));
		
        //Select "cena: od najwyższej"
		new Actions(driver).click(sortPicklist)
		.click(sortHighest)
		.perform();
		
	}
}
