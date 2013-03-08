/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import testgame.appstates.HarvestingAppState;
import testgame.appstates.TargetInfo;
import testgame.gui.SimpleHud;
import testgame.inventory.Inventory;
import testgame.items.Weapon;
import testgame.items.Weapon.WeaponType;
import testgame.player.Player;
import testgame.player.PlayerActions;
import testgame.player.PlayerInput;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;

/**
 *
 * @author jo_da12
 */
public class Game extends AbstractAppState {
    
    private World               world;
    private SimpleHud          	simpleGui;
    private Player              player;
    private Inventory			inventory;
        
    private boolean             running;
        
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        
        world           = app.getStateManager().getState(World.class);
        simpleGui       = app.getStateManager().getState(SimpleHud.class);
        player          = app.getStateManager().getState(Player.class);
        inventory		= app.getStateManager().getState(Inventory.class);

        inventory.add(new Weapon(WeaponType.BOW, "TestItem"));
        inventory.add(new Weapon(WeaponType.SWORD, "TestItem"));
    }
    
    public boolean startGame() {
        
        if(running) // make sure this method only runs once
            return false;
        
        running = true;
        
        world.init();
        player.init();
        simpleGui.init();
        
        return running;
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
        
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

}
