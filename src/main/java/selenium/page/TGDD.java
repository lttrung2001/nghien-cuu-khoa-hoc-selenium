package selenium.page;

import java.time.Duration;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;

public class TGDD {
	public String url = "https://www.thegioididong.com/dtdd";
	public static int defaultNumber = 20;
	public ChromeDriver driver;
	public JavascriptExecutor js;
	public WebDriverWait wait;

	public int totalProduct;
	private WebElement showFilterButton;
	private WebElement listFilterActive;
	private WebElement filterTable;
	private List<WebElement> brandList;
	private List<WebElement> priceList;
	private List<WebElement> ramList;
	private List<WebElement> romList;
	private List<WebElement> featureList;
	private List<WebElement> displaySizeList;
	private List<Result> results = new ArrayList<Result>();

	public TGDD(ChromeDriver driver) {
		this.driver = driver;
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		totalProduct = 0;
	}

	public String run(PhoneConfiguration phone, List<Result> allResults) {
		try {
			connect();
			getFilterElements();
			config();
			filterAll(phone);
			allResults.addAll(getResults());
			return "";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}

	public void getFilterElements() {
		// Get first button (to open filter table)
		showFilterButton = driver.findElement(By.className("filter-item__title"));
		showFilterButton.click();
		filterTable = driver.findElement(By.id("wrapper")); // Chon container bo loc
		listFilterActive = driver.findElement(By.className("list-filter-active"));
		brandList = filterTable.findElements(By.cssSelector(".manu>.c-btnbox"));
		priceList = filterTable.findElements(By.cssSelector(".price>.c-btnbox"));
		ramList = filterTable.findElements(By.cssSelector(".filter-list--ram>.c-btnbox"));
		romList = filterTable.findElements(By.cssSelector(".filter-list--bo-nho-trong>.c-btnbox"));
		featureList = filterTable.findElements(By.cssSelector(".filter-list--tinh-nang-dac-biet>.c-btnbox"));
		displaySizeList = filterTable.findElements(By.cssSelector(".filter-list--man-hinh>.c-btnbox"));
	}

	public void config() {
		FilterList ft = new FilterList();
		try {
			js.executeScript("arguments[0].remove();", listFilterActive); // disable active filters
			// Price
			js.executeScript(String.format("arguments[0].innerText = '%s';", ft.getPriceRanges()[4]), priceList.get(4));
			js.executeScript(String.format("arguments[0].innerText = '%s';", ft.getPriceRanges()[4]), priceList.get(5));
			// Feature
			js.executeScript(String.format("arguments[0].innerText = '%s';", ft.getSpecialFeatures()[2]),
					featureList.get(0));
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Bộ lọc trên trang " + url + " đã thay đổi");
		}
	}

	public void connect() {
		try {
			driver.get(url);
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu");
		}
	}

	public void scrollElement(WebElement elementScrolled, WebElement element) {
		int height = element.findElement(By.xpath("./..")).getSize().getHeight();
		js.executeScript(String.format("arguments[0].scrollTop = %d;", height), elementScrolled);
	}

	public void collectProduct(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		List<WebElement> detailElements = element.findElements(By.cssSelector(".utility>p"));
		List<String> productDetails = new ArrayList<String>();

		String img = element.findElement(By.cssSelector(".item-img.item-img_42>img")).getAttribute("src");
		String name = element.findElement(By.cssSelector("a.main-contain")).getAttribute("data-name");
		String productLink = element.findElement(By.className("main-contain")).getAttribute("href");
		String price = "";
		try {
			price = element.findElement(By.cssSelector("strong.price")).getText();
		} catch (NoSuchElementException e) {
			price = "Chưa có";
		}
		if (detailElements.size() != 0) {
			for (WebElement detailElement : detailElements)
				productDetails.add(detailElement.getText());
		}
		results.add(new Result(img, name, price, productLink, productDetails));
	}

	public void filterBrand(String[] strings) {
		if (strings != null) {
			for (String str : strings) {
				for (WebElement e : brandList) {
					if (e.getAttribute("data-name").toLowerCase().contains(str)) {
						wait.until(ExpectedConditions.elementToBeClickable(e));
						e.click();
						break;
					}
				}
			}
		}
	}

	public void filterOther(String[] strings, List<WebElement> elements) {
		if (strings != null) {
			for (WebElement e : elements) {
				for (String str : strings) {
					if (e.getText().contains(str)) {
						wait.until(ExpectedConditions.elementToBeClickable(e));
						e.click();
						break;
					}
				}
			}
		}
	}

	public void filterAll(PhoneConfiguration phone) {
		List<WebElement> list = filterTable.findElements(By.cssSelector(".show-total-item"));
		filterBrand(phone.getBrand());
		scrollElement(filterTable, list.get(0));

		filterOther(phone.getPriceRange(), priceList);
		scrollElement(filterTable, list.get(1));

		filterOther(phone.getRam(), ramList);
		filterOther(phone.getRom(), romList);
		scrollElement(filterTable, list.get(4));
		
		filterOther(phone.getDisplaySize(), displaySizeList);
		
		filterOther(phone.getSpecialFeature(), featureList);

		getTotalNumber();
		loadAllProduct();
	}

	public void getTotalNumber() {
		By by = By.cssSelector(".total-reloading");
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".btn-filter-readmore.prevent")));
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(by, "...")));
			totalProduct = Integer.parseInt(driver.findElement(by).getText());
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Tìm kiếm phần tử không tồn tại");
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu");
		}
	}

	public void loadAllProduct() {
		if (totalProduct != 0) {
			By by = By.cssSelector("a.btn-filter-readmore");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(by)).click();
			} catch (ElementClickInterceptedException e) {
				throw new ElementClickInterceptedException("Không thể thao tác với phần tử trên trang web để hiển thị danh sách kết quả tìm kiếm");
			} catch (TimeoutException e) {
				throw new TimeoutException("Trang web phản hồi quá lâu");
			}
			if (totalProduct > defaultNumber) {
				int tmp = defaultNumber;
				by = By.cssSelector("div.view-more>a");
				while (tmp < totalProduct) {
					try {
						WebElement preloader = driver.findElement(By.cssSelector("#preloader"));
						wait.until(ExpectedConditions.attributeToBe(preloader, "style", "display: none;"));
						wait.until(ExpectedConditions.visibilityOfElementLocated(by));
						wait.until(ExpectedConditions.elementToBeClickable(by)).click();
						tmp += totalProduct - tmp > 20 ? 20 : totalProduct - tmp;
						wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".item.ajaxed.__cate_42"), tmp));
					} catch (ElementClickInterceptedException e) {
						throw new ElementClickInterceptedException("Không thể thao tác với phần tử trên trang web để tải thêm kết quả");
					} catch (TimeoutException e) {
						throw new TimeoutException("Trang web phản hồi quá lâu");
					}
				}
			}
		}
	}

	public List<WebElement> checkEnoughProducts() {
		List<WebElement> resultElements = null;
		try {
			resultElements = wait
					.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".item.ajaxed.__cate_42"), totalProduct));
			return resultElements;
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu");
		}
	}

	public void refreshItemAfterClickAndGetData(List<WebElement> resultElements, int i) {
		try {
			resultElements = wait.until(ExpectedConditions
					.numberOfElementsToBe(By.cssSelector(".item.ajaxed.__cate_42"), totalProduct));
			collectProduct(resultElements.get(i));
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu");
		}
	}

	public void tryToClickOption(List<WebElement> resultElements, List<WebElement> optionElements, int i, int j) {
		try {
			resultElements = wait.until(ExpectedConditions
					.numberOfElementsToBe(By.cssSelector(".item.ajaxed.__cate_42"), totalProduct));
			optionElements = resultElements.get(i).findElements(By.cssSelector(".merge__item.item"));
			optionElements.get(j).click();
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu");
		}
	}

	public List<Result> getResults() {
		List<WebElement> resultElements = null; // Danh sach san pham thu duoc
		List<WebElement> optionElements = null; // Cac nut option cua 1 san pham
		// Kiem tra load du san pham
		resultElements = checkEnoughProducts();
		// Get each product
		for (int i = 0; i < resultElements.size(); i++) {
			optionElements = resultElements.get(i).findElements(By.cssSelector(".merge__item.item"));
			// Select each option of product
			if (optionElements.size() != 0) {
				// Get option and click
				for (int j = 0; j < optionElements.size(); j++) {
					// Try to click option
					tryToClickOption(resultElements, optionElements, i, j);
					// Refresh item after click and get data
					refreshItemAfterClickAndGetData(resultElements, i);
				}
			} else
				collectProduct(resultElements.get(i));
		}
		return results;
	}
}
