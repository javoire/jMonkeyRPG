package testgame.game;
 
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
 
public class Main extends SimpleApplication {
 
    private World world;
    private Game game;
    private BulletAppState bulletAppState;    
    Material mat_terrain;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        world = new World(rootNode);
        game = new Game();
        bulletAppState = new BulletAppState();

        stateManager.attach(bulletAppState);
        stateManager.attach(world);
        stateManager.attach(game);

        flyCam.setMoveSpeed(50);

    }
    

     
    
  @Override
    public void simpleUpdate(float tpf) {
        game.startGame(inputManager); // cannot be called in init // inputManager exists thanks to simpleApplication

        bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        
        // player controller
    }

}