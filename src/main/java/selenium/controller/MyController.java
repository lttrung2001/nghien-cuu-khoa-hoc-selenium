package selenium.controller;

import java.time.Duration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import selenium.page.TGDD;

@Controller
public class MyController {
	@RequestMapping({"/","home"})
	@ResponseBody
	public String home() {
		ChromeDriver driver = initChromeDriver();
		return new TGDD(driver).fetchData();
	}
	
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
}
