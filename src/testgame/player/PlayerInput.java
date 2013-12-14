package testgame.player;

import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.appstates.HarvestingAppState;
import testgame.appstates.WeaponAppState;
import testgame.inventory.Inventory;
import testgame.items.weapons.Weapon;
import testgame.player.controls.PlayerEquipmentControl;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class PlayerInput extends AbstractAppState implements ActionListener, AnalogListener {
	
	private static final Logger logger = Logger.getLogger(PlayerInput.class.getName());
	
	public enum KeyMap{
	    LEFT("Left"),
	    RIGHT("Right"),
	    UP("Up"),
	    DOWN("Down"),
	    JUMP("Jump"),
	    USEMAINHAND("UseMainHand"),
	    HARVEST("Harvest"),
	    SPRINT("Sprint"),
	    SLOT_1("Slot1"),
	    SLOT_2("Slot2");

	    private KeyMap(final String text) {
	        this.text = text;
	    }

	    private final String text;

	    @Override
	    public String toString() {
	        return text;
	    }
	}    

    private Player 					player;
	private HarvestingAppState 		harvester;
	private InputManager 			inputManager;
    private Vector3f 				walkDirection = new Vector3f();
	private Camera 					cam;
	private CharacterControl 		playerControl;
	private PlayerActions 			playerActions;
	private Inventory 				inventory;
	private PlayerEquipmentControl 	playerEquipment;
	private float 					sprintMultiplier = 3f;
	private WeaponAppState			weaponAppState;
	private boolean left = false, right = false, up = false, down = false, sprint = false;

	@Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        player 			= app.getStateManager().getState(Player.class);
        harvester		= app.getStateManager().getState(HarvestingAppState.class);
        inputManager   	= app.getInputManager();
        cam				= app.getCamera();
        playerActions	= app.getStateManager().getState(PlayerActions.class);
        inventory		= app.getStateManager().getState(Inventory.class);
        weaponAppState 	= app.getStateManager().getState(WeaponAppState.class); 
        playerControl 	= player.getPlayerControl();
        playerEquipment = player.getEquipmentControl();
        initKeyBindings();
    }
	
    @Override
    public void update(float tpf) {
        Vector3f camDir 	= cam.getDirection().clone().multLocal(0.4f);
        Vector3f camLeft 	= cam.getLeft().clone().multLocal(0.2f);
        
        walkDirection.set(0, 0, 0);
        
        if (left)  { walkDirection.addLocal(camLeft.setY(0)); } // set y 0 to not move player upwards.
        if (right) { walkDirection.addLocal(camLeft.setY(0).negate()); }
        if (up)    { walkDirection.addLocal(camDir.setY(0)); }
        if (down)  { walkDirection.addLocal(camDir.setY(0).negate()); }
        
        playerControl.setWalkDirection(walkDirection);
        
        cam.setLocation(playerControl.getPhysicsLocation());
    }

	@Override
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals(KeyMap.LEFT.toString())) {
            if (isPressed) { left = true; } else { left = false; }
        } 

		if (binding.equals(KeyMap.RIGHT.toString())) {
            if (isPressed) { right = true; } else { right = false; }
        } 

		if (binding.equals(KeyMap.UP.toString())) {
            if (isPressed) { up = true; } else { up = false; }
        } 

		if (binding.equals(KeyMap.DOWN.toString())) {
            if (isPressed) { down = true; } else { down = false; }
        } 

		if (binding.equals(KeyMap.JUMP.toString())) {
            playerControl.jump();
	    } 

		if (binding.equals(KeyMap.USEMAINHAND.toString()) && isPressed) { // starting to fire weapon
//	    	playerActions.useMainHand();
	    	weaponAppState.chargeWeapon(); // rather "use main hand" ...
	    } else if (binding.equals(KeyMap.USEMAINHAND.toString()) && !isPressed) { // firing the weapon
//	    	playerActions.useMainHand();
	    	weaponAppState.fireWeapon(); // rather "use main hand" ...
//	    } else if (binding.equals(KeyMap.HARVEST.toString()) && !value) {
//	    	harvester.tryHarvest();
	    } else if (binding.equals(KeyMap.SLOT_1.toString()) && !isPressed) {
	    	Weapon mainHand = inventory.getQuickslot(0);
	    	if(mainHand != null)
	    		playerEquipment.setMainHand(mainHand);
		} else if (binding.equals(KeyMap.SLOT_2.toString()) && !isPressed) {
	    	Weapon mainHand = inventory.getQuickslot(1);
	    	if(mainHand != null)
	    		playerEquipment.setMainHand(mainHand);
		}
	}

	@Override
	public void onAnalog(String binding, float isPressed, float tpf) {
//		if(binding.equals(KeyMap.USEMAINHAND_START))x
	}
    
	private void initKeyBindings() {
		inputManager.addMapping(KeyMap.LEFT.toString(), new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(KeyMap.RIGHT.toString(), new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping(KeyMap.UP.toString(), new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(KeyMap.DOWN.toString(), new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(KeyMap.JUMP.toString(), new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping(KeyMap.USEMAINHAND.toString(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(KeyMap.HARVEST.toString(), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(KeyMap.SLOT_1.toString(), new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping(KeyMap.SLOT_2.toString(), new KeyTrigger(KeyInput.KEY_2));
		inputManager.addListener(this, KeyMap.LEFT.toString());
		inputManager.addListener(this, KeyMap.RIGHT.toString());
		inputManager.addListener(this, KeyMap.UP.toString());
		inputManager.addListener(this, KeyMap.DOWN.toString());
		inputManager.addListener(this, KeyMap.JUMP.toString());
		inputManager.addListener(this, KeyMap.USEMAINHAND.toString());
		inputManager.addListener(this, KeyMap.HARVEST.toString());
		inputManager.addListener(this, KeyMap.SLOT_1.toString());
		inputManager.addListener(this, KeyMap.SLOT_2.toString());
	}
}
