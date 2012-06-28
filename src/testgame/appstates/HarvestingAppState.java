package testgame.appstates;

import testgame.controls.HarvestingControl;
import testgame.game.BasicGui;
import testgame.game.World;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class HarvestingAppState extends AbstractAppState implements
		ActionListener {

	private boolean harvestIsPossible;
	private Node harvestableNode;
	private SimpleApplication app;
	private BasicGui gui;
	private Camera cam;
	private Node trees;
	private World world;
	private TargetInfo targetInfo;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		app 	= (SimpleApplication) app; // cast to a more specific class
		gui 	= app.getStateManager().getState(BasicGui.class);
		cam 	= app.getCamera();
		world	= app.getStateManager().getState(World.class);
		targetInfo = app.getStateManager().getState(TargetInfo.class);
	}

	@Override
	public void update(float tpf) {

	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {

	}

//	public void setHarvestableNode(Node node) {
//		harvestableNode = node;
//		if(node != null)
//			harvestIsPossible = true;
//		else
//			harvestIsPossible = false;
//	}

//	public Node getHarvestableNode() {
//		return harvestableNode;
//	}

    /**
     * 
     * är vi tillr nära?
     * klicka höger -> få resources
     */
	public void tryHarvest() {
		Node harvestNode = targetInfo.getNode();
		HarvestingControl harvestControl = harvestNode.getControl(HarvestingControl.class);
		if (targetInfo.getIntDistance() < harvestControl.getMinDistance())
			harvestControl.subtractFromAmount(5);
	}
}