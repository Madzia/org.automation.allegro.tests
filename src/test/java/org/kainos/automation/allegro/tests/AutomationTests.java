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
	private WebDriverWait wait;
	private String output;
	private String websiteUrl = "http://www.allegro.pl";
	private String searchedPhrase = "smartwatch";
	private String searchFieldId = "main-search-text";
	private String searchButtonId = "//input[@class='sprite search-btn']";
	private String sortDropDownListArrowId = "//div[@class='listing-sort-dropdown']/descendant::span[@class='arrow']";
	private String elementOnDropDownListId = "//dd[2]/child::a[1]";
	private String firstAuctionElementId = "//article[1]/descendant::a[1]";
	private String findingFirstSentenceForWait = "//div[@class='listing-sort-dropdown active']";
	private String findingSecondtSentenceForWait = "//div[@id='paymentShipment']";
	private String priceValueId = "priceValue";
	private String priceValueAttributeId = "data-price";
	private String deliveryId = "//a[@class='siTabs' and @href='#delivery']";
	private String xPathInputTd = "//div[@class='deliveryAndPayment']/descendant::tr[td]/child::td[1]";
	private String outputBegin = "<div class=\"suite-section-content\"><ul>";
	private String ilBegin  = "<li>";
	private String ilEnd = "</li>";
	private String outputEnd = "</ul></div>";
	private int waitingPeriodInSeconds = 5;	
	private int numberOfInstancesFirstSentenceForWait = 0;	
	private int numberOfInstancesSecondSentenceForWait = 1;	
	//in data-price value is multiplied times 100, so it's faster also multiply 1000 times 100
	//and compare that values
	private int priceValueMultiply100 = 100000;
	
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
		driver.findElement(By.id(searchFieldId)).sendKeys(searchedPhrase);
		
		//Click the magnifier button
		driver.findElement(By.xpath(searchButtonId)).click();
		
		//Locate the web elements
		sortPicklist = driver.findElement(By.xpath(sortDropDownListArrowId));
		sortHighest = driver.findElement(By.xpath(elementOnDropDownListId));
		
		//Select "cena: od najwyższej"
		new Actions(driver).click(sortPicklist)
		.click(sortHighest)
		.perform();
		
		//wait until web page will be sorted = dropdown list won't be active
		wait = new WebDriverWait(driver, waitingPeriodInSeconds);
		waitingClass(findingFirstSentenceForWait, wait, numberOfInstancesFirstSentenceForWait);
		
		//Click first auction
		driver.findElement(By.xpath(firstAuctionElementId)).click();
		
		//wait until web page will be loaded = payment panel exists
		waitingClass(findingSecondtSentenceForWait, wait, numberOfInstancesSecondSentenceForWait);
		
		//test for price > 1000 zł
		assert (Integer.parseInt(driver.findElement(By.id(priceValueId)).getAttribute(priceValueAttributeId))>priceValueMultiply100 ? true : false);
		
		//click on "Dostawa i płatność"
		driver.findElement(By.xpath(deliveryId)).click();
        
		//Download and write into log available supply options. Output in index.html -> Reporter output.
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
		output = outputBegin;
		for (WebElement item : allSupplyOptionsInTable)
			output = new String(output+ilBegin+item.getText()+ilEnd);
		return output+outputEnd;
	}
}