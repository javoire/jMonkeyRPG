package testgame.inventory;

import java.util.Iterator;
import java.util.Vector;

import testgame.items.Item;

import com.jme3.app.state.AbstractAppState;

public class Inventory extends AbstractAppState {
	
	private Vector<Item> inventoryList = new Vector<Item>();

	public Inventory(int size) {
		setSize(size);
	}
	
	public boolean add(Item item) {
		return inventoryList.add(item);
	}
	
	public void setSize(int newSize) {
		inventoryList.setSize(newSize);
	}

	public Vector<String> getItemList() {
		Vector<String> items = new Vector<String>();
		Iterator<Item> i = inventoryList.iterator();
		while(i.hasNext()) {
			Item item = i.next();
			if(item!= null)
				items.add(item.getName());
		}
		return items;
	}
}
