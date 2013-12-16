/**
 * Todos (run watson -s dirty)
 * [todo] - Make bigger invisible meshes around targetable objects (e.g. arrows are thin and hard to target). Eg a Physics GhostObject
 * [todo] - Make arrows bounce of hard surfaces and stay in physics space, without glitching (dimensions are too small) 
 */

package testgame;

import testgame.appstates.HarvestingAppState;
import testgame.appstates.TargetingAppState;
import testgame.appstates.WeaponAppState;
import testgame.game.Game;
import testgame.gui.Hud;
import testgame.gui.SimpleHud;
import testgame.inventory.Inventory;
import testgame.player.Player;
import testgame.player.PlayerActions;
import testgame.player.PlayerInput;
import testgame.world.World;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

public class Main extends SimpleApplication {

    private World world;
    private Game game;
    private SimpleHud basicGui;
    private Player player;
    private BulletAppState bulletAppState;
    private HarvestingAppState harvestingAppState;
    private NiftyJmeDisplay gui;
    private TargetingAppState targetInfo;
    private Inventory inventory;
    private PlayerInput playerInput;
    private PlayerActions playerActions;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings appSettings = new AppSettings(true);
        appSettings.setTitle("JDs awesome game of fantastic adventures...");
        appSettings.setResolution(960, 600);
//        appSettings.setFullscreen(true);
        app.setShowSettings(false); // splash screen
        app.setSettings(appSettings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        gui = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        game = new Game();
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);

        // attach all statemanagers
        stateManager.attach(bulletAppState);
        stateManager.attach(game);
        stateManager.attach(new World(rootNode));
        stateManager.attach(new SimpleHud(guiNode, guiFont, settings));
        stateManager.attach(new Player(rootNode));
        stateManager.attach(new PlayerInput());
        stateManager.attach(new PlayerActions(rootNode));
        stateManager.attach(new Inventory(10));
        stateManager.attach(new TargetingAppState(rootNode));
        stateManager.attach(new HarvestingAppState());
        stateManager.attach(new WeaponAppState(rootNode));

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
       
//		flyCam.setMoveSpeed(30);
        // loadStartMenu();
        // this.loadStatsView();
        // this.loadFPSText();
        // this.setDisplayFps(true);
        // this.setDisplayStatView(false);
    }

    public void loadStartMenu() {
        /**
         * Create a new NiftyGUI object
         */
        Nifty nifty = gui.getNifty();
        /**
         * Read your XML and initialize your custom ScreenController
         */
        // nifty.fromXml("Interface/screen.xml", "start");
        nifty.fromXml("Interface/screen.xml", "start", new Hud(null));
        guiViewPort.addProcessor(gui);

        // disable the fly cam
//		flyCam.setDragToRotate(true);
        // gui.loadStartMenu();
    }

    @Override
    public void simpleUpdate(float tpf) {
        game.startGame(); // cannot be called in init
        // bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    }
}