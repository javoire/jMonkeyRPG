package testgame.player.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class PlayerAttributesControl extends AbstractControl {
	
	private double strength;
	private double agility;
	private double intelligence;
	private double vitality;

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

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public double getAgility() {
		return agility;
	}

	public void setAgility(double agility) {
		this.agility = agility;
	}

	public double getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(double intelligence) {
		this.intelligence = intelligence;
	}

	public double getVitality() {
		return vitality;
	}

	public void setVitality(double vitality) {
		this.vitality = vitality;
	}

}
