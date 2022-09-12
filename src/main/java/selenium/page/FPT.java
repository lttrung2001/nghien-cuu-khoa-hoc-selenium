package selenium.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FPT extends TGDD {

	public FPT(ChromeDriver driver) {
		super(driver);
		url = "https://fptshop.com.vn/dien-thoai";
		defaultNumber = 27;
	}

	@Override
	public void connect() {
		try {
			driver.get(url);
			By totalLocate = By.cssSelector(".cdt-head > span");
			String resultString = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLocate)).getText();
			String totalString = resultString.substring(1, resultString.indexOf(" "));
			System.out.println(totalString);
			total = Integer.parseInt(totalString);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String fetchData() {
		By productLocate = By.className("cdt-product");
		List<WebElement> productElements;
		int start = 0;
		int currentShow = 0;
		boolean havingPrice = true;
		do {
			start = currentShow;
			currentShow += total - currentShow > defaultNumber ? defaultNumber : total - currentShow;
			productElements = wait.until(ExpectedConditions.numberOfElementsToBe(productLocate, currentShow));
			System.out.println("currentShow:" + currentShow);
			System.out.println("start:" + start);
			for (int i = start; i < productElements.size(); i++) {
				new Actions(driver).moveToElement(productElements.get(i)).build().perform();
				try {
					productElements.get(i).findElement(By.className("progress"));
					result.add(productElements.get(i).findElement(By.tagName("a")).getAttribute("href"));
				} catch (NoSuchElementException e) {
					try {
						productElements.get(i).findElement(By.className("price"));
						result.add(productElements.get(i).findElement(By.tagName("a")).getAttribute("href"));
					} catch (NoSuchElementException e2) {
						havingPrice = false;
						break;
					}
				}
			}
			if (havingPrice && currentShow < total) {
				By viewMoreLocate = By.cssSelector("div.cdt-product--loadmore > a");
				WebElement viewMoreElement = driver.findElement(viewMoreLocate);
				viewMoreElement.click();
			}
		} while (havingPrice && currentShow < total);
		for (String productUrl : result) {
			driver.get(productUrl);

			List<String> optionUrls = new ArrayList<String>();
			driver.findElements(By.cssSelector(".st-select > a")).forEach(element -> {
				optionUrls.add(element.getAttribute("href"));
			});
			for (int i = 1; i < optionUrls.size(); i++) {
				driver.get(optionUrls.get(i));
				WebElement showDetailElement = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".l-pd-left")));
				driver.close();
			}
		}
		return result.toString();
	}

}
