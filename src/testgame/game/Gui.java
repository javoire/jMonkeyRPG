/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Gui extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private SimpleApplication app;

	/** custom methods */ 

	public Gui(String data) { 
		/** You custom constructor, can accept arguments */ 
	}

	public void startGame(String nextScreen) {
		nifty.gotoScreen(nextScreen);  // switch to another screen
		// start the game and do some more stuff...
	}

	/** Nifty GUI ScreenControl methods */ 

	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
		
	}
	
	public void onStartScreen() { }

	public void onEndScreen() { }

	/** jME3 AppState methods */ 

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app=(SimpleApplication)app;
	}

	@Override
	public void update(float tpf) { 
		/** jME update loop! */ 
	}

}