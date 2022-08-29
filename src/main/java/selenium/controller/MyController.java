package selenium.controller;

import java.time.Duration;





import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	ChromeDriver driver;
	PhoneConfiguration phone;

	private ChromeDriver initChromeDriver() {
		String webDriverPath = "C:\\Users\\THANHTRUNG\\OneDrive - student.ptithcm.edu.vn\\Desktop\\eclipse_workspace\\NCKH_BOOT\\src\\main\\resources\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", webDriverPath);
		ChromeOptions options = new ChromeOptions();
		ChromeDriver driver = new ChromeDriver(options);
		// Xoa tat ca cookie
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		return driver;
	}

	@RequestMapping(value = {"/","home"})
	public String getHome(ModelMap model) {
		model.addAttribute("ft", filterList);
		model.addAttribute("phoneConfiguration", new PhoneConfiguration());
		return "index";
	}

	@RequestMapping(value = "result" , method = RequestMethod.POST)
	public String postHome(ModelMap model, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone) {
		this.phone = phone;
		if (phone.getBrand().length == 0
				&& phone.getPriceRange().length == 0
				&& phone.getRam().length == 0
				&& phone.getRom().length == 0
				&& phone.getSpecialFeature().length == 0
				&& phone.getDisplaySize().length == 0) {
			model.addAttribute("message", "Vui lòng chọn ít nhất 1 thuộc tính!");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		driver = initChromeDriver();
		results = new ArrayList<Result>();
		String resultTGDD = new TGDD(driver).run(phone, results);
		String resultDMX = new DMX(driver).run(phone, results);
		String resultFPT = new FPT(driver, phone).run(phone, results);
		
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
	public String postSearch(ModelMap model, @RequestParam("key") String key, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone) {
		if (key.isBlank()) {
			model.addAttribute("message", "Nhập thông tin để tìm kiếm");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		driver = initChromeDriver();
		results = new ArrayList<Result>();
//		String resultTGDD = new TGDD(driver).search(key, results);
//		String resultDMX = new DMX(driver).search(key, results);
		String resultFPT = new FPT(driver, phone).search(key, results);
		
//		if (!resultTGDD.isEmpty()) {
//			model.addAttribute("tgdd_message", resultTGDD);
//		}
//		if (!resultDMX.isEmpty()) {
//			model.addAttribute("dmx_message", resultDMX);
//		}
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
