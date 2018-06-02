package com.infosys.appium;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;


public class AppiumTest {

	private static AndroidDriver<MobileElement> driver = null;
	private static Utils objUtils;

	public static void main(String[] args) {
		launchApplication();
	}

	private static void launchApplication() {
		// Set the Desired Capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "Redmi4");
		caps.setCapability("udid", "91e7e4b47d44");// Redmi 4
		//caps.setCapability("udid", "ZY223NRZZJ");// Moto G4
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "7.1.2 N2G47H");// Redmi 4
		//caps.setCapability("platformVersion", "7.0");// Moto G4
		caps.setCapability("appPackage", "com.ebay.mobile");
		caps.setCapability("appActivity", "com.ebay.mobile.activities.MainActivity");
		caps.setCapability("noReset", "true");

		// Instantiate Appium Driver
		try {
			driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		objUtils = new Utils(driver);

		// check if login button is present or not
		boolean isLoggedInBtnPresent = objUtils.isElementPresent("com.ebay.mobile:id/button_sign_in");
	
		if (isLoggedInBtnPresent) {
			// login button is present i.e. user is not logged in
			System.out.println("User is not logged in yet");
			executeLoginFlow();

		} else {
			// login button is not present i.e. user is already logged in
			System.out.println("User already logged in, start search flow");
			executeSearchFlow();

		}
	}

	private static void executeSearchFlow() {

		boolean isSearchPresent = objUtils.isElementPresent("com.ebay.mobile:id/search_box");
		Assert.assertTrue("Failed to locate element", isSearchPresent);
		if (isSearchPresent) {
			// Identify an element using Resource ID (exact match)
			MobileElement loginButton = ((AndroidDriver<MobileElement>) driver)
					.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/search_box\")");
			loginButton.click();

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Find 'Ebay Search' element and set the value
			MobileElement searchEditText = ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator(
					"new UiSelector().resourceId(\"com.ebay.mobile:id/search_src_text\")");
			searchEditText.clear();
			searchEditText.setValue("65-inch tv");

			// pressed search on keyboard
			((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// check if popup is visible or not for saving
			boolean isSavePopupShowing = objUtils.isElementPresent("com.ebay.mobile:id/text_slot_1");
			if (isSavePopupShowing) {
				// click on saveSearchResultPopup to hide save search result popup
				MobileElement saveSearchResultPopup = ((AndroidDriver<MobileElement>) driver)
						.findElementByAndroidUIAutomator(
								"new UiSelector().resourceId(\"com.ebay.mobile:id/text_slot_1\")");
				saveSearchResultPopup.click();
				proceedToCheckout();
			} else {
				proceedToCheckout();
			}

		} else {
			// Search layout is not present
			System.out.println("Unable to find Search textview");
		}
	}

	private static void proceedToCheckout() {

		// Search list screen
		// Find random list item from search list
		driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()"
				+ ".scrollable(true).instance(0)).scrollIntoView(new UiSelector()"
				+ ".textContains(\"SONY KD-65X9300E 165CM (65INCH) 4K HDR LED TV WITH 1 YEAR SONY INDIA WARRANTY\")"
				+ ".instance(0))").click();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// main item screen
		// Find 'buyItButton' element and set the value
		MobileElement buyItButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button_bin\")");
		buyItButton.click();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// quantity screen
		// Find 'Review' element and set the value
		MobileElement reviewButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/take_action\")");
		reviewButton.click();

		long timeoutInSeconds = 30;
		new WebDriverWait(driver, timeoutInSeconds)
				.until(ExpectedConditions.elementToBeClickable(By.id("com.ebay.mobile:id/progress_bar")));

		boolean isAddAddress = objUtils.isElementPresent("com.ebay.mobile:id/sbtBtn");
		if (isAddAddress) {
			// add address details
			addAddressDetails();
			System.out.println("add address details");
		} else {
			// address is already added
			System.out.println("address is already added");

		}

	}

	private static void addAddressDetails() {
		// Find 'Country Spinner' element and set the value
		MobileElement countrySpinner = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/countryId\")");
		countrySpinner.click();

		MobileElement addressEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/address1\")");
		addressEditText.clear();
		addressEditText.setValue("Keshav Nagar, Chinchwad");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement apartmentEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/address2\")");
		apartmentEditText.clear();
		apartmentEditText.setValue("10, Kunal Estate A-8, B Wing");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement pinCodeEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/zip\")");
		pinCodeEditText.clear();
		pinCodeEditText.setValue("411033");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement emailEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/email\")");
		emailEditText.clear();
		emailEditText.setValue("armalsandip@gmail.com");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement mobileNumberEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/phoneFlagComp1\")");
		mobileNumberEditText.clear();
		mobileNumberEditText.setValue("8698579562");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement checkbox = (MobileElement) driver.findElementsByClassName("android.widget.CheckBox");
		checkbox.click();

		MobileElement continueButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/sbtBtn\")");
		continueButton.click();

	}

	private static void executeLoginFlow() {

		// Identify an element using Resource ID (exact match)
		MobileElement loginButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button_sign_in\")");
		loginButton.click();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Find 'Ebay Email' element and set the value
		MobileElement emailEditText = ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator(
				"new UiSelector().resourceId(\"com.ebay.mobile:id/edit_text_username\")");
		emailEditText.clear();
		emailEditText.setValue("armalsandip@gmail.com");

		driver.hideKeyboard();

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		// Find 'Ebay Password Edittext' and set the value
		MobileElement passwordEditText = ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator(
				"new UiSelector().resourceId(\"com.ebay.mobile:id/edit_text_password\")");
		passwordEditText.clear();
		passwordEditText.setValue("Deepali@1993");

		driver.hideKeyboard();

		MobileElement mainLoginButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button_sign_in\")");
		mainLoginButton.click();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// check if finger print enable popup is coming or not
		boolean isPopupcoming = objUtils.isElementPresent("com.ebay.mobile:id/button2");
		if (isPopupcoming) {
			MobileElement mayBeLaterButton = ((AndroidDriver<MobileElement>) driver)
					.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button2\")");
			mayBeLaterButton.click();
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		executeSearchFlow();

	}

}