package testgame.items;

public class Ammunition extends AbstractItem {
	
	public enum AmmoType {
		ARROW,
		BALL
	}
	
	private AmmoType type;
	private int quantity;
	
	public Ammunition(AmmoType type) {
		super(ItemType.AMMUNITION);
	}

	public AmmoType getAmmoType() {
		return type;
	}

	public void setAmmoType(AmmoType type) {
		this.type = type;
	}

	public int getAmmoQuantity() {
		return quantity;
	}

	public void setAmmoQuantity(int quantity) {
		this.quantity = quantity;
	}
}
