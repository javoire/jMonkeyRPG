package testgame.controls;

import testgame.game.Player;
import testgame.inventory.Inventory;
import testgame.items.resources.Resource.ResourceType;
import testgame.items.resources.Resource;
import testgame.objects.Tree;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * This is the control that is attached to an in game Spatial. Such as a tree.
 * It holds some information about the type of harvesting that can be done.
 * @author Jonatan Dahl
 *
 */
public class HarvestingControl extends AbstractControl {
	
	Player player;
	Tree tree;
	private int quantity;
	private Camera cam;
	private String type;
	private int minDistance;
	private ResourceType resourceType;
	
	/**
	 * @param camera
	 * @param resType Which type of resource this Spatial has
	 */
	public HarvestingControl(Camera camera, ResourceType resType) {
		cam 			= camera;
		resourceType 	= resType;
		quantity 		= 200;
		type 			= "Wood";
		minDistance 	= 1; // in meters
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if(spatial != null) {

		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int amount) {
		this.quantity = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getHarvestableDistance() {
		return minDistance;
	}

	/**
	 * Takes a specified amount and send it to inventory
	 * @param harvestAmount
	 * @param inventory
	 * @param type
	 */
	public void toInventory(int harvestAmount, Inventory inventory, ResourceType type) {
		if(quantity > 0) {
			Resource res = inventory.getResource(type);
			if(res != null)
				res.addQuantity(harvestAmount);
			else {
				res = new Resource(type);
				res.setQuantity(harvestAmount);
				inventory.add(res);
			}
			quantity -= harvestAmount;
		}		
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
}
