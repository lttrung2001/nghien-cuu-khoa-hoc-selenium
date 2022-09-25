package selenium.model;

public class FilterList {
	public String[] getBrands() {
		return brands;
	}

	public String[] getPriceRanges() {
		return priceRanges;
	}

	public String[] getDisplaySize() {
		return displaySize;
	}

	public String[] getSpecialFeatures() {
		return specialFeatures;
	}

	public String[] getRam() {
		return ram;
	}

	public String[] getRom() {
		return rom;
	}

	private String[] brands;
	private String[] priceRanges;
	private String[] displaySize;
	private String[] specialFeatures;
	private String[] ram;
	private String[] rom;

	public FilterList() {
		super();
		this.setBrands(new String[] { "iphone", "samsung", "oppo", "vivo", "xiaomi", "realme", "nokia", "mobell",
				"itel", "masstel", "energizer" });
		this.setPriceRanges(new String[] { "Dưới 2 triệu", "Từ 2 - 4 triệu", "Từ 4 - 7 triệu", "Từ 7 - 13 triệu",
				"Trên 13 triệu" });
		this.setDisplaySize(new String[] { "Nhỏ gọn dễ cầm", "Từ 6 inch trở lên", "Màn hình gập" });
		this.setSpecialFeatures(new String[] { "Bảo mật khuôn mặt", "Hỗ trợ 5G", "Kháng nước & bụi" });
		
		this.setRam(new String[] { "1 GB", "2 GB", "3 GB", "4 GB", "6 GB", "8 GB", "12 GB" });
		this.setRom(new String[] { "8 GB", "16 GB", "32 GB", "64 GB", "128 GB", "256 GB", "512 GB", "1 TB" });
	}

	public void setBrands(String[] brands) {
		this.brands = brands;
	}

	public void setPriceRanges(String[] priceRanges) {
		this.priceRanges = priceRanges;
	}

	public void setDisplaySize(String[] displaySize) {
		this.displaySize = displaySize;
	}

	public void setSpecialFeatures(String[] specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	public void setRam(String[] ram) {
		this.ram = ram;
	}

	public void setRom(String[] rom) {
		this.rom = rom;
	}
}
