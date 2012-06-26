package testgame.controls;

import testgame.game.Player;
import testgame.objects.Tree;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class HarvestingControl extends AbstractControl {
	
	Player player;
	Tree tree;
	
	// TODO check when player is in range to display amount available to harvest and allow harvest
	public HarvestingControl() {
		
	}
	
	public HarvestingControl(Player player_) {
		player = player_;
		tree = (Tree) spatial;
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if(tree != null) {
			
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
		
	}
}
