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

import selenium.model.FilterList;
import selenium.model.PhoneConfiguration;
import selenium.model.Result;
import selenium.page.DMX;
import selenium.page.FPT;
import selenium.page.TGDD;

@Controller
public class MyController {

	private FilterList filterList = new FilterList();

	private ChromeDriver initChromeDriver() {
		String webDriverPath = "C:/Users/THANHTRUNG/OneDrive - student.ptithcm.edu.vn/Documents/NCKH/chromedriver.exe";
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
	public String postHome(ModelMap model, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone)
			throws InterruptedException {
		if (phone.getBrand() == null
				&& phone.getPriceRange() ==null
				&& phone.getRam() == null
				&& phone.getRom() == null
				&& phone.getSpecialFeature() == null
				&& phone.getDisplaySize() == null) {
			model.addAttribute("message", "Vui lòng chọn ít nhất 1 thuộc tính!");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		List<Result> results = new ArrayList<Result>();
		ChromeDriver driver = this.initChromeDriver();

		//////////////////////////////// BEGIN TGDD - DMX
		TGDD tgdd = new TGDD(driver);
		DMX dmx = new DMX(driver);
		String resultTGDD = tgdd.run(phone, results);
		String resultDMX = dmx.run(phone, results);
		if (!resultTGDD.isEmpty()) {
			model.addAttribute("tgdd_message", resultTGDD);
		}
		if (!resultDMX.isEmpty()) {
			model.addAttribute("dmx_message", resultDMX);
		}
		//////////////////////////////// END TGDD - DMX
		
		//////////////////////////////// BEGIN FPT
		FPT fpt = new FPT(driver);
		String resultFPT = fpt.run(phone, results);
		if (!resultFPT.isEmpty()) {
			model.addAttribute("fpt_message", resultFPT);
		}
		//////////////////////////////// END FPT
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
}
