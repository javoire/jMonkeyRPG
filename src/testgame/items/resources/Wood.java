package testgame.items.resources;

import testgame.items.Item;

public class Wood extends Item {
	
	private int quantity = 1;

	public Wood() {
		super("Wood");
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
