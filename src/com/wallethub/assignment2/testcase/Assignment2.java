package com.wallethub.assignment2.testcase;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Assignment2 {

	static Properties prop = null;
	static WebDriver driver = null;

	public static void main(String[] args) throws Exception {

		// Initialize Chrome
		System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// open Login URL
		driver.get("https://wallethub.com/join/light");
		driver.findElement(By.linkText("Login")).click();

		// Enter email address
		driver.findElement(By.xpath("//input[@placeholder='Email Address']")).sendKeys("shantu.sanyal@gmail.com");

		// Enter password
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Qwerty@123");

		// click on login button
		driver.findElement(By.xpath("//span[contains(text(), 'Login')]")).click();

		while (!driver.getCurrentUrl().contains("https://wallethub.com/profile/test-insurance-company-")) {
			driver.get("http://wallethub.com/profile/test_insurance_company/");
		}

		// locator for fourth star
		WebElement fourth_star = driver.findElement(By.xpath("//div[@class='review-action ng-enter-element']//*[4]"));

		// code to Hover on the 4th star
		Actions action = new Actions(driver);
		action.moveToElement(fourth_star).build().perform();

		// returns a list of stars that are lit, the locator is below
		List<WebElement> stars_list = driver.findElements(By.xpath(
				"//review-star[@class='rvs-svg']//*[name()='svg']//*[name()='g']//*[name()='path' and @stroke='#4ae0e1']"));

		// Number of stars that are lit
		int stars_lit_size = stars_list.size();

		// This count should be equal to 4, as we hovered to 4th star all 4 stars should
		// lit up
		System.out.println("The count of stars that are lit: " + stars_lit_size);

		// Validating if count is 4
		Assert.assertTrue("The stars did not lit up", stars_lit_size == 4);

		// clicking on the 4th star
		fourth_star.click();

		// code for dropdown

		driver.findElement(By.xpath("//span[contains(text(),'Select...')]")).click();

		List<WebElement> dropdown_list = driver
				.findElements(By.xpath("//span[contains(text(),'Select...')]/parent::div//ul//li"));

		for (WebElement option : dropdown_list) {
			if (option.getText().equals("Health Insurance")) {
				option.click();
			}
		}

		// locator to click on "Write your review text box"
		driver.findElement(By.xpath("//textarea[@placeholder='Write your review...']")).click();

		// Read 200+ text from properties file and type this in textbox
		driver.findElement(By.xpath("//textarea[@placeholder='Write your review...']"))
				.sendKeys(textOf200().getProperty("text"));

		// click submit
		driver.findElement(By.xpath("//div[contains(text(),'Submit')]")).click();

		// Your review has been posted
		Boolean posted_indicator = driver.findElement(By.xpath("//div[@class='rvc-header']/h4")).isDisplayed();

		// Validate if review has been posted
		Assert.assertTrue("Something went wrong", posted_indicator);

		// click on continue button
		driver.findElement(By.xpath("//div[contains(text(), 'Continue')]")).click();

		// locator for my review
		WebElement myReview = driver.findElement(By.xpath("//span[contains(text(),'Your Review')]"));

		// Validate if my review has been updated in the review feed
		Assert.assertTrue("Review feed did not get updated ", myReview.isDisplayed());

		// Scroll to top of the page
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");

		// locator for User's top right icon
		WebElement user = driver
				.findElement(By.xpath("//div[@class='brgm-compact-lists']/following-sibling::div/span"));

		waitForElementToBeVisible(driver, user, 8);

		// locator for profile section under user
		WebElement profile = driver.findElement(By.xpath("//a[@class='brgm-list-it'][contains(text(),'Profile')]"));

		// hover mouse to user and click on profile section
		action.moveToElement(user).moveToElement(profile).click().build().perform();

		// locator for "I RECOMMEND" text
		WebElement reviewOnProfile = driver.findElement(By.xpath("//h2[@class='pr-rec-subtitle']"));

		// Validate if I RECOMMEND locator is displayed
		Assert.assertTrue("The review has not been listed in profile section",
				reviewOnProfile.isDisplayed() && reviewOnProfile.getText().equals("I RECOMMEND"));

		System.out.println(reviewOnProfile.getText());
		System.out.println("Assignment No. 2 successfully completed");
		
		driver.quit();

	}

	public static Properties textOf200() {
		try (InputStream input = new FileInputStream("resources//random200text.properties")) {

			prop = new Properties();

			// load a properties file
			prop.load(input);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;

	}

	public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement locator, int timeout) {
		WebElement element = null;
		try {
			System.out.println("Waiting for max:: " + timeout + " seconds for element to be available");

			WebDriverWait wait = new WebDriverWait(driver, timeout);
			element = wait.until(ExpectedConditions.visibilityOf(locator));
			System.out.println("Element appeared on the web page");
		} catch (Exception e) {
			System.out.println("Element not appeared on the web page");
		}
		return element;
	}

}
