package testgame.items.weapons;

import testgame.items.EquippableItem;
import testgame.items.EquippableItem.EquippableType;

public class Weapon extends EquippableItem {
	
	public enum WeaponType {
		RANGED,
		MELEE
	}
	
	private WeaponType type;

	/**
	 * Must take a weapon type and a name
	 * @param type Weapon type
	 * @param name Name
	 */
	public Weapon(WeaponType type, String name) {
		super(EquippableType.WEAPON);
		this.type = type;
		setName(name);
	}

	public WeaponType getWeaponType() {
		return this.type;
	}

	public void setWeaponType(WeaponType type) {
		this.type = type;
	}
	
	public void use() {
		// is overridden in subclasses
	}
}
