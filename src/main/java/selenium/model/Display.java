package selenium.model;

public class Display {
	private String tech;
	private String size;
	private String resolution;
	private String material;
	private Double frequency;
	public String getTech() {
		return tech;
	}
	public void setTech(String tech) {
		this.tech = tech;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public Double getFrequency() {
		return frequency;
	}
	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}
	public Display(String tech, String size, String resolution, String material, Double frequency) {
		super();
		this.tech = tech;
		this.size = size;
		this.resolution = resolution;
		this.material = material;
		this.frequency = frequency;
	}
	public Display() {
		super();
		// TODO Auto-generated constructor stub
	}
}
