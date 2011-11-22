/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 *
 * @author jo_da12
 */
public class World extends AbstractAppState {
    
    private BulletAppState      bulletAppState;
    private RigidBodyControl    landscape;
    private AssetManager        assetManager;
    private Node                rootNode;
    private FilterPostProcessor fpp;
    private WaterFilter         water;
    private Vector3f            lightDir = new Vector3f(-4.9f, -1.3f, 5.9f); // same as light source
    private Spatial             gameLevel;
    private ViewPort            viewPort;
    
    private float               initialWaterHeight = -9f; // choose a value for your scene    
            
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        
        assetManager = app.getAssetManager();
        bulletAppState = app.getStateManager().getState(BulletAppState.class);
        viewPort = app.getViewPort();
    }

    
    public World (Node rootNode) {
        this.rootNode = rootNode;
    }

    public void loadTerrain() {
        gameLevel = assetManager.loadModel("Scenes/terrain/terrain.j3o");
        gameLevel.setLocalTranslation(0, -5.2f, 0);
        gameLevel.setLocalScale(1); 
        
        fpp     = new FilterPostProcessor(assetManager);
        water   = new WaterFilter(rootNode, lightDir);
        water.setWaterHeight(initialWaterHeight);
        fpp.addFilter(water);
    }
    
    public void attachLights() {
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        AmbientLight amb = new AmbientLight();
//        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        sun.setDirection(new Vector3f(-0.24319214f, -0.60267837f, 0.94856685f));
        
        rootNode.addLight(sun);
        rootNode.addLight(amb);
//        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false));
    }
    
    public void loadPostEffects() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        BloomFilter bloom       = new BloomFilter();
        
        bloom.setBloomIntensity(0.05f);
        bloom.setBlurScale(0.4f);
        bloom.setExposurePower(2f);
        bloom.setDownSamplingFactor(1f);
        
        fpp.addFilter(bloom);
        
        viewPort.addProcessor(fpp);
    }
    
    public void attachTerrain() {
        rootNode.attachChild(gameLevel);
        viewPort.addProcessor(fpp);
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