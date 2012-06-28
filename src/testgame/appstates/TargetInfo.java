package testgame.appstates;

import testgame.controls.HarvestingControl;
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

public class TargetInfo extends AbstractAppState {
	
	private CollisionResult result = null;
	private Geometry targetGeom = null;
	private Node targetNode = null;
	private Float distance = -1f;
	private String name = null;
	private HarvestingControl harvestingControl = null;
	private World world;
	private Node targetables;
	private Camera cam;
	private float maxTargetingRange = 60;
	
	public TargetInfo() {
		
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
		if(targetables != null) {
			CollisionResults results = new CollisionResults();
			Ray ray = new Ray(cam.getLocation(), cam.getDirection());
			targetables.collideWith(ray, results); // checks all targetable objects
			if(results != null && results.size() > 0) {
				if(results.getClosestCollision().getDistance() < maxTargetingRange) {
					if(results.size() > 0) {
						setResult(results.getClosestCollision());
					} else { result = null;	}
				} else { result = null; }
			} else { result = null;	}
		} else { result = null; }
	}

	public void setResult(CollisionResult collisionResult) {
		result = collisionResult;
		targetGeom = collisionResult.getGeometry();
		targetNode = targetGeom.getParent();
	}
	
	public boolean hasTarget() {
		if(result != null) return true;
		return false;
	}

	public String getName() {
		if(!hasTarget())
			return null;
		name = targetNode.getName();
		return name;
	}

	public float getDistance() {
		if(!hasTarget())
			return -1;
		distance = result.getDistance();
		return distance/10;
	}

	public int getIntDistance() {
		if(!hasTarget())
			return -1;
		distance = getDistance();
		int distanceInt = distance.intValue();
		return distanceInt;
	}

	public boolean isHarvestable() {
		harvestingControl  = targetNode.getControl(HarvestingControl.class);
		if(harvestingControl != null)
			return true;
		else
			return false;
	}

	public Node getNode() {
		return targetNode;
	}

	public void setNode(Node targetNode) {
		this.targetNode = targetNode;
	}
	
	public String getString() {
		String infoString = getName()
				+ ": "
				+ getIntDistance()
				+ " m";
		if(isHarvestable()) // add harvest info
			infoString += "\n" + harvestingControl.getType() + ": " + harvestingControl.getAmount();
		if(!infoString.equals(""))
			return infoString;
		return null;
	}
}
