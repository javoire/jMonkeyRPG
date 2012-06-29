package testgame.items.resources;

import testgame.items.AbstractItem;

public class Resource extends AbstractItem {

	public enum ResourceType {
		WOOD,
		STONE,
		WATER,
		FOLIAGE
	}
	
	private ResourceType type;
	private int quantity;
	
	public Resource(ResourceType type) {
		super(ItemType.RESOURCE);
		// TODO av någon JÄVLA anledning funkar inte switch här så måste köra på en ful satans if sats.... 
		if (type.equals(ResourceType.WOOD))
			setName("Wood");
		else if (type.equals(ResourceType.STONE))
			setName("Stone");
		else if (type.equals(ResourceType.WATER))
			setName("Water");
		else if (type.equals(ResourceType.FOLIAGE))
			setName("Foliage");
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