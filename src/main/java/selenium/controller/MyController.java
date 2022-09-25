package selenium.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.bonigarcia.wdm.WebDriverManager;
import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;
import selenium.page.DMX;
import selenium.page.FPT;
import selenium.page.TGDD;

@Controller
public class MyController {

	private FilterList filterList = new FilterList();
	List<Result> results;

	private ChromeDriver initChromeDriver() throws IOException {
//		String webDriverPath = "C:\\Users\\THANHTRUNG\\OneDrive - student.ptithcm.edu.vn\\Desktop\\eclipse_workspace\\NCKH_BOOT\\src\\main\\resources\\chromedriver.exe";
//		System.setProperty("webdriver.chrome.driver", webDriverPath);
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--headless");
//		options.addArguments("--disable-gpu");
//		options.addArguments("--no-sandbox");
//		ChromeDriver driver = new ChromeDriver(options);
		ChromeDriver driver = (ChromeDriver) WebDriverManager.chromedriver().create();
		// Xoa tat ca cookie
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		return driver;
	}

	@RequestMapping(value = {"/","home"})
	public String getHome(ModelMap model) {
		model.addAttribute("ft", filterList);
		model.addAttribute("phoneConfiguration", new PhoneConfiguration());
		return "index";
	}

	@RequestMapping(value = "result" , method = RequestMethod.POST)
	public String postHome(ModelMap model, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone) throws IOException {
		if (phone.getBrand().length == 0
				&& phone.getPriceRange().length == 0
				&& phone.getRam().length == 0
				&& phone.getRom().length == 0
				&& phone.getSpecialFeature().length == 0) {
			model.addAttribute("message", "Vui lòng chọn ít nhất 1 thuộc tính!");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		ChromeDriver driver = initChromeDriver();
		results = new ArrayList<Result>();
//		String resultTGDD = new TGDD(driver).run(phone, results);
//		if (!resultTGDD.isEmpty()) {
//			model.addAttribute("tgdd_message", resultTGDD);
//		}
//		String resultDMX = new DMX(driver).run(phone, results);
//		if (!resultDMX.isEmpty()) {
//			model.addAttribute("dmx_message", resultDMX);
//		}
		String resultFPT = new FPT(driver, phone).run(phone, results);		
		if (!resultFPT.isEmpty()) {
			model.addAttribute("fpt_message", resultFPT);
		}

		if (results.isEmpty()) {
				model.addAttribute("message", "Không có sản phẩm nào theo cấu hình vừa chọn!");
				model.addAttribute("ft", filterList);
				driver.quit();
				return "index";				
		} else {
			model.addAttribute("resultList", results);
			model.addAttribute("amountFinded", results.size());
			driver.quit();
			return "show-result";
		}
	}
	
	@RequestMapping(value = "search", method = RequestMethod.POST, params = "btnSearch")
	public String postSearch(ModelMap model, @RequestParam("key") String key, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone) throws IOException {
		if (key.isBlank()) {
			model.addAttribute("message", "Nhập thông tin để tìm kiếm");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		ChromeDriver driver = initChromeDriver();
		results = new ArrayList<Result>();
		String resultTGDD = new TGDD(driver).search(key, results);
		String resultDMX = new DMX(driver).search(key, results);
		String resultFPT = new FPT(driver, phone).search(key, results);
		
		if (!resultTGDD.isEmpty()) {
			model.addAttribute("tgdd_message", resultTGDD);
		}
		if (!resultDMX.isEmpty()) {
			model.addAttribute("dmx_message", resultDMX);
		}
		if (!resultFPT.isEmpty()) {
			model.addAttribute("fpt_message", resultFPT);
		}
		
		if (results.isEmpty()) {
			model.addAttribute("message", "Không có sản phẩm nào theo cấu hình vừa tìm!");
			model.addAttribute("ft", filterList);
			driver.quit();
			return "index";
		} else {
			model.addAttribute("resultList", results);
			driver.quit();
			return "search-result";
		}
	}
}
