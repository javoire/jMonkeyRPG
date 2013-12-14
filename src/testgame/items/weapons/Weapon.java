package testgame.items.weapons;

import testgame.items.EquippableItem;
import testgame.items.EquippableItem.EquippableType;

public class Weapon extends EquippableItem {
	
	/**
	 * In combination with other modifiers, it defines the force of this weapon 
	 * such as velocityMultiplier of a ranged weapon, or damageMultiplier etc 
	 */
	protected Float 	maxChargeTime = 3f;
	/**
	 * The minimum force this ranged weapon has, will modify final damage or velocity etc
	 */
	protected Float		minForce = 0.1f;
	/**
	 * The maximum force this weapon can have
	 */
	protected Float		maxForce = 10f;

	
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
	
	/**
	 * Returns the force calculated from how long we charged the weapon, and it's max and min force values
	 * @param chargeTime
	 * @return <code>Float</code> chargedForce
	 */
	public Float getChargedForce(Float chargeTime) {
		// take chargeTime, and pick a value between minForce and maxForce corresponding to chargeTime 0 and maxChargetime
		// eg. (maxForce-minForce)*chargeTime/maxChargeTime
		Float forceDelta = (maxForce-minForce)*chargeTime/maxChargeTime; // gets a value between minForce and maxForce, depeding on chargeTime/maxChargeTime ratio
		return minForce + forceDelta;
	}

	// ? 
	public void use() {
		// is overridden in subclasses
	}

	public WeaponType getWeaponType() {
		return this.type;
	}

	public void setWeaponType(WeaponType type) {
		this.type = type;
	}
	
	public Float getMaxChargeTime() {
		return maxChargeTime;	
	}
	
	/**
	 * This basically determines how long we need to charge the weapon to reach it's maximum force
	 * @param maxChargeTime
	 */
	public void setMaxChargeTime(float maxChargeTime) {
		this.maxChargeTime = maxChargeTime;
	}

	public Float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}
	
	public Float getMinForce() {
		return minForce;
	}

	public void setMinForce(Float minForce) {
		this.minForce = minForce;
	}
}