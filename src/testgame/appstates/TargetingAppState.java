package testgame.appstates;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.controls.ResourceControl;
import testgame.game.World;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class TargetingAppState extends AbstractAppState {
	
	private static final Logger logger = Logger.getLogger(TargetingAppState.class.getName());
	
	private CollisionResult 	collisionResult = null;
	private Geometry 			targetedGeom = null;
	private Node 				targetedNode = null;
	private Node 				rootNode;
	private Float 				distance = -1f;
	private String 				name = null;
	private ResourceControl 	harvestingControl = null;
	private World 				world;
	private Node 				targetables;
	private Camera				cam;
	private float 				maxTargetingRange = 60;
	
	public TargetingAppState(Node _rootNode) {
		this.rootNode = _rootNode;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		world			= app.getStateManager().getState(World.class);
		cam				= app.getCamera();
		targetables		= world.getTargetables(); // TODO temp. kan bara targeta trees nu
	}
	
	/**
	 * Constanly look for target. If non, result = null
	 */
	@Override
	public void update(float tpf) {
		scanForTarget();
	}

	private void scanForTarget() {
		CollisionResults results = new CollisionResults();
		Ray ray = new Ray(cam.getLocation(), cam.getDirection());
		rootNode.collideWith(ray, results);
		if(results != null && results.size() > 0) {
			if(results.getClosestCollision().getDistance() < maxTargetingRange) {
				if(results.size() > 0) {
					setCollisionResult(results.getClosestCollision());
					setTargetedGeometry(results.getClosestCollision().getGeometry());
					setTargetedNode(results.getClosestCollision().getGeometry().getParent());
				}
			}
		}
	}

	public void setCollisionResult(CollisionResult _collisionResult) {
		collisionResult = _collisionResult;
	}

	public void setTargetedGeometry(Geometry _geometry) {
		targetedGeom = _geometry;
	}

	public void setTargetedNode(Node _node) {
		targetedNode = _node;
	}
	
	public Geometry getTargetedGeometry() {
		return targetedGeom;
	}
	
	public Node getTargetedNode() {
		return targetedNode;
	}
	
	public boolean hasTarget() {
		if(collisionResult == null) return false;
		return true;
	}

	public String getName() {
		if(!hasTarget())
			return null;
		name = targetedNode.getName();
		return name;
	}

	public float getDistance() {
		if(!hasTarget())
			return -1;
		distance = collisionResult.getDistance();
		return distance/10;
	}

	public int getIntDistance() {
		if(!hasTarget())
			return -1;
		distance = getDistance();
		int distanceInt = distance.intValue();
		return distanceInt;
	}

	/**
	 * Checks if it has HarvestingControl attached. Also populates the field harvestingControl
	 * @return True or false
	 */
	public boolean isHarvestable() {
		if(collisionResult == null)
			return false;
		harvestingControl = targetedNode.getControl(ResourceControl.class);
		if(harvestingControl != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public Node getNode() {
		return targetedNode;
	}

	public void setNode(Node targetNode) {
		this.targetedNode = targetNode;
	}
	
	public String getTargetString() {
		String infoString = getName()
				+ ": "
				+ getIntDistance()
				+ " m";
		if(isHarvestable()) // add harvest info
			infoString += "\n" + harvestingControl.getResourceName() + ": " + harvestingControl.getQuantity();
		if(!infoString.equals(""))
			return infoString;
		return null;
	}
}
