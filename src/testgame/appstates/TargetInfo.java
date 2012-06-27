package testgame.appstates;

import com.jme3.app.state.AbstractAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.font.BitmapText;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class TargetInfo extends AbstractAppState {
	
	CollisionResult result;
	Geometry targetGeom;
	private Node targetNode;
	private Float distance;
	private String name;

	public TargetInfo(CollisionResult collisionResult) {
		result = collisionResult;
		targetGeom = collisionResult.getGeometry();
		targetNode = targetGeom.getParent();
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
		if(targetNode.getUserData("isHarvestable") != null)
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
			infoString += "\nAmount: " + targetNode.getUserData("amount");
		if(!infoString.equals(""))
			return infoString;
		return null;
	}
}
