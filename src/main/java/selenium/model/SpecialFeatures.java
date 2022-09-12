package selenium.model;

public class SpecialFeatures {
	private boolean isResistant;
	private boolean isSp5G;
	private boolean isSpFaceSecurity;

	public SpecialFeatures(boolean isResistant, boolean isSp5G, boolean isSpFaceSecurity) {
		super();
		this.isResistant = isResistant;
		this.isSp5G = isSp5G;
		this.isSpFaceSecurity = isSpFaceSecurity;
	}

	public SpecialFeatures() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isResistant() {
		return isResistant;
	}

	public void setResistant(boolean isResistant) {
		this.isResistant = isResistant;
	}

	public boolean isSp5G() {
		return isSp5G;
	}

	public void setSp5G(boolean isSp5G) {
		this.isSp5G = isSp5G;
	}

	public boolean isSpFaceSecurity() {
		return isSpFaceSecurity;
	}

	public void setSpFaceSecurity(boolean isSpFaceSecurity) {
		this.isSpFaceSecurity = isSpFaceSecurity;
	}

}
