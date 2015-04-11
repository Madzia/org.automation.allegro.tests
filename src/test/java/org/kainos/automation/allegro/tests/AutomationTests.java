// Unauthorized copying of this file, via any medium is strictly prohibited.
//
// File name:   AutomationTests.java
// Created:     10/04/2015 by Magdalena Sarzyńska <magda2609@gmail.com>.
// Description: Test for download and write into log available supply options 
// from www.allegro.pl website most expensive watch.

package org.kainos.automation.allegro.tests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AutomationTests {

	private WebDriver driver;	
	private WebElement sortPicklist;
	private WebElement sortHighest;
	private List<WebElement> allSupplyOptionsInTable;
	private String websiteUrl = new String("http://www.allegro.pl");
	private String searchedPhrase = new String("smartwatch");
	private String findingSentence;
	private String output;
	private String xPathInputTd;
	private int waitingPeriodInSeconds = new Integer(5);	
	private int numberOfInstances = new Integer(0);	
	private WebDriverWait wait;
	
	
	
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage()
			.timeouts()
			.implicitlyWait(waitingPeriodInSeconds, TimeUnit.SECONDS);
	}
	
	@BeforeMethod(alwaysRun = true)
	public void openHomePage() {
		driver.get(websiteUrl);
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}
	
	///Test for download and write into log available supply options from www.allegro.pl website most expensive watch.
	@Test
	public void testSupplyOptionsForMostExpensiveSmartwatch(){
		
		//Enter into text field searched phrase
		driver.findElement(By.id("main-search-text")).sendKeys(searchedPhrase);
		
		//Click the magnifier button
		driver.findElement(By.xpath("//input[@class='sprite search-btn']")).click();
		
		//Locate the web elements
		sortPicklist = driver.findElement(By.xpath("//div[@class='listing-sort-dropdown']/descendant::span[@class='arrow']"));
		sortHighest = driver.findElement(By.xpath("//dd[2]/child::a[1]"));
		
		//Select "cena: od najwyższej"
		new Actions(driver).click(sortPicklist)
		.click(sortHighest)
		.perform();
		
		//wait until web page will be sorted = dropdown list won't be active
		findingSentence = new String("//div[@class='listing-sort-dropdown active']");
		numberOfInstances = new Integer(0);		
		wait = new WebDriverWait(driver, waitingPeriodInSeconds);
		waitingClass(findingSentence, wait, numberOfInstances);
		
		//Click first auction
		driver.findElement(By.xpath("//article[1]/descendant::a[1]")).click();
		
		//wait until web page will be loaded = payment panel exists
		findingSentence = new String("//div[@id='paymentShipment']");
		numberOfInstances = new Integer(1);
		waitingClass(findingSentence, wait, numberOfInstances);
		
		//test for price > 1000 zł (in data-price value is multiplied times 100, so it's faster also multiply 1000 times 100
		//and compare that values
		assert (Integer.parseInt(driver.findElement(By.id("priceValue")).getAttribute("data-price"))>100000 ? true : false);
		
		//click on "Dostawa i płatność"
		driver.findElement(By.xpath("//a[@class='siTabs' and @href='#delivery']")).click();
        
		//Download and write into log available supply options. Output in index.html -> Reporter output.
		xPathInputTd = new String("//div[@class='deliveryAndPayment']/descendant::tr[td]/child::td[1]");
		Reporter.log(returnSupplyOptionsFromXpathInput(xPathInputTd));
	}

	///Method will find all elements that contains elementXpath, will count them and compare with numberOfInstances.
	///Wait would been stopped when that values will be equal.
	private void waitingClass(final String elementXpath, WebDriverWait wait, final int numberOfInstances) {
		ExpectedCondition<Boolean> conditionToCheck = new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver input) {
	            return (input.findElements(By.xpath(elementXpath)).size() == numberOfInstances);
	        }
	    };
	    wait.until(conditionToCheck);
	}	
	
	///Function load all WebElements agreed with xPathInput each one add to unordered output list 
	private String returnSupplyOptionsFromXpathInput(String xPathInput) {
		allSupplyOptionsInTable = driver.findElements(By.xpath(xPathInput));
		output = new String("<div class=\"suite-section-content\"><ul>");
		for (WebElement item : allSupplyOptionsInTable)
			output = new String(output+"<li>"+item.getText()+"</li>");
		return output+"</ul></div>";
	}
}