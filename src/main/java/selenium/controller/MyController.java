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
	public String postHome(ModelMap model, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone) throws IOException, InterruptedException {
		if (phone.getBrand().length == 0
				&& phone.getPriceRange().length == 0
				&& phone.getRam().length == 0
				&& phone.getRom().length == 0
				&& phone.getSpecialFeature().length == 0) {
			model.addAttribute("message", "Vui lòng chọn ít nhất 1 thuộc tính!");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		results = new ArrayList<Result>();
		List<Result> results1 = new ArrayList<Result>();
		List<Result> results2 = new ArrayList<Result>();
		List<Result> results3 = new ArrayList<Result>();
		
		Thread t1 = new Thread(() -> {
			try {
				String resultTGDD = new TGDD(initChromeDriver()).run(phone, results1);
				if (!resultTGDD.isEmpty()) {
					model.addAttribute("tgdd_message", resultTGDD);
				}
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		});
		Thread t2 = new Thread(() -> {
			try {
				String resultDMX = new DMX(initChromeDriver()).run(phone, results);
				if (!resultDMX.isEmpty()) {
					model.addAttribute("dmx_message", resultDMX);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		});
		Thread t3 = new Thread(() -> {
			try {
				String resultFPT = new FPT(initChromeDriver(), phone).run(phone, results);
				if (!resultFPT.isEmpty()) {
					model.addAttribute("fpt_message", resultFPT);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		});
		Thread[] threads = {t1, t2, t3};
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
		
		results.addAll(results1);
		results.addAll(results2);
		results.addAll(results3);	

		if (results.isEmpty()) {
				model.addAttribute("message", "Không có sản phẩm nào theo cấu hình vừa chọn!");
				model.addAttribute("ft", filterList);
				return "index";				
		} else {
			model.addAttribute("resultList", results);
			model.addAttribute("amountFinded", results.size());
			return "show-result";
		}
	}
	
	@RequestMapping(value = "search", method = RequestMethod.POST, params = "btnSearch")
	public String postSearch(ModelMap model, @RequestParam("key") String key, @ModelAttribute("phoneConfiguration") PhoneConfiguration phone) throws IOException, InterruptedException {
		if (key.isBlank()) {
			model.addAttribute("message", "Nhập thông tin để tìm kiếm");
			model.addAttribute("ft", filterList);
			return "index";
		}
		
		results = new ArrayList<Result>();
		List<Result> results1 = new ArrayList<Result>();
		List<Result> results2 = new ArrayList<Result>();
		List<Result> results3 = new ArrayList<Result>();
		
		Thread t1 = new Thread(() -> {
			try {
				String searchTGDD = new TGDD(initChromeDriver()).search(key, results1);
				if (!searchTGDD.isEmpty()) {
					model.addAttribute("tgdd_message", searchTGDD);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Thread t2 = new Thread(() -> {
			try {
				String searchDMX = new DMX(initChromeDriver()).search(key, results2);
				if (!searchDMX.isEmpty()) {
					model.addAttribute("dmx_message", searchDMX);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Thread t3 = new Thread(() -> {
			try {
				String searchFPT = new FPT(initChromeDriver(), phone).search(key, results3);
				if (!searchFPT.isEmpty()) {
					model.addAttribute("fpt_message", searchFPT);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Thread[] threads = {t1, t2, t3};
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
		
		results.addAll(results1);
		results.addAll(results2);
		results.addAll(results3);
		
		if (results.isEmpty()) {
			model.addAttribute("message", "Không có sản phẩm nào theo cấu hình vừa tìm!");
			model.addAttribute("ft", filterList);
			return "index";
		} else {
			model.addAttribute("resultList", results);
			return "search-result";
		}
	}
}
