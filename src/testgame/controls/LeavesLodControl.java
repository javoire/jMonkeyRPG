/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.controls;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.terrain.Terrain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jo_da12
 */
public class LeavesLodControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
	
    private Spatial 	leaves;
    private Camera 		camera;
    private Vector3f	cameraLocation, leavesLocation;

	public LeavesLodControl(Spatial leaves, Camera camera) {
    	this.camera = camera;
    	this.leaves = leaves;
    }

    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
    	
    	// update lod level based on distance to camera
    	if (camera != null) {
    		cameraLocation = camera.getLocation();
    		leavesLocation = leaves.getWorldTranslation();

    		float distance = cameraLocation.distance(leavesLocation);
    		
//    		System.out.print(leavesLocation.x);
//    		System.out.print("\n");
    		
    		
    		if ( distance <= 160 ) {
    			leaves.setLodLevel(2);
    		}
//    		if ( distance > 70 && distance <= 90) {
//    			tree.setLodLevel(2);
//    		}
    		if ( distance > 160 && distance <= 200) {
    			leaves.setLodLevel(3);
    		}
    		if ( distance > 200 && distance <= 250) {
    			leaves.setLodLevel(3);
    		}
    		if ( distance > 250 && distance <= 350) {
    			leaves.setLodLevel(4);
    		}
    		if ( distance > 350 && distance <= 1000) {
    			leaves.setLodLevel(5);
    		}
    		if ( distance > 1000) {
    			leaves.setLodLevel(10);
    		}
    	}
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
    	
    	Camera cameraClone = camera;
    	
        LeavesLodControl control = new LeavesLodControl(spatial, cameraClone);
        //TODO: copy parameters to new Control
        control.setSpatial(spatial);
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
}
