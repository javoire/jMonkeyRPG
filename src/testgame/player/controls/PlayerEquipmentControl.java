package testgame.player.controls;

import testgame.items.Weapon;
import testgame.player.items.Armor;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class PlayerEquipmentControl extends AbstractControl {
	
	private Weapon mainHand;
	private Weapon secondaryHand;
	private Armor head;
	private Armor feet;
	private Armor legs;
	private Armor chest;
	private Armor hands;

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
		
	}

	public Weapon getMainHand() {
		return mainHand;
	}

	public void setMainHand(Weapon mainHand) {
		this.mainHand = mainHand;
	}

	public Weapon getSecondaryHand() {
		return secondaryHand;
	}

	public void setSecondaryHand(Weapon secondaryHand) {
		this.secondaryHand = secondaryHand;
	}

	public Armor getHead() {
		return head;
	}

	public void setHead(Armor head) {
		this.head = head;
	}

	public Armor getFeet() {
		return feet;
	}

	public void setFeet(Armor feet) {
		this.feet = feet;
	}

	public Armor getLegs() {
		return legs;
	}

	public void setLegs(Armor legs) {
		this.legs = legs;
	}

	public Armor getChest() {
		return chest;
	}

	public void setChest(Armor chest) {
		this.chest = chest;
	}

	public Armor getHands() {
		return hands;
	}

	public void setHands(Armor hands) {
		this.hands = hands;
	}

}
