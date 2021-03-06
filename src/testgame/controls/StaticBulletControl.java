package testgame.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;


/**
 * This class controls the behavious of bullets (eg arrows) that have been attached to the world, been shot at a tree etc.
 * It controls the lifetime until it disappears and also the posibility for the player to recover it. 
 * @author Jonatan Dahl
 *
 */
public class StaticBulletControl extends AbstractControl {

	private float lifeTime = 30.0f; // live 30 seconds
	private float timer = 0f;
	
	@Override
	protected void controlUpdate(float tpf) {
		timer+=tpf;
		if(timer>lifeTime) {
			spatial.removeFromParent(); // remove it from world..
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}
}
