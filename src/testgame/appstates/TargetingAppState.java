package testgame.appstates;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.controls.QualityControl;
import testgame.controls.ResourceControl;
import testgame.controls.TargetableControl;
import testgame.target.TargetInformation;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 * Checks where the player is looking if there's a valid target
 * @author Jonatan Dahl
 */
public class TargetingAppState extends AbstractAppState {
	
	private static final Logger logger = Logger.getLogger(TargetingAppState.class.getName());
	
	private CollisionResult 	closestCollision = null;
	private Node 				rootNode;
	private Camera				cam;
	private TargetInformation	targetInformation;
	private CollisionResults 	collisionResults;
	private Ray 				ray; 
	private Node				targetedNode;
	
	public TargetingAppState(Node _rootNode) {
		this.rootNode = _rootNode;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		cam					= app.getCamera();
		targetInformation 	= new TargetInformation();
		collisionResults	= new CollisionResults();
		ray 				= new Ray();
	}
	
	/**
	 * Populating collisionResult with the closest target
	 */
	@Override
	public void update(float tpf) {
		// align ray to where player is looking
		ray.setOrigin(cam.getLocation());
		ray.setDirection(cam.getDirection());
		// clear old results
		collisionResults.clear();
		// get collision results
		rootNode.collideWith(ray, collisionResults);
		// 
		if(collisionResults.size() > 0) {
			targetedNode = collisionResults.getClosestCollision().getGeometry().getParent();
			if(targetedNode != null) {
				if(targetedNode.getControl(TargetableControl.class) != null) {
					targetInformation.clearControls(); // clear old info
					targetInformation.setDistance(collisionResults.getClosestCollision().getDistance());
					// add the controls that this node has to targetInformation
					if(targetedNode.getControl(ResourceControl.class) != null) {
						targetInformation.addControl(targetedNode.getControl(ResourceControl.class));						
					}
					if(targetedNode.getControl(QualityControl.class) != null) {
						targetInformation.addControl(targetedNode.getControl(QualityControl.class));						
					}
					logger.log(Level.INFO, 
						"This node is targetable: " + 
						collisionResults.getClosestCollision().getGeometry().getParent().toString() + " distance" + 
						targetInformation.getDistance() + " controls: " +
						targetInformation.getControls().size()
					);
				}
			}
		}
	}
		
	/**
	 * Returns info about the target, if the player is looking at a valid target, i.e a natural resource or something that can be interacted with.
	 * @return
	 */
	public TargetInformation getTargetInformation() {
		return targetInformation;
	}


//	public String getName() {
//		if(!hasTarget())
//			return null;
//		name = targetedNode.getName();
//		return name;
//	}

//	public float getDistance() {
//		if(!hasTarget())
//			return -1;
//		distance = collisionResult.getDistance();
//		return distance/10;
//	}

//	public int getIntDistance() {
//		if(!hasTarget())
//			return -1;
//		distance = getDistance();
//		int distanceInt = distance.intValue();
//		return distanceInt;
//	}

	/**
	 * Checks if it has HarvestingControl attached. Also populates the field harvestingControl
	 * @return True or false
	 */
//	public boolean isHarvestable() {
//		if(collisionResult == null)
//			return false;
//		harvestingControl = targetedNode.getControl(ResourceControl.class);
//		if(harvestingControl != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
//	public String getTargetString() {
//		String infoString = getName()
//				+ ": "
//				+ getIntDistance()
//				+ " m";
//		if(isHarvestable()) // add harvest info
//			infoString += "\n" + harvestingControl.getResourceName() + ": " + harvestingControl.getQuantity();
//		if(!infoString.equals(""))
//			return infoString;
//		return null;
//	}
}
