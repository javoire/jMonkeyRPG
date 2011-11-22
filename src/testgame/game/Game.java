/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.concurrent.Callable;

/**
 *
 * @author jo_da12
 */
public class Game extends AbstractAppState {
    
    private World               world;
    private Gui                 gui;
    private Player              player;
        
    private boolean             running;
        
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        
        world           = app.getStateManager().getState(World.class);
        gui             = app.getStateManager().getState(Gui.class);
        player          = app.getStateManager().getState(Player.class);
    }
    
    public boolean startGame() {
        
        if(running)
            return false;
        
        running = true;
        
        initWorld();
        
        return running;
    }
    
    public void loadResources() { 
        world.loadTerrain();
        world.initWorldPhysics();

        gui.initGui();
               
        player.initPlayer();
        
//        world.initPostEffects();
        world.initSound();
    }
    
    public void initWorld() {
        // Load
        loadResources();
        
        // attach to space
        world.attachLights();
        world.attachTerrain();
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
