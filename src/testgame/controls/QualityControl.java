package testgame.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class QualityControl  extends AbstractControl {
	
	private float quality = 1;

	public QualityControl() {
		
	}

	public QualityControl(float _quality) {
		quality = _quality;
	}

	@Override
	protected void controlUpdate(float tpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @return {@link float} quality
	 */
	public float getQuality() {
		return quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}
	
}
