package selenium.model;

import java.util.List;

public class Result {

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProductLink() {
		return productLink;
	}

	public void setProductLink(String productLink) {
		this.productLink = productLink;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	private String imageLink;
	private String name;
	private String price;
	private String productLink;
	private List<String> details;

	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Result(String imageLink, String name, String price, String productLink, List<String> details) {
		super();
		this.imageLink = imageLink;
		this.name = name;
		this.price = price;
		this.productLink = productLink;
		this.details = details;
	}

}
