package selenium.page;

import java.util.ArrayList;


import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;

public class FPT extends TGDD {
	private static final String selector = "div.checkbox.frowitem>a";
	private static final int brandPosition = 0;
	private static final int pricePosition = 1;
	private static final int featurePosition = 2;
	private static final int displayPosition = 4;
	private PhoneConfiguration phone;
	private List<WebElement> filters;
	private List<WebElement> brandElements;
	@SuppressWarnings("unused")
	private List<WebElement> priceElements;
	private List<WebElement> featureElements;
	private List<WebElement> displayElements;
	private List<Result> results = new ArrayList<Result>();

	public FPT(ChromeDriver driver, PhoneConfiguration phone) {
		super(driver);
		super.url = "https://fptshop.com.vn/dien-thoai";
		super.baseUrl = "https://fptshop.com.vn/";
		this.phone = phone;
	}

	@Override
	public void getFilterElements() {
		filters = wait.until(
				ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".cdt-filter__block"), displayPosition));
		brandElements = filters.get(brandPosition).findElements(By.cssSelector(selector));
		priceElements = filters.get(pricePosition).findElements(By.cssSelector(selector));
		featureElements = filters.get(featurePosition).findElements(By.cssSelector(selector));
		displayElements = filters.get(displayPosition).findElements(By.cssSelector(selector));
	}

	@Override
	public void config() {
		FilterList ft = new FilterList();
		// Feature
		try {
			js.executeScript(String.format("arguments[0].title = '%s';", ft.getSpecialFeatures()[0]),
					featureElements.get(2));
			js.executeScript(String.format("arguments[0].title = '%s';", ft.getSpecialFeatures()[2]),
					featureElements.get(3));
			// Display
			js.executeScript(String.format("arguments[0].title = '%s';", ft.getDisplaySize()[0]),
					displayElements.get(1));
			js.executeScript(String.format("arguments[0].title = '%s';", ft.getDisplaySize()[0]),
					displayElements.get(2));
			js.executeScript(String.format("arguments[0].title = '%s';", ft.getDisplaySize()[1]),
					displayElements.get(3));
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Bộ lọc trên trang " + url + " đã thay đổi");
		}
	}

	@Override
	public void filterBrand(String[] strings) {
		WebElement e = null;
		for (int i = 0; i < brandElements.size(); i++) {
			e = brandElements.get(i);
			for (String string : strings) {
				if (e.getAttribute("title").toLowerCase().contains(string)) {
					driver.get(e.getAttribute("href"));
					getFilterElements();
					config();
					break;
				}
			}
		}
	}

	public void filterOther(String[] strings, int index) {
		WebElement e = null;
		List<WebElement> elements = filters.get(index).findElements(By.cssSelector(selector));
		for (int i = 0; i < elements.size(); i++) {
			e = elements.get(i);
			for (String string : strings) {
				if (e.getAttribute("title").equals(string)) {
					driver.get(e.getAttribute("href"));
					getFilterElements();
					config();
					elements = filters.get(index).findElements(By.cssSelector(selector));
					break;
				}
			}
		}
	}

	@Override
	public void filterAll(PhoneConfiguration phone) {
		if (phone.getBrand() != null)
			filterBrand(phone.getBrand());

		if (phone.getPriceRange() != null)
			filterOther(phone.getPriceRange(), pricePosition);

		if (phone.getSpecialFeature() != null)
			filterOther(phone.getSpecialFeature(), featurePosition);

		if (phone.getDisplaySize() != null)
			filterOther(phone.getDisplaySize(), displayPosition);

		try {
			driver.get(driver.getCurrentUrl().concat("&trang=" + Integer.MAX_VALUE));
		} catch (TimeoutException e) {
			throw new TimeoutException("Trang web phản hồi quá lâu");
		}
	}

	@Override
	public String run(PhoneConfiguration phone, List<Result> allResults) {
		try {
			connect(url);
			getFilterElements();
			config();
			filterAll(phone);
			allResults.addAll(getResults(false));
			return "";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@Override
	public void collectProduct(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		String img = element.findElement(By.tagName("img")).getAttribute("src");
		String name = element.findElement(By.cssSelector(".cdt-product__name")).getText();
		String price = "";
		try {
			price = element.findElement(By.cssSelector(".progress")).getText();
		} catch (Exception progressException) {
			try {
				price = element.findElement(By.cssSelector(".price")).getText();
			} catch (Exception priceException) {
				price = "Không có thông tin";
			}
		}
		String url = element.findElement(By.cssSelector(".cdt-product__name")).getAttribute("href");
		// Sai o day
		List<String> details = new ArrayList<String>();
		List<WebElement> list = element.findElements(By.cssSelector(".cdt-product__config__param"));
		if (!list.isEmpty()) {
			WebElement configElement = list.get(0);
			List<WebElement> detailElements = configElement.findElements(By.cssSelector("span"));
			if (detailElements.size() != 0) {
				for (WebElement e : detailElements) {
					if (isRightProduct(phone, e, name)) {
						details.add(e.getAttribute("data-title") + ": " + e.getAttribute("textContent"));
					} else {
						return;
					}
				}
				results.add(new Result(img, name, price, url, details));
			}
		}
	}

	public boolean isRightProduct(PhoneConfiguration phone, WebElement e, String productName) {
		FilterList ft = new FilterList();
		if (phone.getRam() == null) {
			phone.setRam(new String[] {});
		}
		if (phone.getRom() == null) {
			phone.setRom(new String[] {});
		}
		if (phone.getSpecialFeature() == null) {
			phone.setSpecialFeature(new String[] {});
		}
		List<String> RAMs = Arrays.asList(phone.getRam());
		List<String> ROMs = Arrays.asList(phone.getRom());
		List<String> SFs = Arrays.asList(phone.getSpecialFeature());
		boolean res = true;
		if (!RAMs.isEmpty() && e.getAttribute("data-title").equals("RAM")
				&& !RAMs.contains(e.getAttribute("textContent"))) {
			res = false;
		}
		if (!ROMs.isEmpty() && e.getAttribute("data-title").equals("Bộ nhớ trong")
				&& !ROMs.contains(e.getAttribute("textContent"))) {
			res = false;
		}
		try {
			if (!SFs.isEmpty() && SFs.contains(ft.getSpecialFeatures()[1]) && !productName.contains(" 5G ")) {
				res = false;
			}
		} catch (IndexOutOfBoundsException ex) {
			throw new IndexOutOfBoundsException("Bộ lọc của trang web tìm kiếm đã bị thay đổi");
		}
		return res;
	}

	@Override
	public List<Result> getResults(boolean isSearch) {
		if (isSearch) {
			totalProduct = Integer.parseInt(driver.findElements(By.cssSelector(".re-card h1 > span")).get(0).getText());
		} else {
			String total = "";
			try {
				total = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdt-head>span")))
						.getText();
				totalProduct = Integer.parseInt(total.split(" ")[0].replace("(", ""));
			} catch (TimeoutException e) {
				throw new TimeoutException("Trang web phản hồi quá lâu");
			}
		}
		if (totalProduct > 0) {
			List<WebElement> resultElements = wait
					.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".cdt-product"), totalProduct));
			List<WebElement> optionElements;
			for (int i = 0; i < resultElements.size(); i++) {
				optionElements = resultElements.get(i).findElements(By.cssSelector(".mmr-box.item1"));
				if (optionElements.size() != 0) {
					for (int j = 0; j < optionElements.size(); j++) {
						tryToClickOption(resultElements, optionElements, i, j);
						refreshItemAfterClickAndGetData(resultElements, i);
					}
				} else {
					collectProduct(resultElements.get(i));
				}
			}
		}
		return results;
	}

	@Override
	public void refreshItemAfterClickAndGetData(List<WebElement> resultElements, int i) {
		while (true) {
			try {
				resultElements = wait
						.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".cdt-product"), totalProduct));
				collectProduct(resultElements.get(i));
				break;
			} catch (Exception e) {
				System.out.println("refreshItemAfterClickAndGetData");
			}
		}
	}

	@Override
	public void tryToClickOption(List<WebElement> resultElements, List<WebElement> optionElements, int i, int j) {
		while (true) {
			try {
				resultElements = wait
						.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".cdt-product"), totalProduct));
				optionElements = resultElements.get(i).findElements(By.cssSelector(".mmr-box.item1"));
				optionElements.get(j).click();
				break;
			} catch (Exception e) {
				System.out.println("tryToClickOption");
			}
		}
	}

	@Override
	public String search(String key, List<Result> allResults) {
		try {
			connect(baseUrl + "tim-kiem/" + key + "?Loại%20Sản%20Phẩm=Điện%20thoại");
			allResults.addAll(getResults(true));
			return "";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}