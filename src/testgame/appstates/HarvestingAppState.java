package testgame.appstates;

import testgame.controls.HarvestingControl;
import testgame.inventory.Inventory;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;

public class HarvestingAppState extends AbstractAppState implements
		ActionListener {

	private TargetInfo 		targetInfo;
	private int 			harvestAmount;
	private Inventory 		inventory;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		targetInfo 		= app.getStateManager().getState(TargetInfo.class);
		inventory 		= app.getStateManager().getState(Inventory.class);
		harvestAmount 	= 5; // denna ska räknas ut baserat på typ/kvalité av resource, kvalite av verktyg etc
	}

	@Override
	public void update(float tpf) {

	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {

	}

    /**
     * Checks if we have a target and if it's close enough. 
     * Then checks what type of resource, then take and puts in inventory.
     */
	public void tryHarvest() {
		if (targetInfo.isHarvestable()) {
			HarvestingControl harvestControl = targetInfo.getNode().getControl(
					HarvestingControl.class);
			if (targetInfo.getIntDistance() < harvestControl
					.getHarvestableDistance()) {
				harvestControl.toInventory(harvestAmount, inventory,
						harvestControl.getResourceType());
			}
		}
	}
}