package selenium.model;

import java.util.List;

public class RearCamera {
	private String summary;
	private List<String> features;
	
	public RearCamera() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RearCamera(String summary, List<String> features) {
		super();
		this.summary = summary;
		this.features = features;
	}



	public String getSummary() {
		return summary;
	}



	public void setSummary(String summary) {
		this.summary = summary;
	}



	public List<String> getFeatures() {
		return features;
	}



	public void setFeatures(List<String> features) {
		this.features = features;
	}

}
