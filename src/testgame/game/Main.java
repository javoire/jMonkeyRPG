package testgame.game;
 
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.terrain.geomipmap.TerrainQuad;
 
public class Main extends SimpleApplication {
 
    private TerrainQuad terrain;
    private World world;
    private Game game;
    Material mat_terrain;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        world = new World(this, rootNode);
        game = new Game();

        stateManager.attach(world);
        stateManager.attach(game);
        
//        game.startGame();
        
        world.initTerrain();
        
        initPlayer();
        initPhysics();

        flyCam.setMoveSpeed(50);

    }

    private void initPlayer() {
        
    }

    private void initPhysics() {
        
    }
}