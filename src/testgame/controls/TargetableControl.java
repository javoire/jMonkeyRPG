package testgame.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class TargetableControl extends AbstractControl {
	
	/**
	 * Name of target, shown to the player
	 */
	private String name;

	public TargetableControl(String _name) {
		setName(_name);
	}

	@Override
	protected void controlUpdate(float tpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
