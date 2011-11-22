/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 *
 * @author jo_da12
 */
public class World extends AbstractAppState {
    
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
//    private TerrainQuad terrain;
    private AssetManager assetManager;
    private Node rootNode;
    private SimpleApplication app;
    Material mat_terrain;
    Spatial gameLevel;
            
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        
//        this.stateManager = stateManager;
        this.app = (SimpleApplication)app;
        this.assetManager = this.app.getAssetManager();
        this.bulletAppState = this.app.getStateManager().getState(BulletAppState.class);
    }

    
    public World (Node rootNode) {
        this.rootNode = rootNode;
    }

    public void loadTerrain() {
        
        // sky
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        
//        assetManager.registerLoader(AWTLoader.class, "png");
//        assetManager.registerLocator( new File(“.”).getCanonicalPath(), FileLocator.class);
        
        /** 1. Create terrain material and load four textures into it. */
//        mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        
        gameLevel = assetManager.loadModel("Scenes/terrain/terrain.j3o");
        gameLevel.setLocalTranslation(0, -5.2f, 0);
        gameLevel.setLocalScale(1);
       
 
    }
    
    public void attachLights() {
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        AmbientLight amb = new AmbientLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));

        
        rootNode.addLight(sun);
        rootNode.addLight(amb);        
    }
    
    public void attachTerrain() {
        rootNode.attachChild(gameLevel);

    }
    
    public void initWorldPhysics() {
        
        /** 6. Add physics: */ 
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.*/
        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape((Node) gameLevel);
        landscape = new RigidBodyControl(terrainShape, 0);
        gameLevel.addControl(landscape);

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        bulletAppState.getPhysicsSpace().add(gameLevel);
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