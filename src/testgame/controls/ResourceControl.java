package testgame.controls;

import testgame.items.resources.Resource;
import testgame.items.resources.Resource.ResourceType;

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
public class ResourceControl extends AbstractControl {
	
	private int quantity;
	private int minHarvestableDistance;
	private ResourceType resourceType;
	private Resource resource;
	
	/**
	 * @param camera
	 * @param _resourceType Which type of resource this Spatial has
	 */
	public ResourceControl(ResourceType _resourceType) {
//	public ResourceControl() {
		resourceType 			= _resourceType;
		resource 				= new Resource(_resourceType); 	// TODO dummy object just helps to get
													// the name of the resource. fix. freaking ugly
		quantity 				= 200;
		minHarvestableDistance 	= 1; // in meters
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if(spatial != null) {
//			if(quantity < 300)
//				quantity += 1*tpf;
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

	/**
	 * Takes a specified amount and send it to inventory
	 * @param harvestAmount
	 * @param inventory
	 * @param type
	 */
//	public void toInventory(int harvestAmount, Inventory inventory, ResourceType type) {
//		if(quantity > 0) {
//			Resource res = inventory.getResource(type);
//			if(res != null)
//				res.addQuantity(harvestAmount);
//			else {
//				res = new Resource(type);
//				res.setQuantity(harvestAmount);
//				inventory.add(res);
//			}
//			quantity -= harvestAmount;
//		}		
//	}

	public ResourceType getResourceType() {
		return resourceType;
	}
	
	public String getResourceName() {
		return resource.getName();
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public int getMinHarvestableDistance() {
		return minHarvestableDistance;
	}

	/**
	 * Sets minimum distant you can harvet from
	 * @param distance In meters
	 */
	public void setMinHarvestableDistance(int distance) {
		this.minHarvestableDistance = distance;
	}
}
