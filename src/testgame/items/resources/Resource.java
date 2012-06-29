package testgame.items.resources;

import testgame.items.AbstractItem;

public class Resource extends AbstractItem {

	public enum ResourceType {
		WOOD,
		STONE,
		WATER
	}
	
	private ResourceType type;
	private int quantity;
	
	public Resource(ResourceType type) {
		super(ItemType.RESOURCE);
		switch (type) {
			case WOOD:
				setName("Wood"); 
			case STONE:
				setName("Stone");
			case WATER:
				setName("Water");
			default:
				setName("Wood");
		}
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