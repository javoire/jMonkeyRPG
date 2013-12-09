/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.player.Player;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainPatch;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 *
 * @author jo_da12
 */
public class World extends AbstractAppState {

	private static final Logger logger = Logger.getLogger(World.class.getName());

    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private Node rootNode;
    private WaterFilter water;
    private ViewPort viewPort;
    private AudioRenderer audioRenderer;
    private Camera camera;
    private Material terrainMaterial;
    private Player player;
    private DirectionalLight sun;
    private FilterPostProcessor fpp;
//    private Vector3f lightDir = new Vector3f(0.9f, -0.3f, -1f); // same as light source
    private Vector3f lightDir = new Vector3f(-0.6499433f, -0.49053365f, 0.58047426f); // same as light source
//    private Vector3f lightDir = new Vector3f(0.5f, -0.6f, 0.3f); // same as light source
    private float initialWaterHeight = 0; // choose a value for your scene
    private Node trees;
    private InputManager inputManager;
    boolean wireframe = false;
    private Node harvestables;
    private Node targetables;
    Spatial scene;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        // TODO: initialize your AppState, e.g. attach spatials to rootNode
        // this is called on the OpenGL thread after the AppState has been
        // attached
        
        assetManager = app.getAssetManager();
        bulletAppState = app.getStateManager().getState(BulletAppState.class);
        viewPort = app.getViewPort();
        audioRenderer = app.getAudioRenderer();
        camera = app.getCamera();
        inputManager = app.getInputManager();
        player = app.getStateManager().getState(Player.class);
        fpp = new FilterPostProcessor(assetManager);
        targetables = new Node("targetables");
        harvestables = new Node("harvestables");
        terrainMaterial = assetManager.loadMaterial("Materials/terrain.j3m");
        targetables.attachChild(harvestables);
        rootNode.attachChild(targetables);
    }

    public void init() {
        buildTerrain();
        loadLights();
        loadSky();
//        loadShadows();
        initWorldPhysics();
        initPostEffects();
        initSound();

        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(actionListener, "wireframe");
    }

    public World(Node rootNode) {
        this.rootNode = rootNode;
    }

    public void loadGrass() {
//		http://jmonkeyengine.org/wiki/doku.php/jme3:contributions:vegetationsystem:grass
//		http://jmonkeyengine.org/wiki/doku.php/jme3:contributions:vegetationsystem:trees
        //Step 1 - set up the forester.
//		forester = Forester.getInstance();
//		forester.initialize(rootNode, camera, terrain, app);
//		 
//		//Step 2 - set up the grassloader.
//		GrassLoader grassLoader = forester.createGrassLoader(512,4,250f, 20f);
//		 
//		//Step 3 - set up the datagrid (using the default uniform distribution provider here).
//		UDGrassProvider ud = grassLoader.createUDProvider();
//		ud.setBounds(-1, 1, -1, 1);
//		 
//		 
//		//Step 4 - set up a grass layer
//		 
//		Material grassMat = assetManager.loadMaterial("Grass.j3m");
//		 
//		GrassLayer layer = grassLoader.addLayer(grassMat,MeshType.CROSSQUADS);
//		 
//		layer.setLocalDensityFactor(0.1f);
//		layer.setMinHeight(2.f);
//		layer.setMaxHeight(2.4f);
//		layer.setMinWidth(2.f);
//		layer.setMaxWidth(2.4f);
    }

