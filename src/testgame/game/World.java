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

    public void initTerrain() {
        
        // sky
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        
//        assetManager.registerLoader(AWTLoader.class, "png");
//        assetManager.registerLocator( new File(“.”).getCanonicalPath(), FileLocator.class);
        
        /** 1. Create terrain material and load four textures into it. */
//        mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        
        gameLevel = assetManager.loadModel("Scenes/terrain/terrain.j3o");
        gameLevel.setLocalTranslation(0, -5.2f, 0);
        gameLevel.setLocalScale(1);
        rootNode.attachChild(gameLevel);
        
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        AmbientLight amb = new AmbientLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
        rootNode.addLight(amb);


//        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
//        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
//        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
//        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
////        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/heightmap.png");
//
//        /** 1.2) Add GRASS texture into the red layer (Tex1). */
//        /** 1.3) Add DIRT texture into the green layer (Tex2) */
//        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
//        grass.setWrap(WrapMode.Repeat);
//        dirt.setWrap(WrapMode.Repeat);
//        rock.setWrap(WrapMode.Repeat);
//
//        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
//        mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
//        mat_terrain.setTexture("Tex1", grass);
//        mat_terrain.setTexture("Tex2", dirt);
//        mat_terrain.setTexture("Tex3", rock);
//
//        mat_terrain.setFloat("Tex1Scale", 64f);
//        mat_terrain.setFloat("Tex2Scale", 32f);
//        mat_terrain.setFloat("Tex3Scale", 128f);
//
//        /** 2. Create the height map */
//        AbstractHeightMap heightmap = null;
//        heightmap = new ImageBasedHeightMap(
//        ImageToAwt.convert(heightMapImage.getImage(), false, true, 0));
//
//        heightmap.load();

        /** 3. We have prepared material and heightmap. 
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
//        int patchSize = 65;
//        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());

        /** 4. We give the terrain its material, position & scale it, and attach it. */
//        terrain.setMaterial(mat_terrain);
//        terrain.setLocalTranslation(0, -100, 0);
//        terrain.setLocalScale(2f, 1f, 2f);
//        rootNode.attachChild(terrain);


        /** 5. The LOD (level of detail) depends on were the camera is: */
//        List<Camera> cameras = new ArrayList<Camera>();
//        cameras.add(app.getCamera());
//        TerrainLodControl control = new TerrainLodControl(gameLevel, cameras);
//        gameLevel.addControl(control); 
 
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