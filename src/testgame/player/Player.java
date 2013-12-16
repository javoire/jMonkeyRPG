/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.player;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.inventory.Inventory;
import testgame.player.controls.PlayerAttributesControl;
import testgame.player.controls.PlayerEquipmentControl;
import testgame.world.World;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 * Player class. 
 * @author jo_da12
 */
public class Player extends AbstractAppState {
	
	private static final Logger logger = Logger.getLogger(World.class.getName());
	
    private CharacterControl    		playerControl;
    private BulletAppState      		bulletAppState;
    private Camera              		cam;
    public 	Node 						rootNode;
	private PlayerAttributesControl 	attributesControl;
	private PlayerEquipmentControl 		equipmentControl;
	private Application 				app;
	private Inventory inventory;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app			= app;
        cam            		= app.getCamera();
        bulletAppState 		= app.getStateManager().getState(BulletAppState.class);
        inventory			= app.getStateManager().getState(Inventory.class);
        playerControl 		= new CharacterControl(new CapsuleCollisionShape(1.5f, 6f, 1), 0.05f);
        attributesControl 	= new PlayerAttributesControl();
        equipmentControl	= new PlayerEquipmentControl();
        
        initDefaultWeapons();
        
        cam.setFrustumFar(4000);
        logger.log(Level.INFO, "Initializing camera - Frustum near: " + cam.getFrustumNear());
//        cam.setFrustumPerspective(60, (float)cam.getWidth( ) / cam.getHeight( ), 1f, 4000f);
    }

    public void init() {
	playerControl.setJumpSpeed(30);
    	playerControl.setFallSpeed(30);
    	playerControl.setGravity(90);
    	playerControl.setPhysicsLocation(new Vector3f(20, 20, 0));
    	playerControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
    	playerControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01); // collide with world
        bulletAppState.getPhysicsSpace().add(playerControl);	
    }
    
    private void initDefaultWeapons() {
//    	Cannon cannon1 = new Cannon("Cannon 1", app);
//    	inventory.setQuickslot(0, cannon1);
//    	equipmentControl.setMainHand(cannon1);
	}

	public Player(Node rootNode) {
    	this.rootNode = rootNode;
    }
    
    @Override
    public void update(float tpf) {

    
    }
    
    public Vector3f getLookDirection() {
    	return cam.getDirection();
    }
    
    public Vector3f getLocation() {
    	return cam.getLocation();
    }
    
    public void setAttribute() {
    	
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

	public CharacterControl getPlayerControl() {
		return playerControl;
	}

	public void setPlayerControl(CharacterControl playerControl) {
		this.playerControl = playerControl;
	}

	public PlayerAttributesControl getAttributesControl() {
		return attributesControl;
	}

	public void setAttributesControl(PlayerAttributesControl attributesControl) {
		this.attributesControl = attributesControl;
	}

	public PlayerEquipmentControl getEquipmentControl() {
		return equipmentControl;
	}

	public void setEquipmentControl(PlayerEquipmentControl equipmentControl) {
		this.equipmentControl = equipmentControl;
	}
}
