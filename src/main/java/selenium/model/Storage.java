package selenium.model;

public class Storage {
	private String ram;
	private String rom;

	public Storage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Storage(String ram, String rom) {
		super();
		this.ram = ram;
		this.rom = rom;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getRom() {
		return rom;
	}

	public void setRom(String rom) {
		this.rom = rom;
	}

}
