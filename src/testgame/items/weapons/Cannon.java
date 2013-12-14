package testgame.items.weapons;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 * Cannon class. Stores properties, bullet geometry and collision shape of the Cannon weapon.
 * @author Jonatan Dahl
 */
public class Cannon extends WeaponRanged {
	
	private AssetManager 			assetManager;
	private Material 				bulletMat;
	private Sphere 					bullet;
	private TextureKey 				bulletTexKey;
	private SphereCollisionShape 	bulletCollisionShape;
	private Geometry 				bulletGeometry;

	public Cannon(String name, Application app, Spatial spatial) {
		super(name, spatial);
//		assetManager 			= app.getAssetManager();
//		bulletMat 				= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		bullet 					= new Sphere(32, 32, 0.1f, true, false);
//	  	bulletTexKey			= new TextureKey("Textures/Terrain/Rock/Rock.PNG");
//	  	bulletCollisionShape 	= new SphereCollisionShape(0.4f);
//	  	bulletGeometry 			= new Geometry("bullet", bullet);

//		Spatial arrow			= assetManager.loadModel("Models/arrow.j3o");
//		bulletCollisionShape 	= new SphereCollisionShape(0.4f);
//	  	bulletGeometry 			= arrow;
	  	
//	  	bulletGeometry.setMaterial(bulletMat);
//	  	bulletTexKey.setGenerateMips(true);
//	  	Texture bulletTex = assetManager.loadTexture(bulletTexKey);
//	  	bulletMat.setTexture("ColorMap", bulletTex);
//	  	bullet.setTextureMode(TextureMode.Projected);
	  	
//	  	super.setBulletGeometry(bulletGeometry);
//	  	super.setBulletCollisionShape(bulletCollisionShape);
	}

//	public void use() {
		

//		shoot(bulletGeom, bulletCollisionShape);
//	}
}
