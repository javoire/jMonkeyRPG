/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetKey;
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
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.Control;
import com.jme3.scene.control.LightControl;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;
//import com.jme3.scene.plugins.blender.BlenderLoader;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
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

	private BulletAppState 		bulletAppState;
	private RigidBodyControl 	landscape;
	private AssetManager 		assetManager;
	private Node 				rootNode;
	private WaterFilter 		water;
	private TerrainQuad 		terrain;
	private ViewPort 			viewPort;
	private AudioRenderer 		audioRenderer;
	private Camera 				camera;
	private Material			terrainMaterial;
	private PssmShadowRenderer	pssmRenderer;
	private FilterPostProcessor fpp;
	private Vector3f 			lightDir = new Vector3f(-0.74319214f, -0.20267837f,0.84856685f); // same as light source
	private float 				initialWaterHeight = -1; // choose a value for your scene

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		// TODO: initialize your AppState, e.g. attach spatials to rootNode
		// this is called on the OpenGL thread after the AppState has been
		// attached

		assetManager 		= app.getAssetManager();
		bulletAppState 		= app.getStateManager().getState(BulletAppState.class);
		viewPort 			= app.getViewPort();
		audioRenderer 		= app.getAudioRenderer();
		camera 				= app.getCamera();

		fpp = new FilterPostProcessor(assetManager);

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
	
	public void loadShadows () {
		rootNode.setShadowMode(ShadowMode.Off); // off allt sen aktivera separat
		pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 1);
	    pssmRenderer.setDirection(new Vector3f(lightDir).normalizeLocal()); // light direction
	    pssmRenderer.setShadowIntensity(0.4f);
	    pssmRenderer.setFilterMode(FilterMode.Bilinear);
	    pssmRenderer.setEdgesThickness(0);
	    pssmRenderer.setShadowZExtend(500);
	    viewPort.addProcessor(pssmRenderer);	}

	public void loadTerrainModels() {		
		Node treeParent = new Node("treeParent");
		Node tree1 = new Node("Tree");
		
		treeParent.setCullHint(CullHint.Dynamic);
		
		/* tree */
		Spatial bark = assetManager.loadModel("Models/tree/tree_bark.j3o");
    	Spatial leaves = assetManager.loadModel("Models/tree/tree_leaves.j3o");

    	Control leavesLodControl1 = new LeavesLodControl(leaves, camera);
    	leaves.addControl(leavesLodControl1);    	

    	tree1.attachChild(bark);
    	tree1.attachChild(leaves);
    	    	
    	/* more trees */
    	Node tree2 = tree1.clone(true);
    	Node tree3 = tree1.clone(true);
    	Node tree4 = tree1.clone(true);
    	Node tree5 = tree1.clone(true);
    	
    	tree1.setLocalTranslation(-8, 2, 60);
    	tree2.setLocalTranslation(6, 2, 82);
    	tree3.setLocalTranslation(74, 2, 75);
    	tree4.setLocalTranslation(103, 2, 66);
    	tree5.setLocalTranslation(81, 2, 44);
    	
    	tree1.setLocalScale(4);
    	tree2.setLocalScale(3.5f);
    	tree3.setLocalScale(3.7f);
    	tree4.setLocalScale(3.0f);
    	tree5.setLocalScale(4f);
    	
    	tree1.setLocalRotation(new Quaternion(0, -0.04f, 0, 0));
    	tree2.setLocalRotation(new Quaternion(0, 0.37f, 0, 0));
    	tree3.setLocalRotation(new Quaternion(0, 0.21f, 0, 0));
    	tree4.setLocalRotation(new Quaternion(0, 0.04f, 0, 0));
    	tree5.setLocalRotation(new Quaternion(0, 0.08f, 0, 0));
    	
    	treeParent.setShadowMode(ShadowMode.Cast);

    	/* attach */
    	rootNode.attachChild(treeParent);
     	treeParent.attachChild(tree1);
    	treeParent.attachChild(tree2);
    	treeParent.attachChild(tree3);
    	treeParent.attachChild(tree4);
    	treeParent.attachChild(tree5);

    	/* physics */
    	/* TODO: fixa så bara physics på stammen */
    	CollisionShape treeShape1 = CollisionShapeFactory.createMeshShape(tree1);
    	CollisionShape treeShape2 = CollisionShapeFactory.createMeshShape(tree2);
    	CollisionShape treeShape3 = CollisionShapeFactory.createMeshShape(tree3);
    	CollisionShape treeShape4 = CollisionShapeFactory.createMeshShape(tree4);
    	CollisionShape treeShape5 = CollisionShapeFactory.createMeshShape(tree5);
		RigidBodyControl treeBodyControl1 = new RigidBodyControl(treeShape1, 0);
		RigidBodyControl treeBodyControl2 = new RigidBodyControl(treeShape2, 0);
		RigidBodyControl treeBodyControl3 = new RigidBodyControl(treeShape3, 0);
		RigidBodyControl treeBodyControl4 = new RigidBodyControl(treeShape4, 0);
		RigidBodyControl treeBodyControl5 = new RigidBodyControl(treeShape5, 0);
		tree1.addControl(treeBodyControl1);
		tree2.addControl(treeBodyControl2);
		tree3.addControl(treeBodyControl3);
		tree4.addControl(treeBodyControl4);
		tree5.addControl(treeBodyControl5);
		bulletAppState.getPhysicsSpace().add(tree1);
		bulletAppState.getPhysicsSpace().add(tree2);
		bulletAppState.getPhysicsSpace().add(tree3);
		bulletAppState.getPhysicsSpace().add(tree4);
		bulletAppState.getPhysicsSpace().add(tree5);

    }

	public void loadTerrain() {
				
		/** 2. Create the height map */
		AbstractHeightMap heightmap = null;
//		Texture heightMapImage = assetManager.loadTexture("Models/terrain/heightmap.png");
		Texture heightMapImage = assetManager.loadTexture("Models/terrain/heightmap_256.png");
		heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
		heightmap.setHeightScale(60);
		heightmap.load();
		heightmap.smooth(1f, 2);

		/**
		 * 3. We have prepared material and heightmap. Now we create the actual
		 * terrain: 3.1) Create a TerrainQuad and name it "my terrain". 3.2) A
		 * good value for terrain tiles is 64x64 -- so we supply 64+1=65. 3.3)
		 * We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
		 * 3.4) As LOD step scale we supply Vector3f(1,1,1). 3.5) We supply the
		 * prepared heightmap itself.
		 */
		int patchSize = 33;
		terrain = new TerrainQuad("my terrain", patchSize, 257, heightmap.getHeightMap());
		terrainMaterial = assetManager.loadMaterial("Materials/terrain.j3m");
//		terrainMaterial.getAdditionalRenderState().setWireframe(true); // debug
		terrain.setMaterial(terrainMaterial);
		terrain.setLocalTranslation(0, -30.5f, 0);
		terrain.setLocalScale(2f, 1f, 2f);

//		rotation buggggggar slopes, man går långasmt
		//		Vector3f axis = Vector3f.UNIT_Y; // this equals (0, 1, 0) and does not require to create a new object
//		float angle = -3.14f/4;
//		Quaternion rootRotation = rootNode.getLocalRotation().fromAngleAxis(angle, axis);
//		terrain.setLocalRotation(rootRotation);

		rootNode.attachChild(terrain);
		
		terrain.setShadowMode(ShadowMode.Receive);

		/** 5. The LOD (level of detail) depends on were the camera is: */
		TerrainLodControl lodControl = new TerrainLodControl(terrain, camera);
		lodControl.setLodCalculator(new DistanceLodCalculator(32, 3.1f));
		terrain.addControl(lodControl);
	}
	
	public void loadSky() {
		// rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
		rootNode.attachChild(SkyFactory.createSky(assetManager,"Scenes/Beach/FullskiesSunset0068.dds", false));
	}

	public void loadLights() {
		DirectionalLight sun = new DirectionalLight();
		AmbientLight amb = new AmbientLight();
		ColorRGBA sunColor= new ColorRGBA(0.9f,0.77f,0.52f,2f);
		
		sun.setDirection(lightDir);
		sun.setColor(sunColor.mult(2));
		amb.setColor(sunColor.mult(2));
		
		PointLight lamp_light = new PointLight();
		lamp_light.setColor(ColorRGBA.Orange);
		lamp_light.setRadius(100f);
		lamp_light.setPosition(new Vector3f(0,3,0));
//		rootNode.addLight(lamp_light);

		rootNode.addLight(sun);
		rootNode.addLight(amb);
	}

	public void initPostEffects() {
//		BloomFilter bloom = new BloomFilter();
//		bloom.setBloomIntensity(0.05f);
//		bloom.setBlurScale(0.4f);
//		bloom.setExposurePower(2f);
//		bloom.setDownSamplingFactor(5f);
//		fpp.addFilter(bloom);
		
		water = new WaterFilter(rootNode, lightDir);
		water.setWaterHeight(initialWaterHeight);
		water.setReflectionMapSize(256);
		water.setReflectionDisplace(10);
		water.setFoamExistence(new Vector3f(0.45f,4.35f,1.5f));
		water.setWaterTransparency(0.08f);
		fpp.addFilter(water);

		/* light scattering */
//        Vector3f lightPos = lightDir.multLocal(-3000);
//        LightScatteringFilter lightFilter = new LightScatteringFilter(lightPos);
//        lightFilter.setLightDensity(0.3f);
//        lightFilter.setBlurWidth(2f);
//        lightFilter.setBlurStart(0.01f);
//        fpp.addFilter(lightFilter);
		
		/* fog */
//        FogFilter fog = new FogFilter();
//        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
//        fog.setFogDistance(900);
//        fog.setFogDensity(1.0f);
//        fpp.addFilter(fog);
		
//		SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 5f, 0.33f, 0.61f);
//		fpp.addFilter(ssaoFilter);
		
		viewPort.addProcessor(fpp);
	}

	public void initSound() {
		// AudioNode nature = new AudioNode(assetManager,
		// "Sound/Environment/Nature.ogg", false);
		AudioNode waves = new AudioNode(assetManager,"Sounds/Environment/Ocean Waves.ogg", false);

		waves.setLooping(true);
		waves.setVolume(0.01f);

		audioRenderer.playSource(waves);
	}

	public void initWorldPhysics() {

		/** 6. Add physics: */
		// We set up collision detection for the scene by creating a
		// compound collision shape and a static RigidBodyControl with mass
		// zero.*/
		CollisionShape terrainShape = CollisionShapeFactory.createMeshShape((Node) terrain);
		landscape = new RigidBodyControl(terrainShape, 0);
		terrain.addControl(landscape);

		// We attach the scene and the player to the rootnode and the physics
		// space,
		// to make them appear in the game world.
		bulletAppState.getPhysicsSpace().add(terrain);
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
}