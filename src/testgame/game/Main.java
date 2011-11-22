package testgame.game;
 
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
 
public class Main extends SimpleApplication {
 
    private World           world;
    private Game            game;
    private Gui             gui;
    private Player          player;
    private BulletAppState  bulletAppState;    
//    private Material        mat_terrain;
    
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Awesome Game :D");
        
//        java.util.logging.Logger.getLogger("").setLevel(Level.WARNING);
        
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        world           = new World(rootNode);
        game            = new Game();
        gui             = new Gui(guiNode, guiFont, settings);
        player          = new Player();
        bulletAppState  = new BulletAppState();

        // attach all statemanagers
        stateManager.attach(bulletAppState);
        stateManager.attach(world);
        stateManager.attach(game);
        stateManager.attach(gui);
        stateManager.attach(player);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

        flyCam.setMoveSpeed(30);

    }
    

     
    
  @Override
    public void simpleUpdate(float tpf) {
        game.startGame(); // cannot be called in init

//        bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        
        // player controller
    }

}