//    public void loadShadows() {
//    	DirectionalLightShadowFilter shadowRenderer;
//        shadowRenderer = new DirectionalLightShadowFilter(assetManager, 1024, 3);
//        shadowRenderer.setLight(sun);
//        shadowRenderer.setShadowIntensity(0.4f);
//        shadowRenderer.setEdgesThickness(0);
//        shadowRenderer.setShadowZExtend(500);
//        fpp = new FilterPostProcessor(assetManager);
//        fpp.addFilter(shadowRenderer);
//        viewPort.addProcessor(fpp);
//    }
    
    public void buildTerrain() {
        
    	// Parse the blender scene and assign materials
        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
            	if(!(spatial instanceof TerrainPatch)) {
            		logger.log(Level.INFO,"Instance of: " + spatial.getClass().getName()
            				+ " - Name: " + spatial.getName() 
            				+ " - Parent: " + spatial.getParent());            		
            	}
                // search criterion can be control class:
//                System.out.println("instance of: " + spatial.getClass().getName() + " name: " +  spatial.getName());
//                MyControl control = spatial.getControl(MyControl.class);
//				if (spatial instanceof Node) {
//					logger.log(Level.INFO,"Instance of: " + spatial.getClass().getName()
//							+ " - Name: " + spatial.getName() 
//							+ " - Parent: " + spatial.getParent());
//
//                    if (spatial.getName().matches("stem(.*)")) {
//                        spatial.setMaterial(assetManager.loadMaterial("Materials/tree/stam.j3m"));
//                        ResourceControl woodHarvester = new ResourceControl(Resource.ResourceType.WOOD);
//                        woodHarvester.setQuantity(200);
//                        spatial.addControl(woodHarvester);
//                    }
//                    if (spatial.getName().matches("leaves(.*)")) {
//                        spatial.setMaterial(assetManager.loadMaterial("Materials/tree/leaf.j3m"));
////                        ResourceControl woodHarvester = new ResourceControl(Resource.ResourceType.WOOD);
////                        woodHarvester.setQuantity(200);
////                        spatial.addControl(woodHarvester);
//                    }
////                    if (spatial.getParent() != null && spatial.getParent().getName().equals("trees")) {
////                    }        
//
//                    if (spatial.getParent() != null && spatial.getParent().getName().equals("rocks")) {
//                        spatial.setMaterial(assetManager.loadMaterial("Materials/rock.j3m"));
//                    }
//                    if (spatial.getName().equals("ground")) {
//                        spatial.setMaterial(terrainMaterial);
//                    }
//                    // get water Y position
//                    if (spatial.getName().equals("water")) {
//                        initialWaterHeight = spatial.getWorldTranslation().getY();
//                        logger.log(Level.INFO,"scene parsing water height: " + Float.toString(initialWaterHeight));
//                        spatial.removeFromParent();
//                    }
//                    if (spatial.getName().equals("player")) {
//                         player.getPlayerControl().setPhysicsLocation(spatial.getWorldTranslation());
//                    }
//                }
            }
        };
        
//        String blenderTerrainFilePath = "Scenes/terrain/terrain_2013_12_10.blend";
//        logger.log(Level.INFO,"Parsing blender terrain file: " + blenderTerrainFilePath);
//    	scene = assetManager.loadModel(blenderTerrainFilePath);
    	scene = assetManager.loadModel("Scenes/simpleScene.j3o");
        scene.depthFirstTraversal(visitor);
        scene.scale(1f);
//        scene.setShadowMode(ShadowMode.CastAndReceive);
        rootNode.attachChild(scene);

        // make everything in the scene collidable
        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape(scene);
        RigidBodyControl landscape = new RigidBodyControl(terrainShape, 0);
        scene.addControl(landscape);

        bulletAppState.getPhysicsSpace().addAll(scene);

        //		/** 5. The LOD (level of detail) depends on were the camera is: */
//        TerrainLodControl lodControl = new TerrainLodControl(scene, camera);
//        lodControl.setLodCalculator(new DistanceLodCalculator(32, 3.1f));
//        scene.addControl(lodControl);

