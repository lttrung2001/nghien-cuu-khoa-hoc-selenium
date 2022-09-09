package selenium.page;

import java.util.List;

import org.openqa.selenium.By;
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
		By optionLocate = By.cssSelector(".mmr-box.item1");
		List<WebElement> productElements;
		List<WebElement> optionElements;
		int start = 0;
		int currentShow = 0;
		do {
			start = currentShow;
			currentShow += total - currentShow > defaultNumber ? defaultNumber : total - currentShow;
			productElements = wait
					.until(ExpectedConditions.numberOfElementsToBe(productLocate, currentShow));
			System.out.println("currentShow:"+currentShow);
			System.out.println("start:"+start);
			for (int i = start; i < productElements.size(); i++) {
				new Actions(driver).moveToElement(productElements.get(i)).build().perform();
				optionElements = productElements.get(i).findElements(optionLocate);
				if (optionElements.isEmpty()) {
					result.add(productElements.get(i).findElement(By.tagName("a")).getAttribute("href"));
				} else {
					for (int j = 0; j < optionElements.size(); j++) {
						result.add(productElements.get(i).findElement(By.tagName("a")).getAttribute("href"));
						optionElements.get(j).click();
						productElements = wait
								.until(ExpectedConditions.numberOfElementsToBe(productLocate, currentShow));
						optionElements = productElements.get(i).findElements(optionLocate);
					}
				}
			}
			if (currentShow < total) {
				By viewMoreLocate = By.cssSelector("div.cdt-product--loadmore > a");
				WebElement viewMoreElement = driver.findElement(viewMoreLocate);
				viewMoreElement.click();
			}
		} while (currentShow < total);
//		for (String url : result) {
//			driver.get(url);
//			try {
//				driver.findElement(By.cssSelector(".btn-detail.btn-short-spec")).click();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
		return result.toString();
	}
	
}
