/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import java.util.Iterator;
import java.util.Vector;

import testgame.appstates.TargetInfo;
import testgame.inventory.Inventory;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 *
 * @author jo_da12
 */
public class BasicGui extends AbstractAppState {
    
    private Node guiNode;
    private AppSettings settings;
    private AssetManager assetManager;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private BitmapFont guiFont;
    private FlyByCamera flyCam;
	private int fontSize;
	private BitmapText targetInfo;
	private BitmapText harvestInfo;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.assetManager 	= app.getAssetManager();
        this.inputManager 	= app.getInputManager();
        this.audioRenderer 	= app.getAudioRenderer();
        this.guiViewPort 	= app.getGuiViewPort();
        
     	guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    	fontSize = guiFont.getCharSet().getRenderedSize();
    	targetInfo = new BitmapText(guiFont, false);
    	targetInfo.setSize(fontSize);
        targetInfo.setLocalTranslation( // center under crosshair
  	          settings.getWidth() / 2 - targetInfo.getLineWidth() / 2,
  	          settings.getHeight() / 2 - targetInfo.getLineHeight()*2, 0);
    	harvestInfo = new BitmapText(guiFont, false);
    	harvestInfo.setSize(fontSize);
        harvestInfo.setLocalTranslation( // center under crosshair
        		settings.getWidth() / 2 - targetInfo.getLineWidth() / 2,
        		settings.getHeight() / 2 - targetInfo.getLineHeight()*4, 0);
    	guiNode.attachChild(targetInfo);
    	guiNode.attachChild(harvestInfo);
    }
    
    public BasicGui (Node guiNode, BitmapFont guiFont,  AppSettings settings, FlyByCamera flyCam) {
        this.guiNode 	= guiNode;
        this.settings 	= settings;
        this.guiFont 	= guiFont;
        this.flyCam 	= flyCam;
        
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    public void initGui() {
   
    }
    
    public void initCrosshair() {
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(fontSize*4/3);
        ch.setText("+"); // crosshairs
		ch.setLocalTranslation(settings.getWidth() / 2
				- guiFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
    
    /**
     * Displays info on the screen from a string.
     * @param info Targetinfo object
     */
    public void displayTargetInfo(TargetInfo info) {
    	targetInfo.setText(info.getString());
    }
    
    public void initInventory(Inventory inventory) {
    	Vector<String> items = inventory.getItemList();
    	Iterator<String> i = items.iterator();
    	int j = 1;
    	while(i.hasNext()) {
	    	BitmapText ch = new BitmapText(guiFont, false);
	    	ch.setSize(fontSize);
	    	ch.setText(i.next());
	        ch.setLocalTranslation(
	        		settings.getWidth()-90, 
	        		settings.getHeight()-ch.getLineHeight()*j, 
	        		0);
	        j += 1;
	    	guiNode.attachChild(ch);
    	}
    }
    
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

	public void clearTargetInfo() {
		targetInfo.setText("");
	}
}
