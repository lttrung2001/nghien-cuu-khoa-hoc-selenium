package selenium.model;

import java.util.List;

public class APIResult {
	public class Property {
		private String name;
		private String value;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public Property(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
		public Property() {
			super();
			// TODO Auto-generated constructor stub
		}
	}
	
	public class Detail {
		private String category;
		private List<Property> specs;
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public List<Property> getSpecs() {
			return specs;
		}
		public void setSpecs(List<Property> specs) {
			this.specs = specs;
		}
		@Override
		public String toString() {
			return "Detail [category=" + category + ", specs=" + specs + "]";
		}
		public Detail(String category, List<Property> specs) {
			super();
			this.category = category;
			this.specs = specs;
		}
		public Detail() {
			super();
			// TODO Auto-generated constructor stub
		}
	}

	public APIResult() {
		// TODO Auto-generated constructor stub
	}
	private String title;
	private String img;
	private String img_url;
	private List<Detail> spec_detail;
	private List<Property> quick_spec;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public List<Detail> getSpec_detail() {
		return spec_detail;
	}
	public void setSpec_detail(List<Detail> spec_detail) {
		this.spec_detail = spec_detail;
	}
	public List<Property> getQuick_spec() {
		return quick_spec;
	}
	public void setQuick_spec(List<Property> quick_spec) {
		this.quick_spec = quick_spec;
	}
	@Override
	public String toString() {
		return "APIResult [title=" + title + ", img=" + img + ", img_url=" + img_url + ", spec_detail=" + spec_detail
				+ ", quick_spec=" + quick_spec + "]";
	}
	public APIResult(String title, String img, String img_url, List<Detail> spec_detail, List<Property> quick_spec) {
		super();
		this.title = title;
		this.img = img;
		this.img_url = img_url;
		this.spec_detail = spec_detail;
		this.quick_spec = quick_spec;
	}
	
}