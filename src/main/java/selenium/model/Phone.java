package selenium.model;

import java.util.ArrayList;
import java.util.List;

public class Phone {
	private Model model;
	private SpecialFeatures specialFeatures;
	private List<String> images;
	private String url;
	private Battery battery;
	private CPU cpu;
	private Display display;
	private FrontCamera frontCamera;
	private RearCamera rearCamera;
	private OS os;
	private Storage storage;

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public SpecialFeatures getSpecialFeatures() {
		return specialFeatures;
	}

	public void setSpecialFeatures(SpecialFeatures specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Battery getBattery() {
		return battery;
	}

	public void setBattery(Battery battery) {
		this.battery = battery;
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public FrontCamera getFrontCamera() {
		return frontCamera;
	}

	public void setFrontCamera(FrontCamera frontCamera) {
		this.frontCamera = frontCamera;
	}

	public RearCamera getRearCamera() {
		return rearCamera;
	}

	public void setRearCamera(RearCamera rearCamera) {
		this.rearCamera = rearCamera;
	}

	public OS getOs() {
		return os;
	}

	public void setOs(OS os) {
		this.os = os;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String toString() {
		return "Phone [Model=" + model + ", specialFeatures=" + specialFeatures + ", images=" + images + ", url=" + url
				+ ", battery=" + battery + ", cpu=" + cpu + ", display=" + display + ", frontCamera=" + frontCamera
				+ ", rearCamera=" + rearCamera + ", os=" + os + ", storage=" + storage + "]";
	}

	public Phone(Model model, SpecialFeatures specialFeatures, List<String> images, String url, Battery battery,
			CPU cpu, Display display, FrontCamera frontCamera, RearCamera rearCamera, OS os, Storage storage) {
		super();
		this.model = model;
		this.specialFeatures = specialFeatures;
		this.images = images;
		this.url = url;
		this.battery = battery;
		this.cpu = cpu;
		this.display = display;
		this.frontCamera = frontCamera;
		this.rearCamera = rearCamera;
		this.os = os;
		this.storage = storage;
	}

	public Phone() {
		super();
		model = new Model();
		specialFeatures = new SpecialFeatures();
		images = new ArrayList<String>();
		url = "";
		battery = new Battery();
		cpu = new CPU();
		display = new Display();
		frontCamera = new FrontCamera();
		rearCamera = new RearCamera();
		os = new OS();
		storage = new Storage();
		// TODO Auto-generated constructor stub
	}

}
