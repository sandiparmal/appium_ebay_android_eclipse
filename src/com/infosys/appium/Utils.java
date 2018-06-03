package com.infosys.appium;

import java.time.Duration;

import org.openqa.selenium.By;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class Utils {

	private AndroidDriver<MobileElement> driver = null;

	public Utils(AndroidDriver<MobileElement> driver) {
		this.driver = driver;
	}

	public boolean isElementPresent(String element) {
		if (!driver.findElements(By.id(element)).isEmpty()) {
			return true;
		}
		return false;
	}

	public void swipeVertical(double startPercentage, double finalPercentage, double anchorPercentage, int duration)
			throws Exception {

		org.openqa.selenium.Dimension size = driver.manage().window().getSize();
		int anchor = (int) (size.width * anchorPercentage);
		int startPoint = (int) (size.height * startPercentage);
		int endPoint = (int) (size.height * finalPercentage);
		new TouchAction(driver).press(anchor, startPoint).waitAction(Duration.ofMillis(duration))
				.moveTo(anchor, endPoint).release().perform();

	}

	public MobileElement getElement(String id) {
		return ((AndroidDriver<MobileElement>) driver)
				.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.ebay.mobile:id/" + id + "\")");
	}

	public void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
