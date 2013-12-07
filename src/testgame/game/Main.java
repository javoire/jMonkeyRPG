package testgame.game;

import java.util.logging.Level;

import testgame.appstates.HarvestingAppState;
import testgame.appstates.TargetInfo;
import testgame.gui.Hud;
import testgame.gui.SimpleHud;
import testgame.inventory.Inventory;
import testgame.player.Player;
import testgame.player.PlayerActions;
import testgame.player.PlayerInput;

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
    private TargetInfo targetInfo;
    private Inventory inventory;
    private PlayerInput playerInput;
    private PlayerActions playerActions;

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("").setLevel(Level.WARNING);
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
        world = new World(rootNode);
        game = new Game();
        basicGui = new SimpleHud(guiNode, guiFont, settings);
        gui = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        player = new Player(rootNode);
        bulletAppState = new BulletAppState();
        harvestingAppState = new HarvestingAppState();
        targetInfo = new TargetInfo();
        inventory = new Inventory(10);
        playerInput = new PlayerInput();
        playerActions = new PlayerActions(rootNode);

        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);

        // attach all statemanagers
        stateManager.attach(bulletAppState);
        stateManager.attach(world);
        stateManager.attach(game);
        stateManager.attach(basicGui);
        stateManager.attach(player);
        stateManager.attach(playerInput);
        stateManager.attach(playerActions);
        stateManager.attach(inventory);
        stateManager.attach(targetInfo);
        stateManager.attach(harvestingAppState);

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