/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import jme3test.bullet.BombControl;
import testgame.appstates.HarvestingAppState;
import testgame.appstates.TargetInfo;
import testgame.inventory.Inventory;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;

/**
 *
 * @author jo_da12
 */
public class Player extends AbstractAppState implements ActionListener {
    
    private CharacterControl    		playerControl;
    private InputManager        		inputManager;
    private BulletAppState      		bulletAppState;
    private Camera              		cam;
    private static Sphere				bullet;
    private static SphereCollisionShape bulletCollisionShape;
    private Material					bulletMat;
    private AssetManager				assetManager;
    private Node 						rootNode;
    private Vector3f            		walkDirection = new Vector3f();
    private Inventory					inventory;
    private HarvestingAppState 			harvester;
    private TargetInfo 					targetInfo;
    
    private boolean left = false, right = false, up = false, down = false;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
                
        inputManager   	= app.getInputManager();
        bulletAppState 	= app.getStateManager().getState(BulletAppState.class);
        cam            	= app.getCamera();
        assetManager	= app.getAssetManager();
        harvester		= app.getStateManager().getState(HarvestingAppState.class);
        targetInfo		= app.getStateManager().getState(TargetInfo.class);
        inventory		= app.getStateManager().getState(Inventory.class);

        // create player object
        CapsuleCollisionShape capsuleShape  = new CapsuleCollisionShape(1.5f, 6f, 1);        
        playerControl                       = new CharacterControl(capsuleShape, 0.05f);
        
        // bullet
//        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);

        bullet = new Sphere(32, 32, 0.4f, true, false);
        bullet.setTextureMode(TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.4f);

        bulletMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey bulletTexKey = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        bulletTexKey.setGenerateMips(true);
        Texture bulletTex = assetManager.loadTexture(bulletTexKey);
        bulletMat.setTexture("ColorMap", bulletTex);
    }
    
    public Player(Node rootNode) {
    	this.rootNode = rootNode;
    }
    
    @Override
    public void update(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.4f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.2f);
        
        walkDirection.set(0, 0, 0);
        
        if (left)  { walkDirection.addLocal(camLeft.setY(0)); } // set y 0 to not move player upwards.
        if (right) { walkDirection.addLocal(camLeft.setY(0).negate()); }
        if (up)    { walkDirection.addLocal(camDir.setY(0)); }
        if (down)  { walkDirection.addLocal(camDir.setY(0).negate()); }
        
        playerControl.setWalkDirection(walkDirection);
        
        cam.setLocation(playerControl.getPhysicsLocation());
    }
    
    /** These are our custom actions triggered by key presses.
    * We do not walk yet, we just keep track of the direction the user pressed. */
    @Override
	public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            if (value) { left = true; } else { left = false; }
        } else if (binding.equals("Right")) {
            if (value) { right = true; } else { right = false; }
        } else if (binding.equals("Up")) {
            if (value) { up = true; } else { up = false; }
        } else if (binding.equals("Down")) {
            if (value) { down = true; } else { down = false; }
        } else if (binding.equals("Jump")) {
            playerControl.jump();
	    } else if (binding.equals("Shoot") && !value) {
//	    	shoot();
	    } else if (binding.equals("Harvest") && !value) {
	    	harvester.tryHarvest();
	    }
    }
    
	public void shoot() {
        Geometry bulletGeom = new Geometry("bullet", bullet);
        bulletGeom.setMaterial(bulletMat);
        bulletGeom.setShadowMode(ShadowMode.CastAndReceive);
        Vector3f spawnlocation = cam.getLocation().add(cam.getDirection().mult(5));
        bulletGeom.setLocalTranslation(spawnlocation);
        
        RigidBodyControl bulletNode = new BombControl(assetManager, bulletCollisionShape, 1);
        bulletNode.setLinearVelocity(cam.getDirection().mult(50));
        bulletNode.removeCollideWithGroup(2);
        bulletGeom.addControl(bulletNode);
        rootNode.attachChild(bulletGeom);
        bulletAppState.getPhysicsSpace().add(bulletNode);
    }
    
    public void initPlayer() {
        playerControl.setJumpSpeed(30);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(90);
        playerControl.setPhysicsLocation(new Vector3f(5, 100, 0));
        playerControl.setCollisionGroup(2);
        
        bulletAppState.getPhysicsSpace().add(playerControl);

        initKeyBindings();
    }
    
    private void initKeyBindings() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Harvest", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Shoot");
        inputManager.addListener(this, "Harvest");
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
