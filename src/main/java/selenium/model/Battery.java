package selenium.model;

import java.util.List;

public class Battery {
	private int capacity;
	private String type;
	private List<String> technologies;

	public Battery(int capacity, String type, List<String> technologies) {
		super();
		this.capacity = capacity;
		this.type = type;
		this.technologies = technologies;
	}

	public Battery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(List<String> technologies) {
		this.technologies = technologies;
	}

}
