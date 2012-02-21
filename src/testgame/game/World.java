/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
//import com.jme3.scene.plugins.blender.BlenderLoader;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
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
    private WaterFilter         water;
    private TerrainQuad			terrain;
    private ViewPort            viewPort;
    private AudioRenderer       audioRenderer;
    private Camera              camera;
    private FilterPostProcessor fpp;
    private Vector3f            lightDir = new Vector3f(-0.74319214f, -0.50267837f, 0.84856685f); // same as light source
    private float               initialWaterHeight = 0; // choose a value for your scene    
            
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        
        assetManager    = app.getAssetManager();
        bulletAppState  = app.getStateManager().getState(BulletAppState.class);
        viewPort        = app.getViewPort();
        audioRenderer   = app.getAudioRenderer();
        camera          = app.getCamera();
        
        assetManager.registerLocator( "./assets/", FileLocator.class.getName() );
        
        fpp = new FilterPostProcessor(assetManager);;

    }

    
    public World (Node rootNode) {
        this.rootNode = rootNode;
    }

    public void loadTerrain() {        
        world_scene = assetManager.loadModel("Scenes/world/world.j3o");
    	
    	
    	 /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture("Models/terrain/heightmap.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
        heightmap.setHeightScale(128);
        heightmap.load();
        heightmap.smooth(1f, 2);
     
        /** 3. We have prepared material and heightmap. 
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());
     
        /** 4. We give the terrain its material, position & scale it, and attach it. */
        terrain.setMaterial(assetManager.loadMaterial("Materials/terrain.j3m"));
        terrain.setLocalTranslation(0, -56.5f, 0);
        terrain.setLocalScale(1f, 0.85f, 1f);
        rootNode.attachChild(terrain);
     
        /** 5. The LOD (level of detail) depends on were the camera is: */
        TerrainLodControl control = new TerrainLodControl(terrain, camera);
        terrain.addControl(control);

//              fpp     = new FilterPostProcessor(assetManager);
        
        water   = new WaterFilter(rootNode, lightDir);
        water.setWaterHeight(initialWaterHeight);
        water.setReflectionMapSize(128);
        fpp.addFilter(water);
    }
    
    public void attachLights() {
        DirectionalLight sun = new DirectionalLight();
        AmbientLight amb = new AmbientLight();
        sun.setDirection(lightDir);
        amb.setColor(new ColorRGBA(1, 1, 0.6f, 1));
        
        rootNode.addLight(sun);
        rootNode.addLight(amb);
//        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false));
    }
    
    public void initPostEffects() {
        BloomFilter bloom       = new BloomFilter();
        
        bloom.setBloomIntensity(0.05f);
        bloom.setBlurScale(0.4f);
        bloom.setExposurePower(2f);
        bloom.setDownSamplingFactor(5f);
        
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
    }
    
    public void initSound() {
//        AudioNode nature = new AudioNode(assetManager, "Sound/Environment/Nature.ogg", false);
        AudioNode waves = new AudioNode(assetManager, "Sound/Environment/Ocean Waves.ogg", false);
        
        waves.setLooping(true);
        waves.setVolume(0.05f);
        
        audioRenderer.playSource(waves);
    }
    
    public void attachTerrain() {
//        rootNode.attachChild(world_scene);
        viewPort.addProcessor(fpp);
    }
    
    public void initWorldPhysics() {
        
        /** 6. Add physics: */ 
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.*/
        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape((Node) terrain);
        landscape = new RigidBodyControl(terrainShape, 0);
        terrain.addControl(landscape);

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        bulletAppState.getPhysicsSpace().add(terrain);
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