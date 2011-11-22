/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author jo_da12
 */
public class Player extends AbstractAppState implements ActionListener {
    
    private CharacterControl    playerControl;
    private InputManager        inputManager;
    private BulletAppState      bulletAppState;
    private Camera              cam;
    
    private Vector3f            walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;



    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
                
        inputManager   = app.getInputManager();
        bulletAppState = app.getStateManager().getState(BulletAppState.class);
        cam            = app.getCamera();

        // create player object
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);        
        playerControl = new CharacterControl(capsuleShape, 0.05f);

        
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        Vector3f camDir = cam.getDirection().clone().multLocal(0.4f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.2f);
        
        walkDirection.set(0, 0, 0);
        
        if (left)  { walkDirection.addLocal(camLeft); }
        if (right) { walkDirection.addLocal(camLeft.negate()); }
        if (up)    { walkDirection.addLocal(camDir); }
        if (down)  { walkDirection.addLocal(camDir.negate()); }
        
        playerControl.setWalkDirection(walkDirection);
        
        cam.setLocation(playerControl.getPhysicsLocation());
// 
    }
    
       /** These are our custom actions triggered by key presses.
    * We do not walk yet, we just keep track of the direction the user pressed. */
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            if (value) { left = true; } else { left = false; }
        } else if (binding.equals("Right")) {
            if (value) { right = true; } else { right = false; }
        } else if (binding.equals("Up")) {
            if (value) { up = true; } else { up = false; }
        } else if (binding.equals("Down")) {
            if (value) { down = true; } else { down = false; }
        } else if (binding.equals("Jump")) {
            playerControl.jump();
        }
    }
    
    
    public void initPlayer() {

        playerControl.setJumpSpeed(30);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(90);
        playerControl.setPhysicsLocation(new Vector3f(20, 10, 0));

        bulletAppState.getPhysicsSpace().add(playerControl);
        
        initKeyBindings();
    }
    
    private void initKeyBindings() {
        
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");

    }
       
    
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
