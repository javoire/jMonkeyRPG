package testgame.inventory;

import java.util.Iterator;
import java.util.Vector;

import testgame.items.Item;

public class Inventory extends Vector<Item> {

	private static final long serialVersionUID = 1L;
	
	public Inventory(int size) {
		setSize(size);
	}
	
	public boolean add(Item item) {
		return super.add(item);
	}
	
	public void setSize(int newSize) {
		super.setSize(newSize);
	}

	public Vector<String> getItemList() {
		Iterator<Item> i = iterator();
		while(i.hasNext()) {
			
		}
		return null;
	}
	
//	public get()
}
