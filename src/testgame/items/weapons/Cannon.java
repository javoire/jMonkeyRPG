package testgame.items.weapons;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;

/**
 * Cannon weapon. Can not be instantiated before game runs.
 * @author Jonatan Dahl
 *
 */
public class Cannon extends WeaponRanged {
	
	private AssetManager 			assetManager;
	private Material 				bulletMat;
	private Sphere 					bullet;
	private TextureKey 				bulletTexKey;
	private SphereCollisionShape 	bulletCollisionShape;

	
	public Cannon(String name, Application app) {
		super(name, app);
		assetManager 			= app.getAssetManager();
		bulletMat 				= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		bullet 					= new Sphere(32, 32, 0.1f, true, false);
	  	bulletTexKey			= new TextureKey("Textures/Terrain/Rock/Rock.PNG");
	  	bulletCollisionShape	= new SphereCollisionShape(0.4f);
	}
	
	public void use() {
		Geometry bulletGeom 	= new Geometry("bullet", bullet);
		
		bulletGeom.setMaterial(bulletMat);
        bulletTexKey.setGenerateMips(true);
		Texture bulletTex = assetManager.loadTexture(bulletTexKey);
		bulletMat.setTexture("ColorMap", bulletTex);
		bullet.setTextureMode(TextureMode.Projected);

		shoot(bulletGeom, bulletCollisionShape);
	}
}
