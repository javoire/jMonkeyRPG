/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import testgame.inventory.Inventory;
import testgame.items.Item;
import testgame.items.resources.Wood;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author jo_da12
 */
public class Game extends AbstractAppState {
    
    private World               world;
    private BasicGui            basicGui;
    private Player              player;
    private Inventory			inventory;
        
    private boolean             running;
        
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        
        world           = app.getStateManager().getState(World.class);
        basicGui        = app.getStateManager().getState(BasicGui.class);
        player          = app.getStateManager().getState(Player.class);
        inventory		= app.getStateManager().getState(Inventory.class);

        inventory.add(new Item("Item1"));
        inventory.add(new Item("Item2"));
        inventory.add(new Wood());
    }
    
    public boolean startGame() {
        
        if(running) // make sure this method only runs once
            return false;
        
        running = true;
        
        loadWorld();
        
        return running;
    }
    
    public void loadWorld() { 
    	/* WORLD */
        world.loadTerrain();
        world.loadTrees();
        world.loadLights();
        world.loadSky();
        world.loadShadows();
        world.initWorldPhysics();
        world.initPostEffects();
        world.initSound();

        /* GUI */
        basicGui.initGui();
        basicGui.initCrosshair();
        basicGui.initInventory(inventory);
        
        /* PLAYER */
        player.initPlayer();
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
