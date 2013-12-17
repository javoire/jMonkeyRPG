/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.world;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jme3utilities.sky.SkyControl;
import testgame.controls.QualityControl;
import testgame.controls.ResourceControl;
import testgame.controls.TargetableControl;
import testgame.items.resources.Resource.ResourceType;
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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.terrain.geomipmap.TerrainQuad;
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

	private SkyControl skyCtrl;

	private AmbientLight ambientLight;

	private SpotLight flashLight;

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
        
//        debugStuff();
    }
    
    private void debugStuff() {
//    	attachCoordinateAxes(new Vector3f(0f,0f,0f));
//    	attachGrid(new Vector3f(0f,100f,0f), 10, ColorRGBA.Red);
    	bulletAppState.setDebugEnabled(true);
    }

    private void attachCoordinateAxes(Vector3f pos){
		Arrow arrow = new Arrow(Vector3f.UNIT_X);
		arrow.setLineWidth(4); // make arrow thicker
		putShape(arrow, ColorRGBA.Red).setLocalTranslation(pos);

		arrow = new Arrow(Vector3f.UNIT_Y);
		arrow.setLineWidth(4); // make arrow thicker
		putShape(arrow, ColorRGBA.Green).setLocalTranslation(pos);

		arrow = new Arrow(Vector3f.UNIT_Z);
		arrow.setLineWidth(4); // make arrow thicker
		putShape(arrow, ColorRGBA.Blue).setLocalTranslation(pos);
	}
	
	private void attachGrid(Vector3f pos, int size, ColorRGBA color){
		Geometry g = new Geometry("wireframe grid", new Grid(size, size, 0.2f));
		Material mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		g.center().move(pos);
		rootNode.attachChild(g);
	}

    private Geometry putShape(Mesh shape, ColorRGBA color){
		Geometry g = new Geometry("coordinate axis", shape);
		Material mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		rootNode.attachChild(g);
		return g;
	}

	public void init() {
        loadScene();
        loadLights();
//        loadSky();
//        loadShadows();
        initWorldPhysics();
        initPostEffects();
        initSound();
        
        // player flashlight
        flashLight = new SpotLight();
        flashLight.setSpotRange(200f);                           // distance
        flashLight.setSpotInnerAngle(10f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        flashLight.setSpotOuterAngle(13f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        flashLight.setColor(ColorRGBA.White.mult(1.8f)); 
        rootNode.addLight(flashLight);

        // debug
        inputManager.addMapping("flashlight", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("physics", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "flashlight");
        inputManager.addListener(actionListener, "wireframe");
        inputManager.addListener(actionListener, "physics");
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

    public void loadShadows() {
//    	DirectionalLightShadowFilter shadowRenderer;
//        shadowRenderer = new DirectionalLightShadowFilter(assetManager, 1024, 3);
//        shadowRenderer.setLight(sun);
//        shadowRenderer.setShadowIntensity(0.4f);
//        shadowRenderer.setEdgesThickness(0);
//        shadowRenderer.setShadowZExtend(500);
//        fpp = new FilterPostProcessor(assetManager);
//        fpp.addFilter(shadowRenderer);
//        viewPort.addProcessor(fpp);
    }
    
    public void loadScene() {
        
    	// Parse the blender scene and assign materials
        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
            	logger.log(Level.INFO, "Visiting spatial: " + spatial.getName());
            	
//            	if(!(spatial instanceof TerrainPatch)) {
//            		logger.log(Level.INFO,"Instance of: " + spatial.getClass().getName()
//            				+ " - Name: " + spatial.getName() 
//            				+ " - Parent: " + spatial.getParent());            		
//            	}
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

            		if(spatial instanceof TerrainQuad && spatial.getName().equals("terrain")) {
            			logger.log(Level.INFO, "Adding RigidBodyControl to: " + spatial.getName());
            	        CollisionShape cs = CollisionShapeFactory.createMeshShape(spatial);
            	        RigidBodyControl rbc = new RigidBodyControl(cs, 0);
            	        spatial.addControl(rbc);
                    	bulletAppState.getPhysicsSpace().add(spatial);
            		}
            		if(spatial instanceof Node && spatial.getName().equals("tree")) { // tree node
                    	logger.log(Level.INFO, "Parsing tree and adding controls " + spatial.toString());
                    	spatial.addControl(new TargetableControl("Lovely tree"));
                    	spatial.addControl(new ResourceControl(ResourceType.WOOD));
                    	spatial.addControl(new QualityControl(1));
            		}
            		if(spatial instanceof Spatial && spatial.getName().equals("stem")) {
            			CollisionShape cs = CollisionShapeFactory.createMeshShape(spatial);
            			cs.setScale(spatial.getWorldScale()); // set the scale of the CS to the WORLD scale of the spatial
            			spatial.addControl(new RigidBodyControl(cs, 0));
            			bulletAppState.getPhysicsSpace().addAll(spatial);
            		}
            		if(spatial instanceof Spatial && spatial.getName().equals("rock")) { // tree node
            			logger.log(Level.INFO, "Parsing rock and adding controls " + spatial.toString());
            			spatial.addControl(new TargetableControl("Nice rock"));
            			spatial.addControl(new ResourceControl(ResourceType.STONE));
            			spatial.addControl(new QualityControl(0.9f));
            			
            			CollisionShape cs = CollisionShapeFactory.createMeshShape(spatial);
            			Vector3f v = spatial.getWorldScale();
            			cs.setScale(v);
            			spatial.addControl(new RigidBodyControl(cs, 0));
            			bulletAppState.getPhysicsSpace().addAll(spatial);
            		}
            		
            		if(spatial.getName().equals("Sky")) {
            			spatial.removeFromParent();
            		}
//                    if (spatial.getParent() != null && spatial.getParent().getName().equals("Trees")) {
//                    	logger.log(Level.INFO, "Parsing tree " + spatial.toString());
//                    	spatial.addControl(new ResourceControl());
//                    	logger.log(Level.INFO, "Added control to tree: " + spatial.getControl(ResourceControl.class));
//                    }        
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
    	
        scene = assetManager.loadModel("Scenes/minimalScene.j3o");
        scene.depthFirstTraversal(visitor);
        scene.scale(1f);
        scene.setShadowMode(ShadowMode.CastAndReceive);
        rootNode.attachChild(scene);
        
        Node skyNode = new Node("Sky");
        skyCtrl = new SkyControl(assetManager, camera, 0, true, true);
        skyNode.addControl(skyCtrl);
        skyCtrl.setEnabled(true);
        rootNode.attachChild(skyNode);
        
//        Spatial arrow = assetManager.loadModel("Models/arrow_scene.j3o");
//        Spatial arrow = assetManager.loadModel("Models/arrow2.j3o");
//        Spatial arrow = assetManager.loadModel("Models/arrow_box.j3o");
//        Spatial arrow = assetManager.loadModel("Models/tree/sapling_tree_mesh_type2.j3o");
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md"); // default material
//        arrow.setMaterial(mat);
//        arrow.setLocalTranslation(6.0551443f, 4.502792f, 22.551016f);
//        arrow.setShadowMode(ShadowMode.CastAndReceive);
//        CollisionShape cs = CollisionShapeFactory.createMeshShape(arrow);
//        RigidBodyControl rbc = new RigidBodyControl(cs, 0);
//        arrow.addControl(rbc);
//    	bulletAppState.getPhysicsSpace().addAll(arrow);
//
//        rootNode.attachChild(arrow);

        // make everything in the scene collidable

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
//        rootNode.attachChild(SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false));
    }

    public void loadLights() {
		// get the lights from the scene model
		LightList ll = scene.getLocalLightList();
		Iterator<Light> i = ll.iterator();    	
		while (i.hasNext()) {
			Light next = i.next();
			logger.log(Level.INFO, "light: " + next.getClass());
			
			// find the "sun", we want to reach it globally in the code
			if(next instanceof DirectionalLight) {
				sun = (DirectionalLight) next;
				logger.log(Level.INFO, "found DirectionalLight" + sun.toString());
			} 
			if(next instanceof AmbientLight) {
				ambientLight = (AmbientLight) next;
				logger.log(Level.INFO, "found AmbientLight" + ambientLight.toString());
			} 
	
			// IMPORTANT! remove the lights from the scene object and attach them to rootNode !!! 
			// (otherwise all objects on rootNode will be invisible)
			rootNode.addLight(next);    			
		}
		ll.clear(); // remove the lights from scene object since we've added them to rootNode instead!!!
		
		// add to skyCtrl
		skyCtrl.setMainLight(sun);
		skyCtrl.setAmbientLight(ambientLight);
	
    }

    public void initPostEffects() {
//    	Bloom
    	logger.log(Level.INFO,"Adding bloom effects");
        BloomFilter bloom = new BloomFilter();
        bloom.setBloomIntensity(0.2f);
        bloom.setBlurScale(0.4f);
        bloom.setExposurePower(2f);
        bloom.setDownSamplingFactor(5f);
        fpp.addFilter(bloom);

        logger.log(Level.INFO, "Adding directional shadow filter");
        DirectionalLightShadowFilter shadowFilter;
        shadowFilter = new DirectionalLightShadowFilter(assetManager, 512, 3);
        shadowFilter.setLight(sun);
        shadowFilter.setLambda(.65f);
        shadowFilter.setShadowIntensity(0.1f);
        shadowFilter.setEdgesThickness(2);
        shadowFilter.setShadowZExtend(500);
        shadowFilter.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        fpp.addFilter(shadowFilter);
        skyCtrl.setShadowFilter(shadowFilter); // let skyCtrl control this

//        logger.log(Level.INFO,"post effects water height: " + Float.toString(initialWaterHeight));
//        water = new WaterFilter(rootNode, lightDir);
//        water.setWaterHeight(initialWaterHeight);
//        water.setReflectionMapSize(512);
//        water.setReflectionDisplace(2);
//        water.setFoamExistence(new Vector3f(0.45f, 4.35f, 1.5f));
//        water.setWaterTransparency(0.08f);
//        fpp.addFilter(water);
        
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
        
        LightScatteringFilter lsf = new LightScatteringFilter(sun.getDirection().mult(-100));
        lsf.setLightDensity(.5f);
        fpp.addFilter(lsf);
        
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
    	if (flashLight != null && player != null) {
    		flashLight.setPosition(player.getLocation());
    		flashLight.setDirection(player.getLookDirection());
    	}
    }

    @Override
    public void cleanup() {
        super.cleanup();
        // TODO: clean up what you initialized in the initialize method,
        // e.g. remove all spatials from rootNode
        // this is called on the OpenGL thread after the AppState has been
        // detached
    }

//    public Node getTrees() {
//        return trees;
//    }
//
//    public void setTrees(Node trees) {
//        this.trees = trees;
//    }
    
	private ActionListener actionListener = new ActionListener() {
		boolean wireframeDebug = false;
		boolean physicsDebug = false;
		boolean flashLightOn = true;

		@Override
		public void onAction(String name, boolean pressed, float tpf) {
			// toggle wireframe
			if (name.equals("wireframe") && !pressed) {
				wireframeDebug = !wireframeDebug; // toggle boolean
				rootNode.depthFirstTraversal(new SceneGraphVisitor() {
					public void visit(Spatial spatial) {
						if (spatial instanceof Geometry)
							((Geometry) spatial).getMaterial()
									.getAdditionalRenderState()
									.setWireframe(wireframeDebug);
					}
				});
			}
			if (name.equals("physics") && !pressed) {
				physicsDebug = !physicsDebug; // toggle boolean
				bulletAppState.setDebugEnabled(physicsDebug);
			}
			if (name.equals("flashlight") && !pressed) {
				if (flashLightOn) {
					rootNode.removeLight(flashLight);					
				} else {
					rootNode.addLight(flashLight);					
				}
				flashLightOn = !flashLightOn; // toggle boolean
			}
			// else ... other input tests.
		}
	};
}