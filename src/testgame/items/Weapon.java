package testgame.items;

public class Weapon extends EquipableItem {
	
	public enum WeaponType {
		BOW,
		SWORD
	}
	
	private WeaponType type;

	/**
	 * Must take a weapon type and a name
	 * @param type Weapon type
	 * @param name Name
	 */
	public Weapon(WeaponType type, String name) {
		super(EquipableType.WEAPON);
		this.type = type;
		setName(name);
	}

	public WeaponType getWeaponType() {
		return type;
	}
}
