package testgame.items;

public class EquippableItem extends AbstractItem {
	
	public enum EquippableType {
		WEAPON,
		ARMOR
	}

	private EquippableType type;
	
	public EquippableItem(EquippableType type) {
		super(ItemType.EQUIPABLE);
		this.type = type;
	}

	public EquippableType getType() {
		return type;
	}
}
