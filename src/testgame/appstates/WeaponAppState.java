package testgame.appstates;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.controls.BulletRigidBodyControl;
import testgame.items.weapons.RangedWeapon;
import testgame.items.weapons.RangedWeaponBullet;
import testgame.items.weapons.Weapon;
import testgame.player.Player;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
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
		RangedWeaponBullet bullet = new RangedWeaponBullet("Arrow", assetManager.loadModel("Models/arrow2.j3o")); 
		bullet.setMass(0.3f);
		RangedWeapon bow = new RangedWeapon("Sweet Bow"); // temp
		bow.setBullet(bullet);
		bow.setMaxChargeTime(2f); // set how long it can be charged before reaching max force 
		bow.setMinForce(200f); // so we don't get a 0 velocity arrow...
		bow.setMaxForce(700f);
		
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
			return;
		}
		
		// we are not chargin the weapon anymore
		weaponCharging = false;

		if(activeWeapon instanceof RangedWeapon) {
			logger.log(Level.INFO, "Weapon is ranged");
			
			if(((RangedWeapon) activeWeapon).getBullet() == null) {
				logger.log(Level.INFO, "No bullet set, can't fire");
				return;
			}
			
			// clone the bullet spatial
			Node arrowNode = new Node();
			Spatial bulletSpatial = ((RangedWeapon) activeWeapon).getBullet().getSpatial().clone();
			bulletSpatial.setLocalScale(0.3f);
			arrowNode.attachChild(bulletSpatial);
			
			logger.log(Level.INFO, "Weapon has bullet spatial: " + bulletSpatial.toString());
			
			// set initial position of bullet
			Vector3f spawnlocation = player.getLocation().add(player.getLookDirection());
			arrowNode.setLocalTranslation(spawnlocation);
			
			// add rbc
			// [review] - necessary to create a new collision shape here every time?
			BoxCollisionShape cs = new BoxCollisionShape(((BoundingBox) bulletSpatial.getWorldBound()).getExtent(new Vector3f()));
			BulletRigidBodyControl arbc	= new BulletRigidBodyControl(assetManager, cs, ((RangedWeapon) activeWeapon).getBullet().getMass());

			// set initial rotation to point to where player looks
			Quaternion lookRotation = new Quaternion();
			lookRotation.lookAt(player.getLookDirection(), new Vector3f(0,1,0));
			arbc.setPhysicsRotation(lookRotation);
			arrowNode.setLocalRotation(lookRotation);
			
			// collion groups
			arbc.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03); // separate from the player
			arbc.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01); // collide with world
			
			// set velocity based on how long we charged and the weapons max and min force
			arbc.setLinearVelocity(player.getLookDirection().mult(((RangedWeapon) activeWeapon).getBulletVelocity(chargeTime))); // this is specifc for a RANGED weapon
			arrowNode.addControl(arbc);
			
			// add this to ze world
			bulletAppState.getPhysicsSpace().add(arbc);
			rootNode.attachChild(arrowNode);
			
			logger.log(Level.INFO, "Fired bullet [chargeTime: {0}s, force: {1}, velocity: {2}]", new Object[]{chargeTime, ((RangedWeapon) activeWeapon).getChargedForce(chargeTime), ((RangedWeapon) activeWeapon).getBulletVelocity(chargeTime)});
		}
	}
	
	public Weapon getActiveWeapon() {
		return activeWeapon;
	}

	public void setActiveWeapon(Weapon activeWeapon) {
		this.activeWeapon = activeWeapon;
	}
}
