package com.infosys.appium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

		objUtils = new Utils(driver);
		objUtils.sleep(10000);  
		
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
		Assert.assertTrue("Failed to locate Search TextView", isSearchPresent);
		
		if (isSearchPresent) {
			// Identify an element using Resource ID (exact match)
			MobileElement searchBox = ((AndroidDriver<MobileElement>) driver)
					.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/search_box\")");
			searchBox.click();

			objUtils.sleep(10000);

			boolean isSearchMainPresent = objUtils.isElementPresent("com.ebay.mobile:id/search_src_text");
			Assert.assertTrue("Failed to locate Search EditText", isSearchMainPresent);
			
			// Find 'Ebay Search' element and set the value
			MobileElement searchEditText = ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator(
					"new UiSelector().resourceId(\"com.ebay.mobile:id/search_src_text\")");
			searchEditText.clear();
			searchEditText.setValue("65-inch tv");

			// pressed search on keyboard
			((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

			objUtils.sleep(10000);

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
		WebElement listItem = driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()"
				+ ".scrollable(true).instance(0)).scrollIntoView(new UiSelector()"
				+ ".textContains(\"65 inch 4K UHD ANDROID SMART SAMSUNG PANEL 8GB LED TV +1YR RREPLACEMENT\")"
				+ ".instance(0))");
		listItem.click();
		
		String clickedItemText = listItem.getAttribute("text");
		
		System.out.println("clicked item text : "+clickedItemText);

		objUtils.sleep(10000);

		boolean isBuyButtonPresent = objUtils.isElementPresent("com.ebay.mobile:id/button_bin");
		Assert.assertTrue("Failed to locate Buy Button", isBuyButtonPresent);
		// main item screen
		// Find 'buyItButton' element and set the value
		MobileElement buyItButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button_bin\")");
		buyItButton.click();

		objUtils.sleep(10000);

		// quantity screen
		//is product tile is present or not
		boolean isTitlePresentInCart = objUtils.isElementPresent("com.ebay.mobile:id/item_title");
		Assert.assertTrue("Failed to locate Title TextView", isTitlePresentInCart);
		
		MobileElement titleTextView = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/item_title\")");
		String cartProductText = titleTextView.getAttribute("text");
		
		// compare it clicked item text is equal to cart product text
		assertEquals(clickedItemText, cartProductText);
		
		boolean isReviewButtonPresent = objUtils.isElementPresent("com.ebay.mobile:id/take_action");
		Assert.assertTrue("Failed to locate Review Button", isReviewButtonPresent);
		
		// Find 'Review' element and set the value
		MobileElement reviewButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/take_action\")");
		reviewButton.click();

		objUtils.sleep(10000);
		
		boolean isAddAddress = objUtils.isElementPresent("com.ebay.mobile:id/sbtBtn");
		if (isAddAddress) {
			// add address details
			addAddressDetails();
			System.out.println("add address details");
		} else {
			// address is already added
			// proceed to checkout
			System.out.println("address is already added, Proceed to checkout");
			System.out.println("WebView Can not be tested");
			
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
		addressEditText.setValue("xyz, abc");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement apartmentEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/address2\")");
		apartmentEditText.clear();
		apartmentEditText.setValue("a, abc, Bcs");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement pinCodeEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/zip\")");
		pinCodeEditText.clear();
		pinCodeEditText.setValue("010110");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement emailEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/email\")");
		emailEditText.clear();
		emailEditText.setValue("armalsandip@gmail.com");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement mobileNumberEditText = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/phoneFlagComp1\")");
		mobileNumberEditText.clear();
		mobileNumberEditText.setValue("1234567890");

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		MobileElement checkbox = (MobileElement) driver.findElementsByClassName("android.widget.CheckBox");
		checkbox.click();

		MobileElement continueButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/sbtBtn\")");
		continueButton.click();
		
		executeSearchFlow();

	}

	private static void executeLoginFlow() {

		boolean isLoginButtonPresent = objUtils.isElementPresent("com.ebay.mobile:id/button_sign_in");
		Assert.assertTrue("Failed to locate Login Button", isLoginButtonPresent);
		
		// Identify an element using Resource ID (exact match)
		MobileElement loginButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button_sign_in\")");
		loginButton.click();

		objUtils.sleep(10000);
		
		boolean isEmailTextPresent = objUtils.isElementPresent("com.ebay.mobile:id/edit_text_username");
		Assert.assertTrue("Failed to locate Email EditText", isEmailTextPresent);
		
		// Find 'Ebay Email' element and set the value
		MobileElement emailEditText = ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator(
				"new UiSelector().resourceId(\"com.ebay.mobile:id/edit_text_username\")");
		emailEditText.clear();
		emailEditText.setValue("ABC@gmail.com");

		driver.hideKeyboard();

		((AndroidDriver<MobileElement>) driver).pressKeyCode(66);

		boolean isPassTextPresent = objUtils.isElementPresent("com.ebay.mobile:id/edit_text_password");
		Assert.assertTrue("Failed to locate Password EditText", isPassTextPresent);
		
		// Find 'Ebay Password Edittext' and set the value
		MobileElement passwordEditText = ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator(
				"new UiSelector().resourceId(\"com.ebay.mobile:id/edit_text_password\")");
		passwordEditText.clear();
		passwordEditText.setValue("XYZ@ABC");

		driver.hideKeyboard();

		boolean isMainLoginButton = objUtils.isElementPresent("com.ebay.mobile:id/button_sign_in");
		Assert.assertTrue("Failed to locate Password Login Button", isMainLoginButton);
		
		MobileElement mainLoginButton = ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button_sign_in\")");
		mainLoginButton.click();

		objUtils.sleep(10000);

		// check if finger print enable popup is coming or not
		boolean isPopupcoming = objUtils.isElementPresent("com.ebay.mobile:id/button2");
		if (isPopupcoming) {
			MobileElement mayBeLaterButton = ((AndroidDriver<MobileElement>) driver)
					.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/button2\")");
			mayBeLaterButton.click();
		}

		objUtils.sleep(10000);

		executeSearchFlow();

	}

}