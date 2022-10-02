package selenium.page;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TGDD {
    private static final By productLocator = By.cssSelector(".item.ajaxed.__cate_42");
    private static final By optionLocator = By.cssSelector(".merge__item.item");
    private final List<Result> results = new ArrayList<>();
    public String baseUrl = "https://www.thegioididong.com/";
    public String url = baseUrl + "dtdd";
    public int defaultNumber = 20;
    public int defaultSearchNumber = 15;
    public ChromeDriver driver;
    public JavascriptExecutor js;
    public WebDriverWait wait;
    public int totalProduct;
    private WebElement listFilterActive;
    private WebElement filterTable;
    private List<WebElement> brandList;
    private List<WebElement> priceList;
    private List<WebElement> ramList;
    private List<WebElement> romList;
    private List<WebElement> featureList;

    public TGDD(ChromeDriver driver) {
        this.driver = driver;
        js = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        totalProduct = 0;
    }

    public String run(PhoneConfiguration phone, List<Result> allResults) {
        String res = "";
        try {
            connect(url); // Kết nối trang web
            getFilterElements(); // Lấy bộ lọc
            config(); // Cấu hình
            filterAll(phone); // Tiến hành lọc
            {
                getTotalNumber(); // Lấy totalProduct
                if (totalProduct == 0) {
                    driver.quit();
                    return res;
                }
            }
            loadAllProduct(); // Tải tất cả điện thoại
            allResults.addAll(getResults(false, phone)); // Đọc kết quả
        } catch (TimeoutException e) {
            e.printStackTrace();
            res = "Lỗi Timeout";
        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage();
        }
        driver.quit();
        return res;
    }

    public String search(PhoneConfiguration phone, String key, List<Result> allResults) {
        String res = "";
        try {
            connect(baseUrl + "tim-kiem?key=" + key);
            showSearchList();
            if (getSearchProducts().size() == 0) {
                driver.quit();
                return res;
            }
            allResults.addAll(getResults(true, phone));
        } catch (TimeoutException e) {
            e.printStackTrace();
            res = "Lỗi Timeout";
        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage();
        }
        driver.quit();
        return res;
    }

    public void showSearchList() throws NumberFormatException, ElementClickInterceptedException {
        try {
            WebElement menu = driver.findElements(By.cssSelector(".searchCategoryResult")).get(0);
            List<WebElement> list = menu.findElements(By.tagName("a"));
            for (WebElement webElement : list) {
                if (webElement.getText().contains("Điện thoại")) {
                    totalProduct = Integer.parseInt(webElement.getAttribute("data-total"));
                    webElement.click();
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            WebElement sortTotalElement = driver.findElement(By.cssSelector(".sort-total > b"));
            totalProduct = Integer.parseInt(sortTotalElement.getText());
            System.out.println("totalProduct in catch: " + totalProduct);
        }
    }

    public List<WebElement> getSearchProducts() throws TimeoutException {
        By searchProductLocator = By.cssSelector("ul.listproduct > li.item");
        totalProduct = Math.min(totalProduct, defaultSearchNumber);
        return wait
                .until(ExpectedConditions.numberOfElementsToBe(searchProductLocator, totalProduct));
    }

    public void getFilterElements() {
        // Mở bộ lọc
        WebElement showFilterButton = driver.findElement(By.className("filter-item__title"));
        showFilterButton.click();
        filterTable = driver.findElement(By.id("wrapper")); // Chon container bo loc
        listFilterActive = driver.findElement(By.className("list-filter-active"));
        brandList = filterTable.findElements(By.cssSelector(".manu>.c-btnbox"));
        priceList = filterTable.findElements(By.cssSelector(".price>.c-btnbox"));
        ramList = filterTable.findElements(By.cssSelector(".filter-list--ram>.c-btnbox"));
        romList = filterTable.findElements(By.cssSelector(".filter-list--dung-luong-luu-tru >.c-btnbox"));
        featureList = filterTable.findElements(By.cssSelector(".filter-list--tinh-nang-dac-biet>.c-btnbox"));
    }

    public void config() {
        // Khai báo bộ lọc do ta custom để đồng bộ với trang web
        FilterList ft = new FilterList();
        try {
            js.executeScript("arguments[0].remove();", listFilterActive); // disable active filters
            // Giá tiền
            js.executeScript(String.format("arguments[0].innerText = '%s';", ft.getPriceRanges()[4]), priceList.get(4));
            js.executeScript(String.format("arguments[0].innerText = '%s';", ft.getPriceRanges()[4]), priceList.get(5));
            // Tính năng
            js.executeScript(String.format("arguments[0].innerText = '%s';", ft.getSpecialFeatures()[2]),
                    featureList.get(0));
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Bộ lọc trên trang " + url + " đã thay đổi");
        }
    }

    public void connect(String url) throws TimeoutException {
        driver.get(url);
    }

    public void scrollElement(WebElement elementScrolled, WebElement element) throws NoSuchElementException {
        int height = element.findElement(By.xpath("./..")).getSize().getHeight();
        js.executeScript(String.format("arguments[0].scrollTop = %d;", height), elementScrolled);
    }

    public void collectProduct(WebElement element, PhoneConfiguration phone) throws IOException, TimeoutException {
        new Actions(driver).moveToElement(element).build().perform();
        List<String> productDetails = new ArrayList<>();

        /* Locator */
        By imgLocator = By.cssSelector(".item-img.item-img_42>img");
        By anchorLocator = By.cssSelector("a.main-contain");

        wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(imgLocator, "src", "")));
        String img = element.findElement(imgLocator).getAttribute("src");

        String name = element.findElement(anchorLocator).getAttribute("data-name");

        String productLink = element.findElement(anchorLocator).getAttribute("href");

        String price;
        try {
            price = element.findElement(By.cssSelector("strong.price")).getText();
        } catch (NoSuchElementException e) {
            price = "Không có thông tin";
        }
        Request detailRequest = new Request.Builder()
                .url(productLink)
                .build();
        Response detailResponse = new OkHttpClient().newCall(detailRequest).execute();
        if (detailResponse.code() == 200) {
            Document document = Jsoup.parse(detailResponse.body().string());
            List<Element> liLefts = document.getElementsByClass("lileft");
            List<Element> liRights = document.getElementsByClass("liright");
            if (liLefts.isEmpty()) {
                element.findElement(By.className("utility")).findElements(By.tagName("p"))
                        .forEach(propertyElement -> productDetails.add(propertyElement.getText()));
            } else {
                for (int i = 0; i < liLefts.size(); i++) {
                    productDetails.add(liLefts.get(i).text() + liRights.get(i).text());
                }
            }
        }
        results.add(new Result(img, name, price, productLink, productDetails));
    }

    public void filterBrand(String[] strings) throws TimeoutException {
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

    public void filterOther(String[] strings, List<WebElement> elements) throws TimeoutException {
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

    public void filterAll(PhoneConfiguration phone) throws NoSuchElementException {
        List<WebElement> list = filterTable.findElements(By.cssSelector(".show-total-item"));
        filterBrand(phone.getBrand());
        scrollElement(filterTable, list.get(0));


        filterOther(phone.getPriceRange(), priceList);
        scrollElement(filterTable, list.get(1));

        filterOther(phone.getRam(), ramList);
        filterOther(phone.getRom(), romList);
        scrollElement(filterTable, list.get(4));

        filterOther(phone.getSpecialFeature(), featureList);
    }

    public void getTotalNumber() throws TimeoutException {
        By by = By.cssSelector(".total-reloading");
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(by, "...")));
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(by, "")));
            totalProduct = Integer.parseInt(driver.findElement(by).getText());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Tìm kiếm phần tử không tồn tại");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Không thể convert totalProduct vì sai định dạng");
        }
    }

    public void loadAllProduct() throws TimeoutException {
        if (totalProduct > 0) {
            By by = By.cssSelector("a.btn-filter-readmore");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(by)).click();
            } catch (ElementClickInterceptedException e) {
                throw new ElementClickInterceptedException(
                        "Không thể thao tác với phần tử trên trang web để hiển thị danh sách kết quả tìm kiếm"
                );
            }
            if (totalProduct > defaultNumber) {
                WebElement preloaderLocator = driver.findElement(By.cssSelector("#preloader"));
                int tmp = defaultNumber;
                by = By.cssSelector("div.view-more>a");
                try {
                    wait.until(ExpectedConditions
                            .attributeToBe(preloaderLocator, "style", "display: none;"));
                    while (tmp < totalProduct) {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                        wait.until(ExpectedConditions.elementToBeClickable(by));
                        js.executeScript("arguments[0].click();", driver.findElement(by));
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("bubblingG")));
                        tmp += Math.min(totalProduct - tmp, 20);
                        wait.until(ExpectedConditions.numberOfElementsToBe(productLocator, tmp));
                    }
                } catch (ElementClickInterceptedException e) {
                    throw new ElementClickInterceptedException(
                            "Không thể thao tác với phần tử trên trang web để tải thêm kết quả"
                    );
                }
            }
        }
    }

    public List<WebElement> checkEnoughProducts() throws TimeoutException {
        return wait
                .until(ExpectedConditions.numberOfElementsToBe(productLocator, totalProduct));
    }

    public void clickAndGetProduct(List<WebElement> resultElements,
                                   List<WebElement> optionElements,
                                   int i, int j,
                                   PhoneConfiguration phone)
            throws TimeoutException, IOException {
        if (j > 0) {
            By by = By.cssSelector(".item.ajaxed.__cate_42.loading-border");
            try {
                resultElements.clear();
                resultElements.addAll(wait
                        .until(ExpectedConditions.numberOfElementsToBe(productLocator, totalProduct)));
                optionElements.clear();
                optionElements.addAll(resultElements.get(i).findElements(optionLocator));
                wait.until(ExpectedConditions.elementToBeClickable(optionElements.get(j))).click();
                wait.until(ExpectedConditions.numberOfElementsToBe(by, 0));
                resultElements.clear();
                resultElements.addAll(
                        wait.until(ExpectedConditions.numberOfElementsToBe(productLocator, totalProduct))
                );
                collectProduct(resultElements.get(i), phone);
            } catch (Exception e) {

            }
        } else {
            collectProduct(resultElements.get(i), phone);
        }
    }

    public List<Result> getResults(boolean isSearch, PhoneConfiguration phone) throws IOException {
        List<WebElement> resultElements; // Danh sách điện thoại tìm được
        List<WebElement> optionElements; // Các phần tử option
        if (isSearch) {
            resultElements = getSearchProducts();
        } else {
            resultElements = checkEnoughProducts();
        }
        // Get each product
        for (int i = 0; i < resultElements.size(); i++) {
            optionElements = resultElements.get(i).findElements(optionLocator);
            // Select each option of product
            if (optionElements.size() > 0) {
                // Get option and click
                for (int j = 0; j < optionElements.size(); j++) {
                    clickAndGetProduct(resultElements, optionElements, i, j, phone);
                }
            } else {
                collectProduct(resultElements.get(i), phone);
            }
        }
        return results;
    }
}
