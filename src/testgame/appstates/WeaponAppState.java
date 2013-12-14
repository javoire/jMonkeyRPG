package testgame.appstates;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.controls.ArrowRigidBodyControl;
import testgame.items.weapons.Cannon;
import testgame.items.weapons.Weapon;
import testgame.items.weapons.WeaponRanged;
import testgame.player.Player;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class WeaponAppState extends AbstractAppState {
	
	private static final Logger logger = Logger.getLogger(WeaponAppState.class.getName());
	
	private Weapon activeWeapon;
	private Player player;
	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;

	public WeaponAppState(Node rootNode) {
		this.rootNode = rootNode;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		player 			= app.getStateManager().getState(Player.class);
		bulletAppState	= app.getStateManager().getState(BulletAppState.class);
		assetManager	= app.getAssetManager();

//		activeWeapon 	= new Cannon("Sweet Cannon", app, assetManager.loadModel("Models/arrow2.j3o")); // temp
		activeWeapon 	= new Cannon("Sweet Cannon", app, assetManager.loadModel("Models/arrow_box.j3o")); // temp
//		activeWeapon 	= new Cannon("Sweet Cannon", app, assetManager.loadModel("Models/arrow/arrow.j3o")); // temp, exported with ogre
	}
	
	@Override
	public void update(float tpf) {
		// manipulate the rotation of a flying arrow?
	}
	
	public void fireWeapon() {
		if(activeWeapon == null) {
			logger.log(Level.INFO, "No active weapon, not firing");
		}
		logger.log(Level.INFO, "Firing weapon" + player.getLookDirection());
		if(activeWeapon instanceof WeaponRanged) {
			logger.log(Level.INFO, "Weapon is ranged");

			// load object
			Spatial arrow 				= assetManager.loadModel("Models/arrow2.j3o"); // we CANNOT deep clone, the mesh will get fucked up and break the collision shape generator
			arrow.setLocalScale(0.4f);
			
			logger.log(Level.INFO, "Weapon has bullet spatial: " + arrow.toString());
			
			// set direction and velocity
			Vector3f spawnlocation = player.getLocation().add(player.getLookDirection().mult(5));
			arrow.setLocalTranslation(spawnlocation);
			
			// set initial rotation to point to where player looks
			Quaternion lookRotation = new Quaternion();
			lookRotation.lookAt(player.getLookDirection(), new Vector3f(0,1,0));
			arrow.setLocalRotation(lookRotation);
			
			logger.log(Level.INFO, "Firing: Arrow looking towards: " + lookRotation);

			// add rbc
			CollisionShape cs			= CollisionShapeFactory.createDynamicMeshShape(arrow);
			ArrowRigidBodyControl arbc	= new ArrowRigidBodyControl(assetManager, cs, 0.1f); // TEMP: hardcoded arrow control..
			arbc.setLinearVelocity(player.getLookDirection().mult(100));
			arrow.addControl(arbc);
			
			// add this to ze world
			bulletAppState.getPhysicsSpace().add(arbc);
			rootNode.attachChild(arrow);
		}
	}

	public Weapon getActiveWeapon() {
		return activeWeapon;
	}

	public void setActiveWeapon(Weapon activeWeapon) {
		this.activeWeapon = activeWeapon;
	}
}
