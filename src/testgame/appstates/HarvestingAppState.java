package testgame.appstates;

import testgame.controls.HarvestingControl;
import testgame.inventory.Inventory;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;

public class HarvestingAppState extends AbstractAppState implements
		ActionListener {

	private TargetInfo targetInfo;
	private int harvestAmount;
	private Inventory inventory;
	private Application app;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app		= app; // cast to a more specific class
		targetInfo 		= app.getStateManager().getState(TargetInfo.class);
		inventory 		= app.getStateManager().getState(Inventory.class);
		harvestAmount = 5; // denna ska räknas ut baserat på typ/kvalité av resource, kvalite av verktyg etc
	}

	@Override
	public void update(float tpf) {

	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {

	}

    /**
     * Checks if we have a target and if it's close enough. 
     * If that's the case we may gather resources.
     */
	public void tryHarvest() {
		if (targetInfo.isHarvestable()) {
			Node harvestNode = targetInfo.getNode();
			HarvestingControl harvestControl = harvestNode
					.getControl(HarvestingControl.class);
			if (targetInfo.getIntDistance() < harvestControl
					.getHarvestableDistance()) {
				harvestControl.subtractFromAmount(harvestAmount);
				// add wood to inventory
				// if wood exists
				// add quantity
			}
		}
	}
}