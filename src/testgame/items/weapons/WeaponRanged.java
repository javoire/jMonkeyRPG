package testgame.items.weapons;

import jme3test.bullet.BombControl;
import testgame.player.Player;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;

public class WeaponRanged extends Weapon {
	
//	private Player                  player;
//	private AssetManager 			assetManager;
//	private BulletAppState 			bulletAppState;
//	private Application 			app;
	private SphereCollisionShape 	bulletCollisionShape;
	private Geometry 				bulletGeometry;

	public WeaponRanged(String name) {
		super(WeaponType.RANGED, name);
//		this.app 	= app;
//		this.player = app.getStateManager().getState(Player.class);
//		this.assetManager 	= app.getAssetManager();
//		this.bulletAppState = app.getStateManager().getState(BulletAppState.class);
	}

	public Geometry getBulletGeometry() {
		return bulletGeometry;
	}

	public void setBulletGeometry(Geometry bulletGeometry) {
		this.bulletGeometry = bulletGeometry;
	}

	public SphereCollisionShape getBulletCollisionShape() {
		return bulletCollisionShape;
	}

	public void setBulletCollisionShape(SphereCollisionShape bulletCollisionShape) {
		this.bulletCollisionShape = bulletCollisionShape;
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
//	}
}
