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
        
//        if (load) {
//            if (loadFuture == null) {
//                //if we have not started loading yet, submit the Callable to the executor
//                loadFuture = exec.submit(loadingCallable);
//            }
//            //check if the execution on the other thread is done
//            if (loadFuture.isDone()) {
//                //these calls have to be done on the update loop thread, 
//                //especially attaching the terrain to the rootNode
//                //after it is attached, it's managed by the update loop thread 
//                // and may not be modified from any other thread anymore!
//                nifty.gotoScreen("end");
//                nifty.exit();
//                guiViewPort.removeProcessor(niftyDisplay);
//                flyCam.setEnabled(true);
//                flyCam.setMoveSpeed(50);
//                rootNode.attachChild(terrain);
//                load = false;
//            }
//        }
        
        
        if(running)
            return false;
        
        running = true;
        
        // Load
        loadResources();
        
        
        // attach to space
        world.attachLights();
        world.attachTerrain();

        
        return running;
    }
    
//    Callable<Void> loadResources = new Callable<Void>() {
// 
//        public Void call() {
//            
//            
//        }
//        
//    }
    
    public void loadResources() { 
        world.loadTerrain();
        world.initWorldPhysics();

        gui.initGui();
               
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
