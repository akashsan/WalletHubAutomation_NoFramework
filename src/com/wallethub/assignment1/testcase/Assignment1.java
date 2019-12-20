package com.wallethub.assignment1.testcase;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Assignment1 {

	static Properties prop = null;
	static WebDriver driver = null;

	public static void main(String[] args) {

		// Initialize Chrome
		System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// launch facebook
		driver.get("http://www.facebook.com");

		// Reading login parameter from properties file
		String emailid = propertiesForFacebook().getProperty("login");

		// Reading password parameter from properties file
		String password = propertiesForFacebook().getProperty("password");

		loginToFacebook(emailid, password, driver);

		postMessageOnFacebook("Hello World", driver);
		
		System.out.println("Assignment No.1 successfully completed");
		
		driver.quit();
	}

	public static void loginToFacebook(String emailid, String password, WebDriver driver) {

		// Entering Email Id to email id text box
		WebElement emailtxtbox = driver.findElement(By.id("email"));
		emailtxtbox.sendKeys(emailid);

		// Entering password in password textbox
		WebElement passwordtxtbox = driver.findElement(By.id("pass"));
		passwordtxtbox.sendKeys(password);

		// Clicking on login button
		WebElement loginbttn = driver.findElement(By.xpath("//input[@value='Log In']"));
		loginbttn.click();

		// Click escape key to close the show notification.
		WebElement body = driver.findElement(By.tagName("body"));
		body.sendKeys(Keys.ESCAPE);

	}

	public static void postMessageOnFacebook(String msg, WebDriver driver) {

		// locator for "Write something here..."
		WebElement msg_txtbox = driver.findElement(By.name("xhpc_message"));

		// Enter text in the text box
		msg_txtbox.sendKeys(msg);

		// locator to post button
		WebElement post = driver.findElement(By.xpath("//button[@data-testid='react-composer-post-button']"));

		// click on post button
		post.click();
	}
	
	
	public static Properties propertiesForFacebook() {
		try (InputStream input = new FileInputStream("resources//facebook_credentials.properties")) {

			prop = new Properties();

			// load a properties file
			prop.load(input);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;

	}

}
