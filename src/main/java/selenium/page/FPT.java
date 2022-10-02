package selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FPT extends TGDD {
    /* Locator */
    private static final By checkboxLocator = By.cssSelector("div.checkbox.frowitem>a");
    private static final String selector = "div.checkbox.frowitem>a";
    private static final By optionLocator = By.cssSelector(".mmr-box.item1");
    private static By productLocator;
    /* Vị trí các filter của bộ lọc */
    private static final int brandPosition = 0;
    private static final int pricePosition = 1;
    private static final int featurePosition = 2;
    /* Container bộ lọc */
    private List<WebElement> filters;
    /* Các option bộ lọc */
    private List<WebElement> brandElements;
    private List<WebElement> priceElements;
    private List<WebElement> featureElements;
    private final List<Result> results = new ArrayList<>();

    public FPT(ChromeDriver driver) {
        super(driver);
        super.url = "https://fptshop.com.vn/dien-thoai";
        super.baseUrl = "https://fptshop.com.vn/";
        /* Khi tìm kiếm theo từ khóa, paging size mặc định là 8.
         * Khi lọc, paging size mặc định là 27.
         * Paging size là số phần tử tối đa trên 1 trang. */
        super.defaultSearchNumber = 8;
        super.defaultNumber = 27;
    }

    /* Hàm này dùng để lấy các element của bộ lọc
     * để sau này lọc các option theo khai báo của user */
    @Override
    public void getFilterElements() throws TimeoutException {
        By filterLocators = By.cssSelector(".cdt-filter__block");
        filters = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(filterLocators, featurePosition));
        brandElements = filters.get(brandPosition).findElements(checkboxLocator);
        priceElements = filters.get(pricePosition).findElements(checkboxLocator);
        featureElements = filters.get(featurePosition).findElements(checkboxLocator);
    }

    @Override
    public void config() {
        // Khởi tạo biến lưu giữ bộ lọc custom (điểm chung giữa 3 trang web).
        FilterList ft = new FilterList();
        // Thay đổi một số thuộc tính để đồng bộ bộ lọc giữa 3 trang web.
        try {
            js.executeScript(String.format("arguments[0].title = '%s';", ft.getSpecialFeatures()[0]),
                    featureElements.get(2));
            js.executeScript(String.format("arguments[0].title = '%s';", ft.getSpecialFeatures()[2]),
                    featureElements.get(3));
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Bộ lọc trên trang " + url + " đã thay đổi");
        }
    }

    /* Hàm này dùng để lọc thuộc tính hãng điện thoại. */
    @Override
    public void filterBrand(String[] strings) throws TimeoutException {
        String tmp;
        for (WebElement brandElement : brandElements) {
            for (String string : strings) {
                if (brandElement.getAttribute("title").toLowerCase().contains(string)) {
                    wait.until(ExpectedConditions.elementToBeClickable(brandElement));
                    tmp = brandElement.getAttribute("href");
                    brandElement.click();
                    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(brandElement, "href", tmp)));
                    break;
                }
            }
        }
    }

    /* Hàm này dùng để thao tác lọc các thuộc tính khác. */
    public void filterOther(String[] strings, int index) throws TimeoutException {
        String tmp;
        List<WebElement> elements = filters.get(index).findElements(By.cssSelector(selector));
        for (WebElement element : elements) {
            for (String string : strings) {
                if (element.getAttribute("title").equals(string)) {
                    wait.until(ExpectedConditions.elementToBeClickable(element));
                    tmp = element.getAttribute("href");
                    element.click();
                    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(element, "href", tmp)));
                    break;
                }
            }
        }
    }

    /* Hàm này dùng để tương tác bộ lọc và đọc tổng số điện thoại đúng cấu hình vừa lọc. */
    @Override
    public void filterAll(PhoneConfiguration phone) throws TimeoutException {
        if (phone.getBrand() != null)
            filterBrand(phone.getBrand());

        if (phone.getPriceRange() != null)
            filterOther(phone.getPriceRange(), pricePosition);

        if (phone.getSpecialFeature() != null)
            filterOther(phone.getSpecialFeature(), featurePosition);

        // Biến totalLocator lưu giữ locator của element chứa tổng số lượng sản phâm lọc được.
        By totalLocator = By.cssSelector(".cdt-head>span");
        try {
            String total = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLocator))
                    .getText();
            totalProduct = Integer.parseInt(total.split(" ")[0].replace("(", ""));
            driver.get(driver.getCurrentUrl().concat("&trang=" + (int) Math.ceil(1.0 * totalProduct / defaultNumber)));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Không thể convert totalProduct vì sai định dạng");
        }
    }

    @Override
    public String run(PhoneConfiguration phone, List<Result> allResults) {
        String res = "";
        try {
            connect(url); // Kết nối tới web
            getFilterElements(); // Lấy bộ lọc
            config(); // Cấu hình lại bộ lọc
            filterAll(phone); // Tiến hành lọc
            allResults.addAll(getResults(false, phone)); // Đọc kết quả và trả về
        } catch (TimeoutException e) {
            allResults.addAll(results);
            e.printStackTrace();
            res = "Lỗi Timeout";
        } catch (Exception e) {
            allResults.addAll(results);
            e.printStackTrace();
            res = e.getMessage();
        }
        driver.quit();
        return res;
    }

    /* Hàm này dùng để đọc kết quả 1 phần tử chứa thông tin cấu hình điện thoại. */
    @Override
    public void collectProduct(WebElement element, PhoneConfiguration phone) {
        // Cuộn màn hình tới element.
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        By imgLocator = By.tagName("img");
        By productNameLocator = By.cssSelector(".cdt-product__name");
        By configLocator = By.cssSelector(".cdt-product__config__param");
        // Lấy url ảnh
        String img = element.findElement(imgLocator).getAttribute("src");
        String name = element.findElement(By.cssSelector(".cdt-product__name")).getText();
        // Lấy giá tiền điện thoại
        String price = ""; // Biến lưu trữ giá tiền
        try {
            price = element.findElement(By.cssSelector(".progress")).getText();
        } catch (NoSuchElementException progressException) {
            try {
                price = element.findElement(By.cssSelector(".price")).getText();
            } catch (NoSuchElementException priceException) {
                price = "Không có thông tin";
            }
        }
        // Lấy url điện thoại
        String url = element.findElement(productNameLocator).getAttribute("href");
        // Lấy chi tiết cấu hình
        List<String> details = new ArrayList<>();
        List<WebElement> list = element.findElements(configLocator);
        if (!list.isEmpty()) {
            WebElement configElement = list.get(0); // Element chứa chi tiết cấu hình
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
        if (phone.getRam() == null) {
            phone.setRam(new String[]{});
        }
        if (phone.getRom() == null) {
            phone.setRom(new String[]{});
        }
        if (phone.getSpecialFeature() == null) {
            phone.setSpecialFeature(new String[]{});
        }
        List<String> RAMs = Arrays.asList(phone.getRam());
        List<String> ROMs = Arrays.asList(phone.getRom());
        boolean res = RAMs.isEmpty() || !e.getAttribute("data-title").equals("RAM")
                || RAMs.contains(e.getAttribute("textContent"));
        if (!ROMs.isEmpty() && e.getAttribute("data-title").equals("Bộ nhớ trong")
                && !ROMs.contains(e.getAttribute("textContent"))) {
            res = false;
        }
        return res;
    }

    @Override
    public List<Result> getResults(boolean isSearch, PhoneConfiguration phone) throws TimeoutException {
        if (isSearch) {
            By totalLocator = By.cssSelector(".re-card h1 > span");
            try {
                totalProduct = Integer.parseInt(driver.findElements(totalLocator).get(0).getText());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Không thể convert totalProduct vì sai định dạng");
            }
            totalProduct = Math.min(totalProduct, defaultSearchNumber);
            productLocator = By.cssSelector(".row-flex > div.cdt-product");
        } else {
            productLocator = By.cssSelector(".cdt-product-wrapper > div.cdt-product");
        }
        if (totalProduct > 0) {
            List<WebElement> resultElements = wait
                    .until(ExpectedConditions.numberOfElementsToBe(productLocator, totalProduct));
            List<WebElement> optionElements;
            for (int i = 0; i < resultElements.size(); i++) {
                optionElements = resultElements.get(i).findElements(optionLocator);
                if (optionElements.size() != 0) {
                    for (int j = 0; j < optionElements.size(); j++) {
                        clickAndGetProduct(resultElements, optionElements, i, j, phone);
                    }
                } else {
                    resultElements.clear();
                    resultElements.addAll(wait
                            .until(ExpectedConditions.numberOfElementsToBe(productLocator, totalProduct)));
                    collectProduct(resultElements.get(i), phone);
                }
            }
        }
        return results;
    }

    @Override
    public void clickAndGetProduct(List<WebElement> resultElements,
                                     List<WebElement> optionElements,
                                     int i, int j,
                                     PhoneConfiguration phone)
            throws TimeoutException {
        if (j > 0) {
            while (true) {
                try {
                    resultElements.clear();
                    resultElements.addAll(wait
                            .until(ExpectedConditions.numberOfElementsToBe(productLocator, totalProduct)));
                    optionElements.clear();
                    optionElements.addAll(resultElements.get(i).findElements(optionLocator));
                    optionElements.get(j).click();
                    collectProduct(resultElements.get(i), phone);
                    break;
                } catch (Exception e) {

                }
            }
        } else {
            collectProduct(resultElements.get(i), phone);
        }
    }

    @Override
    public String search(PhoneConfiguration phone, String key, List<Result> allResults) {
        String res = "";
        try {
            connect(baseUrl + "tim-kiem/" + key + "?Loại%20Sản%20Phẩm=Điện%20thoại");
            allResults.addAll(getResults(true, phone));
        } catch (TimeoutException e) {
            allResults.addAll(results);
            e.printStackTrace();
            res = "Lỗi Timeout";
        } catch (Exception e) {
            allResults.addAll(results);
            e.printStackTrace();
            res = e.getMessage();
        }
        driver.quit();
        return res;
    }
}