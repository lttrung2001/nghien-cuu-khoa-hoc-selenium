package selenium.page;

import org.openqa.selenium.chrome.ChromeDriver;

public class DMX extends TGDD {
	
	public DMX(ChromeDriver driver) {
		super(driver);
		super.url = "https://www.dienmayxanh.com/dien-thoai";
		super.baseUrl = "https://www.dienmayxanh.com/";
	}
}
