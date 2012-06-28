package testgame.controls;

import testgame.game.Player;
import testgame.objects.Tree;

import com.jme3.app.Application;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class HarvestingControl extends AbstractControl {
	
	Player player;
	Tree tree;
	private int amount;
	private Camera cam;
	private String type;
	private int minDistance;
	
	// TODO check when player is in range to display amount available to harvest and allow harvest
	public HarvestingControl() {
	}
	
	public HarvestingControl(Camera camera) {
		cam = camera;
		amount = 200;
		type = "Wood";
		minDistance = 1; // in meters
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if(spatial != null) {

		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void subtractFromAmount(int i) {
		if(amount > 0)
			amount -= i;
	}

	public int getHarvestableDistance() {
		return minDistance;
	}
}
