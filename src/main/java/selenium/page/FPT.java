package selenium.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.gson.Gson;

import constant.Constant;
import selenium.model.Model;
import selenium.model.Phone;

public class FPT extends TGDD {

	public FPT(ChromeDriver driver) {
		super(driver);
		url = "https://fptshop.com.vn/dien-thoai";
		defaultNumber = 27;
	}

	@Override
	public void connect() {
		try {
			driver.get(url);
			By totalLocate = By.cssSelector(".cdt-head > span");
			String resultString = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLocate)).getText();
			String totalString = resultString.substring(1, resultString.indexOf(" "));
			System.out.println(totalString);
			total = Integer.parseInt(totalString);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String fetchData() {
		By productLocate = By.className("cdt-product");
		List<WebElement> productElements;
		int start = 0;
		int currentShow = 0;
		boolean havingPrice = true;
		do {
			start = currentShow;
			currentShow += total - currentShow > defaultNumber ? defaultNumber : total - currentShow;
			productElements = wait.until(ExpectedConditions.numberOfElementsToBe(productLocate, currentShow));
			System.out.println("currentShow:" + currentShow);
			System.out.println("start:" + start);
			for (int i = start; i < productElements.size(); i++) {
				new Actions(driver).moveToElement(productElements.get(i)).build().perform();
				try {
					productElements.get(i).findElement(By.className("progress"));
					result.add(productElements.get(i).findElement(By.tagName("a")).getAttribute("href"));
				} catch (NoSuchElementException e) {
					try {
						productElements.get(i).findElement(By.className("price"));
						result.add(productElements.get(i).findElement(By.tagName("a")).getAttribute("href"));
					} catch (NoSuchElementException e2) {
						havingPrice = false;
						break;
					}
				}
			}
			if (havingPrice && currentShow < total) {
				By viewMoreLocate = By.cssSelector("div.cdt-product--loadmore > a");
				WebElement viewMoreElement = driver.findElement(viewMoreLocate);
				viewMoreElement.click();
			}
		} while (havingPrice && currentShow < total);

		for (String productUrl : result) {
			driver.get(productUrl);

			List<String> optionUrls = new ArrayList<String>();
			driver.findElements(By.cssSelector(".st-select > a")).forEach(element -> {
				optionUrls.add(element.getAttribute("href"));
			});
			for (int i = 0; i < optionUrls.size(); i++) {
				driver.get(optionUrls.get(i));
				// If product is unserved
				try {
					driver.findElement(By.className("st-stt__noti"));
					continue;
				} catch (Exception e) {

				}

				Phone phone = new Phone();
				// Set brand name and model name for phone
				{
					By modelNameLocate = By.className("st-name");
					String modelName = wait.until(ExpectedConditions.visibilityOfElementLocated(modelNameLocate))
							.getText();
					String brandName = modelName.substring(0, modelName.indexOf(" "));
					phone.setModel(new Model(brandName, modelName));
					phone.setUrl(optionUrls.get(i));
				}

				By headerLocate = By.className("l-pd-header");
				WebElement headerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(headerLocate));
				((JavascriptExecutor) driver)
						.executeScript(String.format("window.scrollTo(0, %d);", headerElement.getRect().getHeight()));

				By showDetailLocate = By.cssSelector(".re-link.js--open-modal2");
				WebElement showDetailElement = wait
						.until(ExpectedConditions.visibilityOfElementLocated(showDetailLocate));
				showDetailElement.click();
				{
					By galleryLocate = By.cssSelector(".lSPager.lSGallery");
					List<String> images = new ArrayList<String>();
					driver.findElement(galleryLocate).findElements(By.tagName("img")).forEach(element -> {
						images.add(element.getAttribute("src"));
					});
					phone.setImages(images);
				}

				By rowDetailLocate = By.className("c-modal__row");
				By rowTitleLocate = By.className("st-table-title");

				List<WebElement> detailElements = driver.findElements(rowDetailLocate);
				String tableTitle;
				for (WebElement webElement : detailElements) {
					// Scroll to it
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
					tableTitle = webElement.findElement(rowTitleLocate).getText();
					if (tableTitle.equals(Constant.THIETKEVATRONGLUONGFPT)) {
						fetchResistance(webElement, phone);
					} else if (tableTitle.equals(Constant.CPUFPT)) {
						fetchCPU(webElement, phone);
					} else if (tableTitle.equals(Constant.RAMFPT)) {
						fetchRAM(webElement, phone);
					} else if (tableTitle.equals(Constant.MANHINHFPT)) {
						fetchDisplay(webElement, phone);
					} else if (tableTitle.equals(Constant.ROMFPT)) {
						fetchROM(webElement, phone);
					} else if (tableTitle.equals(Constant.CAMERASAUFPT)) {
						fetchRearCamera(webElement, phone);
					} else if (tableTitle.equals(Constant.CAMERATRUOCFPT)) {
						fetchFrontCamera(webElement, phone);
					} else if (tableTitle.equals(Constant.BAOMATFPT)) {
						fetchSecurityFeatures(webElement, phone);
					} else if (tableTitle.equals(Constant.KETNOIFPT)) {
						fetch5GFeature(webElement, phone);
					} else if (tableTitle.equals(Constant.PINSACFPT)) {
						fetchBattery(webElement, phone);
					} else if (tableTitle.equals(Constant.HEDIEUHANHFPT)) {
						fetchOS(webElement, phone);
					}
				}
				Gson gson = new Gson();
				System.out.println(gson.toJson(phone));
			}
		}
		return result.toString();
	}

	private void fetchOS(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.OS)) {
				phone.getOs().setName(tableDatas.get(1).getText());
			} else if (tableDatas.get(0).getText().equals(Constant.VERSION)) {
				phone.getOs().setVersion(tableDatas.get(1).getText());
			}
		}
	}

	private void fetchBattery(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.LOAIPIN)) {
				phone.getBattery().setType(tableDatas.get(1).getText());
			} else if (tableDatas.get(0).getText().equals(Constant.DUNGLUONGPIN)) {
				phone.getBattery().setCapacity(Integer.parseInt(tableDatas.get(1).getText().split(" ")[0]));
			} else if (tableDatas.get(0).getText().equals(Constant.THONGTINTHEM)) {
				List<String> techs = new ArrayList<String>();
				tableDatas.get(1).findElements(By.tagName("li")).forEach(element -> {
					techs.add(element.getText());
				});
				phone.getBattery().setTechnologies(techs);
			}
		}
	}

	private void fetch5GFeature(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.HOTROMANG) && tableDatas.get(1).getText().contains("5G")) {
				phone.getSpecialFeatures().setSp5G(true);
			}
		}
	}

	private void fetchSecurityFeatures(WebElement webElement, Phone phone) {
		webElement.findElements(By.tagName("li")).forEach(element -> {
			if (element.getText().equals(Constant.MOKHOAKHUONMAT)) {
				phone.getSpecialFeatures().setSpFaceSecurity(true);
			}
		});
	}

	private void fetchRearCamera(WebElement webElement, Phone phone) {
		try {
			phone.getRearCamera().setSummary(webElement.findElement(By.className("st-table-sub-title")).getText());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.TINHNANG)) {
				List<String> features = new ArrayList<String>();
				tableDatas.get(1).findElements(By.cssSelector(".st-list li")).forEach(element -> {
					features.add(element.getText());
				});
				phone.getRearCamera().setFeatures(features);
			}
		}
	}

	private void fetchFrontCamera(WebElement webElement, Phone phone) {
		try {
			phone.getFrontCamera().setSummary(webElement.findElement(By.className("st-table-sub-title")).getText());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.TINHNANG)) {
				List<String> features = new ArrayList<String>();
				webElement.findElements(By.cssSelector(".st-list li")).forEach(element -> {
					features.add(element.getText());
				});
				phone.getFrontCamera().setFeatures(features);
			}
		}
	}

	private void fetchROM(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.BONHOTRONG)) {
				phone.getStorage().setRom(tableDatas.get(1).getText());
			}
		}
	}

	private void fetchResistance(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.KHANGNUOCVABUI)) {
				phone.getSpecialFeatures().setResistant(true);
			}
		}
	}

	private void fetchCPU(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.TENCPU)) {
				phone.getCpu().setName(tableDatas.get(1).getText());
			} else if (tableDatas.get(0).getText().equals(Constant.CORECPU)) {
				phone.getCpu().setCores(tableDatas.get(1).getText());
			}
		}
	}

	private void fetchRAM(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.RAMFPT)) {
				phone.getStorage().setRam(tableDatas.get(1).getText());
			}
		}
	}

	private void fetchDisplay(WebElement webElement, Phone phone) {
		List<WebElement> tableDatas;
		for (WebElement tr : webElement.findElements(By.tagName("tr"))) {
			tableDatas = tr.findElements(By.tagName("td"));
			if (tableDatas.get(0).getText().equals(Constant.KICHTHUOCMANHINH)) {
				phone.getDisplay()
						.setSize(phone.getDisplay().getSize()+tableDatas.get(1).getText());
			} else if (tableDatas.get(0).getText().equals(Constant.CONGNGHEMANHINH)) {
				phone.getDisplay().setTech(tableDatas.get(1).getText().replace("Chính: ", ""));
			} else if (tableDatas.get(0).getText().equals(Constant.DOPHANGIAI)) {
				phone.getDisplay().setResolution(tableDatas.get(1).getText());
			} else if (tableDatas.get(0).getText().equals(Constant.CHATLIEUMATKINH)) {
				phone.getDisplay().setMaterial(tableDatas.get(1).getText());
			} else if (tableDatas.get(0).getText().equals(Constant.TANSOQUET)) {
				phone.getDisplay().setFrequency(Double.parseDouble(tableDatas.get(1).getText().split(" ")[0]));
			}
		}
	}
}
