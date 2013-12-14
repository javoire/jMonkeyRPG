package testgame.appstates;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.controls.ArrowRigidBodyControl;
import testgame.items.weapons.RangedWeapon;
import testgame.items.weapons.Weapon;
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
	private Float velocity;
	private Float chargeTime = 0f;
	private boolean weaponCharging = false;

	public WeaponAppState(Node rootNode) {
		this.rootNode = rootNode;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		player 			= app.getStateManager().getState(Player.class);
		bulletAppState	= app.getStateManager().getState(BulletAppState.class);
		assetManager	= app.getAssetManager();

		// build a ranged weapon (bow)
		RangedWeapon bow = new RangedWeapon("Sweet Bow", assetManager.loadModel("Models/arrow2.j3o")); // temp
		bow.setVelocityMultiplier(100f); // set "force" of bow
		bow.setMaxChargeTime(2f); // set how long it can be charged before reaching max force 
		bow.setMinForce(0.8f); // so we don't get a 0 velocity arrow...
		bow.setMaxForce(2.5f);
		
		activeWeapon = bow;
	}
	
	@Override
	public void update(float tpf) {
		// manipulate the rotation of a flying arrow?
		
		if(activeWeapon != null) {
			// set velocity depending on the time until releasing arrow (how long we charge)
			if(weaponCharging && chargeTime < activeWeapon.getMaxChargeTime()) {
				chargeTime += tpf;
			}			
		}
	}
	
	public void chargeWeapon() {
		// we are charging the weapong
		weaponCharging = true;
		// reset chargeTime
		chargeTime = 0f;
	}
	
	public void fireWeapon() {
		if(activeWeapon == null) {
			logger.log(Level.INFO, "No active weapon, not firing");
		}
		
		// we are not chargin the weapon anymore
		weaponCharging = false;

		if(activeWeapon instanceof RangedWeapon) {
			logger.log(Level.INFO, "Weapon is ranged");
			
			// Create and fire the bullet //
			
			// clone the bullet spatial
			Spatial bullet = ((RangedWeapon) activeWeapon).getBulletSpatial().clone();
			bullet.setLocalScale(0.4f);
			
			logger.log(Level.INFO, "Weapon has bullet spatial: " + bullet.toString());
			
			// set initial direction of bullet
			Vector3f spawnlocation = player.getLocation().add(player.getLookDirection().mult(5));
			bullet.setLocalTranslation(spawnlocation);
			
			// set initial rotation to point to where player looks
			Quaternion lookRotation = new Quaternion();
			lookRotation.lookAt(player.getLookDirection(), new Vector3f(0,1,0));
			bullet.setLocalRotation(lookRotation);
	
			// add rbc
			ArrowRigidBodyControl arbc	= new ArrowRigidBodyControl(assetManager, CollisionShapeFactory.createDynamicMeshShape(bullet), 0.1f); // TEMP: hardcoded arrow control..
			
			// set velocity based on how long we charged and the weapons max and min force
			arbc.setLinearVelocity(player.getLookDirection().mult(((RangedWeapon) activeWeapon).getVelocity(chargeTime))); // this is specifc for a RANGED weapon
			bullet.addControl(arbc);
			
			// add this to ze world
			bulletAppState.getPhysicsSpace().add(arbc);
			rootNode.attachChild(bullet);
			
			logger.log(Level.INFO, "Fired bullet [chargeTime: {0}, force: {1}, velocity: {2}]", new Object[]{chargeTime, ((RangedWeapon) activeWeapon).getChargedForce(chargeTime), ((RangedWeapon) activeWeapon).getVelocity(chargeTime)});
		}
	}
	
	public Weapon getActiveWeapon() {
		return activeWeapon;
	}

	public void setActiveWeapon(Weapon activeWeapon) {
		this.activeWeapon = activeWeapon;
	}
}
