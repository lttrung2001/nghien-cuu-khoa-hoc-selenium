package selenium.model;

import java.util.List;

public class APISearch {
	public class Info {
		private String name;
		private String img;
		private String url;
		private String description;
		public Info() {
			// TODO Auto-generated constructor stub
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Info(String name, String img, String url, String description) {
			super();
			this.name = name;
			this.img = img;
			this.url = url;
			this.description = description;
		}
		@Override
		public String toString() {
			return "Info [name=" + name + ", img=" + img + ", url=" + url + ", description=" + description + "]";
		}
		
	}
	private List<Info> list;
	public List<Info> getList() {
		return list;
	}
	public void setList(List<Info> list) {
		this.list = list;
	}
	public APISearch(List<Info> list) {
		super();
		this.list = list;
	}
	public APISearch() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "APISearch [list=" + list + "]";
	}
}
