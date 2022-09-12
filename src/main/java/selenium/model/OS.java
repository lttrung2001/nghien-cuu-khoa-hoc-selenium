package selenium.model;

public class OS {
	private String name;
	private String version;

	public OS() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OS(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
