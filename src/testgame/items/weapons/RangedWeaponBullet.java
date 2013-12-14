package testgame.items.weapons;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;

public class RangedWeaponBullet {
	/**
	 * The spatial of the bullet, can be an arrow, bolt, bullet etc
	 */
	private Spatial					spatial;
	/**
	 * Mass of the bullet being shot. Affects velocity.
	 */
	private Float					bulletMass = 0.1f;
	/**
	 * The name in GUIs, eg "Arrow"
	 */
	private String					name;
	
	/**
	 * A bullet for a RangedWeapon
	 * @param name The name shown in GUIs
	 * @param spatial The spacial (model) used for this bullet
	 */
	public RangedWeaponBullet(String name, Spatial spatial) {
		this.setSpatial(spatial);
		this.setName(name);
	}

	/**
	 * The mass of the bullet
	 * @return <code>Float</code> mass
	 */
	public Float getMass() {
		return bulletMass;
	}

	public void setMass(float bulletMass) {
		this.bulletMass = bulletMass;
	}

	/**
	 * The spacial the bullet is using
	 * @return
	 */
	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}