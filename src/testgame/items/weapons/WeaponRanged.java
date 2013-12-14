package testgame.items.weapons;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;

public class WeaponRanged extends Weapon {
	
//	private Player                  player;
//	private AssetManager 			assetManager;
//	private BulletAppState 			bulletAppState;
//	private Application 			app;
//	private SphereCollisionShape 	bulletCollisionShape;
	private MeshCollisionShape 		bulletMeshCollisionShape;
	private Spatial					bulletSpatial;

	public WeaponRanged(String name) {
		super(WeaponType.RANGED, name);
//		this.app 	= app;
//		this.player = app.getStateManager().getState(Player.class);
//		this.assetManager 	= app.getAssetManager();
//		this.bulletAppState = app.getStateManager().getState(BulletAppState.class);
	}
	
	public WeaponRanged(String name, Spatial spatial) {
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
//	}
}
