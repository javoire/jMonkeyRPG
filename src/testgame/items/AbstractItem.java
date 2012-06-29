package testgame.items;


public class AbstractItem {
	
	public enum ItemType {
		RESOURCE,
		EQUIPABLE,
		AMMUNITION
	}

	private ItemType type;
	private String name;
	private double quality;
	private boolean isResource = false;
	private boolean allowDuplicates = true;
	
	public AbstractItem(ItemType type) {
		this.type = type;
		this.quality = 1.0; // default
		this.name = "nullItem"; // default
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAllowDuplicates() {
		return allowDuplicates;
	}

	public void setAllowDuplicates(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	public boolean isResource() {
		return isResource;
	}

	public void setIsResource(boolean isResource) {
		this.isResource = isResource;
	}

	public double getQuality() {
		return quality;
	}

	public void setQuality(double quality) {
		this.quality = quality;
	}

	public ItemType getItemType() {
		return type;
	}
}
