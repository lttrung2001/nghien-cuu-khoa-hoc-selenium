package selenium.model;
public class PhoneConfiguration {
	@Override
	public String toString() {
		return "PhoneConfiguration [brand=" + brand + ", priceRange=" + priceRange + ", displaySize=" + displaySize
				+ ", specialFeature=" + specialFeature + ", ram=" + ram + ", rom=" + rom + "]";
	}

	public PhoneConfiguration() {
		super();
	}

	private String[] brand;
	private String[] priceRange;
	private String[] displaySize;
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

	public String[] getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(String[] displaySize) {
		this.displaySize = displaySize;
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

}
