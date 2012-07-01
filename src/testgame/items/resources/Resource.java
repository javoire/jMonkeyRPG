package testgame.items.resources;

import testgame.items.AbstractItem;

public class Resource extends AbstractItem {

	public enum ResourceType {
		WOOD("Wood"),
		STONE("Stone"),
		WATER("Water"),
		LEAVES("Leaves");
		

	    private ResourceType(final String text) {
	        this.text = text;
	    }

	    private final String text;

	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	private ResourceType type;
	private int quantity;
	
	public Resource(ResourceType type) {
		super(ItemType.RESOURCE);
		setName(type.toString());
		this.type = type;
		this.quantity = 1;
	}

	public ResourceType getResourceType() {
		return type;
	}

	public void setResourceType(ResourceType type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void addQuantity(int add) {
		quantity += add;
	}

	public void setQuantity(int amount) {
		this.quantity = amount;
	}
}