package selenium.page;

import java.time.Duration;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TGDD {
	private ChromeDriver driver;
	private WebDriverWait wait;
	public String url = "https://www.thegioididong.com/dtdd";
	public int defaultNumber = 20;

	private int total;
	private List<String> result = new ArrayList<String>();

	public TGDD(ChromeDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get(url);
		By totalLocate = By.cssSelector("div.box-sort > p.sort-total > b");
		String totalString = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLocate)).getText();
		total = Integer.parseInt(totalString);
	}

	public String fetchData() {
		By productLocate = By.cssSelector("ul.listproduct > li");
		By optionLocate = By.cssSelector("div.prods-group li");
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
						result.add("https://www.thegioididong.com"+optionElements.get(j).getAttribute("data-url"));
						productElements = wait
								.until(ExpectedConditions.numberOfElementsToBe(productLocate, currentShow));
					}
				}
			}
			if (currentShow < total) {
				By viewMoreLocate = By.cssSelector("div.view-more > a");
				WebElement viewMoreElement = driver.findElement(viewMoreLocate);
				viewMoreElement.click();
			}
		} while (currentShow < total);
		for (String url : result) {
			driver.get(url);
			try {
				driver.findElement(By.cssSelector(".btn-detail.btn-short-spec")).click();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result.toString();
	}
}
