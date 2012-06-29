package testgame.inventory;

import java.util.Vector;

import testgame.items.Ammunition;
import testgame.items.Weapon;
import testgame.items.resources.Resource;

// TODO fixa nå smart så den här kan lagra olika typer av objekt... 
public class InventoryList {

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
}
