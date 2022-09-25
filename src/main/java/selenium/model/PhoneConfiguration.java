package selenium.model;

import java.util.Arrays;

public class PhoneConfiguration {

	public PhoneConfiguration() {
		super();
	}

	private String[] brand;
	private String[] priceRange;
	private String[] specialFeature;
	private String[] ram;
	private String[] rom;
	public String[] getBrand() {
		return brand;
	}
	public void setBrand(String[] brand) {
		this.brand = brand;
	}
	public String[] getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String[] priceRange) {
		this.priceRange = priceRange;
	}
	public String[] getSpecialFeature() {
		return specialFeature;
	}
	public void setSpecialFeature(String[] specialFeature) {
		this.specialFeature = specialFeature;
	}
	public String[] getRam() {
		return ram;
	}
	public void setRam(String[] ram) {
		this.ram = ram;
	}
	public String[] getRom() {
		return rom;
	}
	public void setRom(String[] rom) {
		this.rom = rom;
	}
	@Override
	public String toString() {
		return "PhoneConfiguration [brand=" + Arrays.toString(brand) + ", priceRange=" + Arrays.toString(priceRange)
				+ ", specialFeature=" + Arrays.toString(specialFeature) + ", ram=" + Arrays.toString(ram) + ", rom="
				+ Arrays.toString(rom) + "]";
	}
	public PhoneConfiguration(String[] brand, String[] priceRange, String[] specialFeature, String[] ram,
			String[] rom) {
		super();
		this.brand = brand;
		this.priceRange = priceRange;
		this.specialFeature = specialFeature;
		this.ram = ram;
		this.rom = rom;
	}
	
	
}
