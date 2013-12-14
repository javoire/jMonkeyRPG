package testgame.items.weapons;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.scene.Spatial;

public class RangedWeapon extends Weapon {
	
//	private Player                  player;
//	private AssetManager 			assetManager;
//	private BulletAppState 			bulletAppState;
//	private Application 			app;
//	private SphereCollisionShape 	bulletCollisionShape;
	/**
	 * The spatial of the bullet, can be an arrow, bolt, bullet etc
	 */
	private Spatial					bulletSpatial;
	/**
	 * The collision shape of the bullet
	 */
	private MeshCollisionShape 		bulletMeshCollisionShape;
	/**
	 * Determine the velocity of the bullet (arrow, bolt), is combined with chargeTime and a force value
	 */
	private Float 					velocityMultiplier = 100f; // default 10
	
	public RangedWeapon(String name) {
		super(WeaponType.RANGED, name);
//		this.app 	= app;
//		this.player = app.getStateManager().getState(Player.class);
//		this.assetManager 	= app.getAssetManager();
//		this.bulletAppState = app.getStateManager().getState(BulletAppState.class);
	}
	
	/**
	 * RangedWeapon takes a name and the spatial that it will use as a bullet (that it'll shoot when fired)
	 * @param name The name of the weapon, shown in GUIs etc
	 * @param spatial The spatial it'll shoot when fired
	 */
	public RangedWeapon(String name, Spatial spatial) {
		super(WeaponType.RANGED, name);
		bulletSpatial = spatial;
	}
	
//	public SphereCollisionShape getBulletCollisionShape() {
//		return bulletCollisionShape;
//	}
//
//	public void setBulletCollisionShape(SphereCollisionShape bulletCollisionShape) {
//		this.bulletCollisionShape = bulletCollisionShape;
//	}

	public Spatial getBulletSpatial() {
		return bulletSpatial;
	}

	public void setBulletSpatial(Spatial spatial) {
		this.bulletSpatial = spatial;
	}
	
	/**
	 * Returns a velocity value of the bullet based on how much force we got from charging the weapon, and a velocity multiplier
	 * @param chargeTime
	 * @return <code>Float</code> velocity
	 */
	public Float getVelocity(Float chargeTime) {
		return getChargedForce(chargeTime) * velocityMultiplier;
	}

	public Float getVelocityMultiplier() {
		return velocityMultiplier;
	}

	/**
	 * This should be set by a child class (eg. a Bow() etc) to define the force of the weapon
	 * @param velocityMultiplier
	 */
	public void setVelocityMultiplier(Float velocityMultiplier) {
		this.velocityMultiplier = velocityMultiplier;
	}
	
//	public MeshCollisionShape getBulletMeshCollisionShape() {
//		return bulletMeshCollisionShape;
//	}
//
//	public void setBulletMeshCollisionShape(MeshCollisionShape bulletMeshCollisionShape) {
//		this.bulletMeshCollisionShape = bulletMeshCollisionShape;
//	}
	
//	public void shoot(Geometry bulletGeom, SphereCollisionShape bulletCollisionShape) {		
//		RigidBodyControl bulletNode = new BombControl(assetManager, bulletCollisionShape, 1);
//		bulletGeom.setShadowMode(ShadowMode.CastAndReceive);
//		
//		Vector3f spawnlocation = player.getLocation().add(player.getLookDirection().mult(5));
//		bulletGeom.setLocalTranslation(spawnlocation);
//		bulletNode.setLinearVelocity(player.getLookDirection().mult(150));
////		bulletNode.removeCollideWithGroup(2);
//		bulletGeom.addControl(bulletNode);
//		
//		player.rootNode.attachChild(bulletGeom);
//		bulletAppState.getPhysicsSpace().add(bulletNode);
	// }
}
