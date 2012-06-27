package testgame.appstates;

import testgame.game.BasicGui;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;

public class HarvestingAppState extends AbstractAppState implements
		ActionListener {

	private boolean harvestIsPossible;
	private Node harvestableNode;
	private SimpleApplication app;
	private BasicGui gui;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app; // cast to a more specific class
		this.gui = app.getStateManager().getState(BasicGui.class);
	}

	@Override
	public void update(float tpf) {
//		if(harvestIsPossible)
//			gui.displayHarvestingInformation(harvestableNode);
//		else
//			gui.clearHarvestingInformation();
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {

	}

	public void setHarvestableNode(Node node) {
		harvestableNode = node;
//		if(node != null)
//			harvestIsPossible = true;
//		else
//			harvestIsPossible = false;
	}

	public Node getHarvestableNode() {
		return harvestableNode;
	}
}
