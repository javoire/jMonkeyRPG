package testgame.inventory;

import java.util.Iterator;
import java.util.Vector;

import testgame.items.AbstractItem.ItemType;
import testgame.items.Ammunition;
import testgame.items.Weapon;
import testgame.items.resources.Resource;
import testgame.items.resources.Resource.ResourceType;

import com.jme3.app.state.AbstractAppState;

public class Inventory extends AbstractAppState {
	
	private int size;
	
	/**
	 * @param size Number of items it can hold
	 */
	public Inventory(int size) {
		this.size = size;
	}
	
	private Vector<Resource> resources = new Vector<Resource>();
	private Vector<Weapon> weapons = new Vector<Weapon>();
	private Vector<Ammunition> ammunition = new Vector<Ammunition>();
	
	public boolean add(Resource resource) {
		resources.add(resource);
		return true;
	}
	public boolean add(Weapon weapon) {
		weapons.add(weapon);
		return true;
	}
	public boolean add(Ammunition ammo) {
		ammunition.add(ammo);
		return true;
	}
	public Vector<Resource> getResources() {
		return resources;
	}
	public Vector<Weapon> getWeapons() {
		return weapons;
	}
	public Vector<Ammunition> getAmmunition() {
		return ammunition;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * @return A list with all item names as strings
	 */
	public Vector<String> getItemList() {
		Vector<String> itemNames = new Vector<String>();
		Iterator<Weapon> w = weapons.iterator();
		while(w.hasNext())
			itemNames.add(w.next().getName());
		Iterator<Resource> r = resources.iterator();
		while(r.hasNext()) {
			Resource res = r.next();
			itemNames.add(res.getName() + ": " + res.getQuantity());
		}
		Iterator<Ammunition> a = ammunition.iterator();
		while(a.hasNext())
			itemNames.add(a.next().getName());

		return itemNames;
	}
	
	/**
	 * @param resource enum ResourceType
	 * @return True if the given resource type exists
	 */
	public boolean resourceExists(ResourceType resource) {
		Iterator<Resource> i = resources.iterator();
		while(i.hasNext()) {
			if(i.next().getResourceType().equals(ResourceType.WOOD))
				return true;
		}
		return false;
	}

	/**
	 * @param resource enum ResourceType
	 * @return Resource if it exists. Otherwise null
	 */
	public Resource getResource(ResourceType resource) {
		Iterator<Resource> i = resources.iterator();
		while(i.hasNext()) {
			Resource res = i.next();
			if(res.getResourceType().equals(ResourceType.WOOD))
				return res;
		}
		return null;
	}
}
