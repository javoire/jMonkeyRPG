package testgame.items;

public class EquipableItem extends AbstractItem {
	
	public enum EquipableType {
		WEAPON,
		ARMOR
	}

	private EquipableType type;
	
	public EquipableItem(EquipableType type) {
		super(ItemType.EQUIPABLE);
		this.type = type;
	}

	public EquipableType getType() {
		return type;
	}
}
