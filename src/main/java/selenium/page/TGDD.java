package selenium.page;

import java.io.IOException;




import java.time.Duration;





import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;

public class TGDD {
	public String url = "https://www.thegioididong.com/dtdd";
	public String baseUrl = "https://www.thegioididong.com/";
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		totalProduct = 0;
	}

	public String run(PhoneConfiguration phone, List<Result> allResults) {
		try {
			connect(url);
			getFilterElements();
			config();
			filterAll(phone);
			{
				getTotalNumber();
				if (totalProduct == 0) return "";
			}
			loadAllProduct();
			allResults.addAll(getResults(false));
			return "";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}
	
	public String search(String key, List<Result> allResults) {
		try {
			connect(baseUrl+"tim-kiem?key="+key);
			showSearchList();
			// Warning: Handle getSearchProducts twice
			if (getSearchProducts().size() == 0) {
				return "";
			}
			allResults.addAll(getResults(true));
			return "";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}
	
	public void showSearchList() {
		try {
			WebElement menu = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".searchCategoryResult")));
			List<WebElement> list = menu.findElements(By.tagName("a"));
			for (WebElement webElement : list) {
				if (webElement.getText().contains("Điện thoại")) {
					totalProduct = Integer.parseInt(webElement.getAttribute("data-total"));
					System.out.println("Total search: "+totalProduct);
					webElement.click();
					break;
				}
			}
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Không tìm thấy kết quả phù hợp");
		} catch (ElementClickInterceptedException e) {
			throw new ElementClickInterceptedException("Không thể tương tác với phần tử trên trang");
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu showSearchList");
		}
	}
	
	public List<WebElement> getSearchProducts() {
		List<WebElement> resultElements = null;
		try {
			resultElements = wait
					.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".item.cat42"), totalProduct));
			return resultElements;
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu getSearchProducts");
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

	public void connect(String url) {
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
	
	public WebElement getParentElement(WebElement child) {
		return child.findElement(By.xpath("./.."));
	}

	public void collectProduct(WebElement element) throws IOException {
		new Actions(driver).moveToElement(element).build().perform();
//		List<WebElement> detailElements = element.findElements(By.cssSelector(".utility>p"));
		List<String> productDetails = new ArrayList<String>();

		String img = element.findElement(By.cssSelector(".item-img.item-img_42>img")).getAttribute("src");
		String name = element.findElement(By.cssSelector("a.main-contain")).getAttribute("data-name");
		String productLink = element.findElement(By.className("main-contain")).getAttribute("href");
		String price = "";
		try {
			price = element.findElement(By.cssSelector("strong.price")).getText();
		} catch (NoSuchElementException e) {
			price = "Không có thông tin";
		}
//		if (detailElements.size() != 0) {
//			for (WebElement detailElement : detailElements) {
//				productDetails.add(detailElement.getText());				
//			}
//		} else {
			Request detailRequest = new Request.Builder()
					.url(productLink)
					.build();
			Response detailResponse = new OkHttpClient().newCall(detailRequest).execute();
			if (detailResponse.code() == 200) {
				Document document = Jsoup.parse(detailResponse.body().string());
				List<Element> lilefts = document.getElementsByClass("lileft");
				List<Element> lirights = document.getElementsByClass("liright");
				for (int i = 0; i < lilefts.size(); i++) {
					productDetails.add(lilefts.get(i).text()+lirights.get(i).text());
				}
			}
//		}
		results.add(new Result(img, name, price, productLink, productDetails));
	}

	public void filterBrand(String[] strings) {
		if (strings != null) {
			for (String str : strings) {
				for (WebElement e : brandList) {
					if (e.getAttribute("data-name").toLowerCase().contains(str)) {
						wait.until(ExpectedConditions.elementToBeClickable(e));
						js.executeScript("arguments[0].click();", e);
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
						js.executeScript("arguments[0].click();", e);
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
	}

	public void getTotalNumber() {
		By by = By.cssSelector(".total-reloading");
		try {
			// Bug here
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(by, "...")));
			totalProduct = Integer.parseInt(driver.findElement(by).getText());
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Tìm kiếm phần tử không tồn tại");
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu getTotalNumber");
		}
	}

	public void loadAllProduct() {
		if (totalProduct > 0) {
			By by = By.cssSelector("a.btn-filter-readmore");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(by)).click();
			} catch (ElementClickInterceptedException e) {
				throw new ElementClickInterceptedException("Không thể thao tác với phần tử trên trang web để hiển thị danh sách kết quả tìm kiếm");
			} catch (TimeoutException e) {
				throw new TimeoutException("Trang web phản hồi quá lâu loadAllProduct 1");
			}
			if (totalProduct > defaultNumber) {
				WebElement preloader = driver.findElement(By.cssSelector("#preloader"));
				by = By.cssSelector("div.view-more>a");
				int tmp = defaultNumber;
				try {
					wait.until(ExpectedConditions.attributeToBe(preloader, "style", "display: none;"));
					while (tmp < totalProduct) {						
						wait.until(ExpectedConditions.visibilityOfElementLocated(by));
						wait.until(ExpectedConditions.elementToBeClickable(by));
						js.executeScript("arguments[0].click();", driver.findElement(by));
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("bubblingG")));
						tmp += totalProduct - tmp > 20 ? 20 : totalProduct - tmp;
						wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".item.ajaxed.__cate_42"), tmp));
					}
				} catch (ElementClickInterceptedException e) {
					throw new ElementClickInterceptedException("Không thể thao tác với phần tử trên trang web để tải thêm kết quả");
				} catch (TimeoutException e) {
					throw new TimeoutException("Trang web phản hồi quá lâu loadAllProduct 2");
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
			throw new TimeoutException("Trang web phản hồi quá lâu checkEnoughProducts");
		}
	}

	public void refreshItemAfterClickAndGetData(List<WebElement> resultElements, int i) throws IOException {
		try {			
			By by = By.cssSelector(".item.ajaxed.__cate_42.loading-border");
			wait.until(ExpectedConditions.numberOfElementsToBe(by, 0));
			by = By.cssSelector(".item.ajaxed.__cate_42");
			resultElements = wait.until(ExpectedConditions.numberOfElementsToBe(by, totalProduct));
			collectProduct(resultElements.get(i));
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu refreshItemAfterClickAndGetData");
		} catch (StaleElementReferenceException e) {
			throw new StaleElementReferenceException("Tham chiếu phần tử cũ");
		}
	}

	public void tryToClickOption(List<WebElement> resultElements, List<WebElement> optionElements, int i, int j) {
		try {
			resultElements = wait.until(ExpectedConditions
					.numberOfElementsToBe(By.cssSelector(".item.ajaxed.__cate_42"), totalProduct));
			optionElements = resultElements.get(i).findElements(By.cssSelector(".merge__item.item"));
			wait.until(ExpectedConditions.elementToBeClickable(optionElements.get(j))).click();
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu tryToClickOption");
		} catch (ElementClickInterceptedException e) {
			tryToClickOption(resultElements, optionElements, i, j);
		}
	}

	public List<Result> getResults(boolean isSearch) throws IOException {
		List<WebElement> resultElements = null; // Danh sach san pham thu duoc
		List<WebElement> optionElements = null; // Cac nut option cua 1 san pham
		if (isSearch) {
			resultElements = getSearchProducts();
		} else {
			// Kiem tra load du san pham
			resultElements = checkEnoughProducts();
		}
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
