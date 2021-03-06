package testgame.controls;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * This is supposed to control the behaviour of an arrow that is fired... 
 * @author Jonatan Dahl (based on BombControl by normenhansen)
 */
public class BulletRigidBodyControl extends RigidBodyControl implements PhysicsCollisionListener, PhysicsTickListener {

	private static final Logger logger = Logger.getLogger(BulletRigidBodyControl.class.getName());
	
    private float explosionRadius = 10;
    private PhysicsGhostObject ghostObject;
    private Vector3f vector = new Vector3f();
    private Vector3f vector2 = new Vector3f();
    private float forceFactor = 1;
    private ParticleEmitter effect;
    private float fxTime = 0.5f;
    private float maxFlyingTime = 30f; // sec
    private float curTime = -1.0f;
    private float timer;
    private Quaternion flyingRotation = new Quaternion();
    private PhysicsCollisionObject other;

	private Vector3f collisionPoint;

	private float zExtent;

    public BulletRigidBodyControl(CollisionShape shape, float mass) {
        super(shape, mass);
        createGhostObject();
    }

    public BulletRigidBodyControl(AssetManager manager, CollisionShape shape, float mass, Float zExtent) {
        super(shape, mass);
//        createGhostObject(); // this is for making an explosion when we hit something
    	this.zExtent = zExtent; // zExtent of the bounding box of the bullet spatial. we must get this from OUTSIDE the control for some reason. Otherwise it won't stay consistent

        prepareEffect(manager);
    }

    public void setPhysicsSpace(PhysicsSpace space) {
        super.setPhysicsSpace(space);
        if (space != null) {
            space.addCollisionListener(this);
        }
    }
    
    /**
     * Prepare a smokey impact effect
     * @param assetManager
     */
    private void prepareEffect(AssetManager assetManager) {
        int COUNT_FACTOR = 2;
        float COUNT_FACTOR_F = 1.3f;
        effect = new ParticleEmitter("Smoke", Type.Triangle, 32 * COUNT_FACTOR);
        effect.setSelectRandomImage(true);
        effect.setStartColor(new ColorRGBA(0.5f, 0.5f, 0.5f, (float) (1f / COUNT_FACTOR_F)));
        effect.setEndColor(new ColorRGBA(0.2f, .2f, .2f, 0f));
        effect.setStartSize(0.1f);
        effect.setEndSize(0.2f);
        effect.setShape(new EmitterSphereShape(Vector3f.ZERO, 0.1f));
        effect.setParticlesPerSec(0);
        effect.setGravity(0, 0, 0);
        effect.setLowLife(.1f);
        effect.setHighLife(.2f);
        effect.setImagesX(2);
        effect.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        effect.setMaterial(mat);
    }

    protected void createGhostObject() {
        ghostObject = new PhysicsGhostObject(new SphereCollisionShape(explosionRadius));
    }

    public void collision(PhysicsCollisionEvent event) {
        if (space == null || this.isEnabled() == false) {
            return;
        }
        if (event.getObjectA() == this || event.getObjectB() == this) {
        	this.setEnabled(false); // removes it from physics space: "freezes" the bullet where it lands. this will also disable the "update" method of this class

        	if (event.getObjectA() == this) { // get what we collided with 
        		other = event.getObjectB();
        		collisionPoint = event.getPositionWorldOnB();
        	} else {
        		other = event.getObjectA();
        		collisionPoint = event.getPositionWorldOnA();
        	}

        	logger.log(Level.INFO, "Collided with: " + other.toString());

        	Float cpDistance = ((Node) spatial).getChild(0).getWorldTranslation().distance(collisionPoint);
        	// [todo] - This should also be calculated from an objects hardness, eg grass/dirt is softer, thus an arrow will stick deeper
        	Float stickDepthModifier = (this.getLinearVelocity().length() / 800 * 3f); // how deep it will stick into the object it hits
        	if(stickDepthModifier < 0.3f) { stickDepthModifier = 0.3f; }; // min
        	((Node) spatial).getChild(0).setLocalTranslation(new Vector3f(0,0,cpDistance-zExtent*(1-stickDepthModifier))); // negative offset the arrow on Z related to collisionPoint by slightly lesser than it's z extent (so it sticks "into" a tree for example)  
        	spatial.addControl(new StaticBulletControl());
        	spatial.addControl(new TargetableControl("Arrow"));
        	spatial.addControl(new QualityControl(0.9f));

            // smoke effect
            if (effect != null && spatial.getParent() != null) {
            	curTime = 0;
            	effect.setLocalTranslation(new Vector3f(0,0,spatial.getLocalTranslation().distance(collisionPoint)));
            	((Node) spatial).attachChild(effect); // spatial is actually arrowNode
            	effect.emitAllParticles();
            }
        }
    }
    
    public void prePhysicsTick(PhysicsSpace space, float f) {
//        space.removeCollisionListener(this);
    }

    // this is to eg. make a brick wall fall down, send a force impulse outwards
    public void physicsTick(PhysicsSpace space, float f) {
        //get all overlapping objects and apply impulse to them
//        for (Iterator<PhysicsCollisionObject> it = ghostObject.getOverlappingObjects().iterator(); it.hasNext();) {            
//            PhysicsCollisionObject physicsCollisionObject = it.next();
//            if (physicsCollisionObject instanceof PhysicsRigidBody) {
//                PhysicsRigidBody rBody = (PhysicsRigidBody) physicsCollisionObject;
//                rBody.getPhysicsLocation(vector2);
//                vector2.subtractLocal(vector);
//                float force = explosionRadius - vector2.length();
//                force *= forceFactor;
//                force = force > 0 ? force : 0;
//                vector2.normalizeLocal();
//                vector2.multLocal(force);
//                ((PhysicsRigidBody) physicsCollisionObject).applyImpulse(vector2, Vector3f.ZERO);
//            }
//        }
//        space.removeTickListener(this);
//        space.remove(ghostObject);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        timer+=tpf;
        if(enabled){
        	// point arrow towards where it is going
        	flyingRotation.lookAt(this.getLinearVelocity(), new Vector3f(0,1,0));
        	this.setPhysicsRotation(flyingRotation);
        	
        	// this is to cleanup old bullets that hasn't collided yet (lived more than maxTime)
            if(timer>maxFlyingTime){
                if(spatial.getParent()!=null){
                	spatial.removeFromParent();
                	if (space != null) {
                		space.removeCollisionListener(this);
                		space.remove(this);
                	}
                }
            }
        }
        if (enabled && curTime >= 0) {
            curTime += tpf;
            if (curTime > fxTime) {
                curTime = -1;
                effect.removeFromParent();
            }
        }
    }

    /**
     * @return the explosionRadius
     */
    public float getExplosionRadius() {
        return explosionRadius;
    }

    /**
     * @param explosionRadius the explosionRadius to set
     */
    public void setExplosionRadius(float explosionRadius) {
        this.explosionRadius = explosionRadius;
        createGhostObject();
    }

    public float getForceFactor() {
        return forceFactor;
    }

    public void setForceFactor(float forceFactor) {
        this.forceFactor = forceFactor;
    }
    
    
    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Reading not supported.");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Saving not supported.");
    }
}