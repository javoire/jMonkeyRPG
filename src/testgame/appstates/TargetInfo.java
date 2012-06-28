package testgame.appstates;

import testgame.controls.HarvestingControl;

import com.jme3.app.state.AbstractAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class TargetInfo extends AbstractAppState {
	
	private CollisionResult result = null;
	private Geometry targetGeom;
	private Node targetNode;
	private Float distance;
	private String name;
	private HarvestingControl harvestingControl = null;
	
	public TargetInfo() {
		
	}

//	public TargetInfo(CollisionResult collisionResult) {
//		result = collisionResult;
//		targetGeom = collisionResult.getGeometry();
//		targetNode = targetGeom.getParent();
//	}
//	
	public void setResults(CollisionResult collisionResult) {
		result = collisionResult;
		targetGeom = collisionResult.getGeometry();
		targetNode = targetGeom.getParent();
	}
	
	public boolean hasTarget() {
		if(result != null) { return true; }
		return false;
	}

	public String getName() {
		name = targetNode.getName();
		return name;
	}

	public float getDistance() {
		distance = result.getDistance();
		return distance/10;
	}

	public int getIntDistance() {
		distance = result.getDistance();
		int distanceInt = distance.intValue();
		return distanceInt/10;
	}

	public boolean isHarvestable() {
		// TODO kolla om den har en harvester controller
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
		// TODO fixa den strängen som ska visas i target info. med lr utan harvest info t.ex
		String infoString = getName()
				+ ": "
				+ getIntDistance()
				+ " m";
		if(isHarvestable())// lägg på harvest info
			infoString += "\n" + harvestingControl.getType() + ": " + harvestingControl.getAmount();
		if(!infoString.equals(""))
			return infoString;
		return null;
	}

}
