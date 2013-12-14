package testgame.items.weapons;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.scene.Spatial;

public class RangedWeapon extends Weapon {
	
	/**
	 * The bullet used, could be an arrow or bolt etc
	 */
	private RangedWeaponBullet bullet;
	
	/**
	 * RangedWeapon
	 * @param name The name of the weapon, shown in GUIs etc
	 */
	public RangedWeapon(String name) {
		super(WeaponType.RANGED, name);
	}
		
	/**
	 * Returns a velocity value for the bullet based on how much force we got from charging the weapon and the mass of the bullet
	 * Use this value when firing a bullet with this weapon
	 * @param chargeTime
	 * @return <code>Float</code> velocity
	 */
	public Float getBulletVelocity(Float chargeTime) {
		return getChargedForce(chargeTime) * bullet.getMass();
	}

	/**
	 * @return {@link RangedWeaponBullet} The bullet this weapon fires
	 */
	public RangedWeaponBullet getBullet() {
		return bullet;
	}

	public void setBullet(RangedWeaponBullet bullet) {
		this.bullet = bullet;
	}
	
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
