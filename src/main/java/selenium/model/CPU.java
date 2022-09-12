package selenium.model;

public class CPU {
	private String name;
	private String cores;

	public CPU(String name, String cores) {
		super();
		this.name = name;
		this.cores = cores;
	}

	public CPU() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCores() {
		return cores;
	}



	public void setCores(String cores) {
		this.cores = cores;
	}

}
