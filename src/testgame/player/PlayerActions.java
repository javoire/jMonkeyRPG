package testgame.player;

import testgame.player.controls.PlayerEquipmentControl;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class PlayerActions extends AbstractAppState {
	
    private Camera cam;
	private Material bulletMat;
	private AssetManager assetManager;
	private Node rootNode;
	private BulletAppState bulletAppState;
	private Player player;
	private PlayerEquipmentControl playerEquipment;
	private static Sphere bullet;
	private static SphereCollisionShape bulletCollisionShape;

	public PlayerActions(Node rootNode) {
		this.rootNode = rootNode;
	}

	@Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        cam 			= app.getCamera();
        assetManager	= app.getAssetManager();
        bulletAppState 	= app.getStateManager().getState(BulletAppState.class);
        player			= app.getStateManager().getState(Player.class);
        playerEquipment = player.getEquipmentControl();
    }
    
	public void useMainHand() {
		if(playerEquipment.getMainHand() != null)
			playerEquipment.getMainHand().use();
    }
}
