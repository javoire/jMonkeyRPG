package testgame.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;

public class HarvestingAppState extends AbstractAppState implements
		ActionListener {

	private boolean isHarvesting;
	private SimpleApplication app;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app; // cast to a more specific class

	}

	@Override
	public void update(float tpf) {

	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {

	}
}