//		/** 2. Create the height map */
//		AbstractHeightMap heightmap = null;
////		Texture heightMapImage = assetManager.loadTexture("Models/terrain/heightmap.png");
//		Texture heightMapImage = assetManager.loadTexture("Models/terrain/heightmap_256.png");
//		heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
//		heightmap.setHeightScale(60);
//		heightmap.load();
//		heightmap.smooth(1f, 2);
//
//		/**
//		 * 3. We have prepared material and heightmap. Now we create the actual
//		 * terrain: 3.1) Create a TerrainQuad and name it "my terrain". 3.2) A
//		 * good value for terrain tiles is 64x64 -- so we supply 64+1=65. 3.3)
//		 * We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
//		 * 3.4) As LOD step scale we supply Vector3f(1,1,1). 3.5) We supply the
//		 * prepared heightmap itself.
//		 */
//		int patchSize = 33;
//		terrain = new TerrainQuad("my terrain", patchSize, 257, heightmap.getHeightMap());
//		terrainMaterial = assetManager.loadMaterial("Materials/terrain.j3m");
////		terrainMaterial.getAdditionalRenderState().setWireframe(true); // debug
//		terrain.setMaterial(terrainMaterial);
//		terrain.setLocalTranslation(0, -30.5f, 0);
//		terrain.setLocalScale(2f, 1f, 2f);
//
////		rotation buggggggar slopes, man g���r l���ngasmt
//		//		Vector3f axis = Vector3f.UNIT_Y; // this equals (0, 1, 0) and does not require to create a new object
////		float angle = -3.14f/4;
////		Quaternion rootRotation = rootNode.getLocalRotation().fromAngleAxis(angle, axis);
////		terrain.setLocalRotation(rootRotation);
//
//		root.attachChild(terrain);
//		
//		terrain.setShadowMode(ShadowMode.Receive);
//

    }

    public void loadSky() {
//        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false));
    }

    public void loadLights() {
//        sun = new DirectionalLight();
//        AmbientLight amb = new AmbientLight();
//        ColorRGBA sunColor = new ColorRGBA(0.9f, 0.77f, 0.52f, 1f);
////
//        sun.setDirection(lightDir);
//        sun.setColor(sunColor.mult(1.5f));
//        amb.setColor(sunColor.mult(1.5f));
//        
////
////        PointLight lamp_light = new PointLight();
////        lamp_light.setColor(ColorRGBA.Orange);
////        lamp_light.setRadius(100f);
////        lamp_light.setPosition(new Vector3f(0, 3, 0));
//////		rootNode.addLight(lamp_light);
////
//        rootNode.addLight(sun);
//        rootNode.addLight(amb);
    }

    public void initPostEffects() {
    	logger.log(Level.INFO,"Addin bloom effects");
        BloomFilter bloom = new BloomFilter();
        bloom.setBloomIntensity(0.2f);
        bloom.setBlurScale(0.4f);
        bloom.setExposurePower(2f);
        bloom.setDownSamplingFactor(5f);
        fpp.addFilter(bloom);

//        logger.log(Level.INFO,"post effects water height: " + Float.toString(initialWaterHeight));
//        water = new WaterFilter(rootNode, lightDir);
//        water.setWaterHeight(initialWaterHeight);
//        water.setReflectionMapSize(512);
//        water.setReflectionDisplace(2);
//        water.setFoamExistence(new Vector3f(0.45f, 4.35f, 1.5f));
//        water.setWaterTransparency(0.08f);
//        fpp.addFilter(water);
        
//        logger.log(Level.INFO, "Adding directional shadow filter");
//        DirectionalLightShadowFilter shadowFilter;
//        shadowFilter = new DirectionalLightShadowFilter(assetManager, 512, 3);
//        shadowFilter.setLight(sun);
//        shadowFilter.setLambda(20f);
//        shadowFilter.setShadowIntensity(0.4f);
//        shadowFilter.setEdgesThickness(2);
////        shadowRenderer.setShadowZExtend(500);
//        shadowFilter.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
//        fpp.addFilter(shadowFilter);

		// shadow renderer, heavy
//        logger.log(Level.INFO, "Adding directional shadow renderer");
//        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 3);
//		dlsr.setLight(sun);
//		dlsr.setLambda(3f);
//		dlsr.setShadowIntensity(0.3f);
//		dlsr.setEdgesThickness(3);
//		dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
//		viewPort.addProcessor(dlsr);
        
//        logger.log(Level.INFO, "Adding SSAO filter");
//        SSAOFilter ssaoFilter = new SSAOFilter(5f, 10f, 1f, 0.61f);
//        fpp.addFilter(ssaoFilter);
//        viewPort.addProcessor(fpp);
        
//        LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
//        lsf.setLightDensity(1.0f);
//        fpp.addFilter(lsf);
        
//         BloomFilter bloom = new BloomFilter();
//        //bloom.getE
//        bloom.setExposurePower(55);
//        bloom.setBloomIntensity(1.0f);
//        fpp.addFilter(bloom);

//        HDRRenderer hdrRender = new HDRRenderer(assetManager, renderer);
//        hdrRender.setSamples(0);
//        hdrRender.setMaxIterations(20);
//        hdrRender.setExposure(0.87f);
//        hdrRender.setThrottle(0.33f);
//
//        viewPort.addProcessor(hdrRender);
//
//        viewPort.addProcessor(hdrRender);

        /* light scattering */
//        Vector3f lightPos = lightDir.multLocal(-3000);
//        LightScatteringFilter lightFilter = new LightScatteringFilter(lightPos);
//        lightFilter.setLightDensity(0.9f);
//        lightFilter.setBlurWidth(2f);
//        lightFilter.setBlurStart(-0.2f);
//        fpp.addFilter(lightFilter);

        /* fog */
//        FogFilter fog = new FogFilter();
//        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
//        fog.setFogDistance(900);
//        fog.setFogDensity(1.0f);
//        fpp.addFilter(fog);
//
//        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 5f, 0.33f, 0.61f);
//        fpp.addFilter(ssaoFilter);
//        
//                SSAOFilter ssaoFilter = new SSAOFilter(12.940201f, 43.928635f, 0.32999992f, 0.6059958f);
//        fpp.addFilter(ssaoFilter);
//        SSAOUI ui = new SSAOUI(inputManager, ssaoFilter);

        viewPort.addProcessor(fpp);
    }

    public void initSound() {
        // AudioNode nature = new AudioNode(assetManager,
        // "Sound/Environment/Nature.ogg", false);
        AudioNode waves = new AudioNode(assetManager, "Sounds/Environment/Ocean Waves.ogg", false);

        waves.setLooping(true);
        waves.setVolume(0.01f);

        audioRenderer.playSource(waves);
    }

    public void initWorldPhysics() {
        /**
         * 6. Add physics:
         */
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass
        // zero.*/
//		CollisionShape terrainShape = CollisionShapeFactory.createMeshShape(terrain);
//		landscape = new RigidBodyControl(terrainShape, 0);
//		terrain.addControl(landscape);
//
//		// We attach the scene and the player to the rootnode and the physics
//		// space,
//		// to make them appear in the game world.
//		bulletAppState.getPhysicsSpace().add(terrain);
    }

    @Override
    public void update(float tpf) {
        // TODO: implement behavior during runtime
    }

    @Override
    public void cleanup() {
        super.cleanup();
        // TODO: clean up what you initialized in the initialize method,
        // e.g. remove all spatials from rootNode
        // this is called on the OpenGL thread after the AppState has been
        // detached
    }

    public Node getTrees() {
        return trees;
    }

    public void setTrees(Node trees) {
        this.trees = trees;
    }

    public Node getTargetables() {
        return targetables;
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("wireframe") && !pressed) {
                wireframe = !wireframe;
                if (!wireframe) {
                    terrainMaterial.getAdditionalRenderState().setWireframe(
                            true); // debug
                } else {
                    terrainMaterial.getAdditionalRenderState().setWireframe(
                            false); // debug
                }
            }
        }
    };
}