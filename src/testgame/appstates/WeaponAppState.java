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
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

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
		activeWeapon 	= new Cannon("Sweet Cannon", app); // temp
		player 			= app.getStateManager().getState(Player.class);
		bulletAppState	= app.getStateManager().getState(BulletAppState.class);
		assetManager	= app.getAssetManager();
	}
	
	@Override
	public void update(float tpf) {
		// here check what the weapon hits?
		// manipulate the rotation of a flying arrow?
		
		// here store all collisions? and deal with the cleanup of debris? (arrows etc)
	}
	
	public void fireWeapon() {
		logger.log(Level.INFO, "Firing weapon" + player.getLookDirection());
		if(activeWeapon instanceof WeaponRanged) {
			logger.log(Level.INFO, "Weapon is ranged");
			WeaponRanged wr = (WeaponRanged) activeWeapon;

			// create a geometry, collision shape and control of the right type and let it flyy
			// IMPORTANT! clone a unique copy of the geometry for each weapon fire. otherwise there will be nullpointer errors in PhysicsSpace... !
			Geometry arrow 				= (Geometry) wr.getBulletGeometry().deepClone();
			SphereCollisionShape cs 	= wr.getBulletCollisionShape(); // FIX: make general
			ArrowRigidBodyControl arbc	= new ArrowRigidBodyControl(assetManager, cs, 1); // TEMP: hardcoded arrow control..
			
			logger.log(Level.INFO, "Weapon has bullet geom: " + arrow.toString() + " and shape: " + cs.toString());
			
			// test with arrow
			arrow.setShadowMode(ShadowMode.CastAndReceive);

			// set direction and velocity
			Vector3f spawnlocation = player.getLocation().add(player.getLookDirection().mult(2));
			arrow.setLocalTranslation(spawnlocation);
			arbc.setLinearVelocity(player.getLookDirection().mult(100));
			arrow.addControl(arbc);
			
			// add this to ze world
			player.rootNode.attachChild(arrow);
			bulletAppState.getPhysicsSpace().add(arbc);
		}
	}

	public Weapon getActiveWeapon() {
		return activeWeapon;
	}

	public void setActiveWeapon(Weapon activeWeapon) {
		this.activeWeapon = activeWeapon;
	}
}
