/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.gui;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import testgame.appstates.TargetingAppState;
import testgame.controls.QualityControl;
import testgame.controls.ResourceControl;
import testgame.inventory.Inventory;
import testgame.target.TargetInformation;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.jme3.system.AppSettings;

/**
 *
 * @author jo_da12
 */
public class SimpleHud extends AbstractAppState {
	
	private static final Logger logger = Logger.getLogger(SimpleHud.class.getName());
    
    private Node 			hudRoot;
    private AppSettings 	settings;
    private AssetManager 	assetManager;
    private ViewPort 		guiViewPort;
	private BitmapFont 		hudFont;
	private BitmapText		inventoryText;
	private BitmapText 		targetInfoText;
	private TargetingAppState 		targetingAppState;
	private Inventory 		inventory;
	private int 			fontSize;
	private TargetInformation		targetInformation;
	private String			targetString = " ";
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        assetManager 		= app.getAssetManager();
        guiViewPort 		= app.getGuiViewPort();
        inventory			= app.getStateManager().getState(Inventory.class);
        targetingAppState	= app.getStateManager().getState(TargetingAppState.class);
     	hudFont 			= assetManager.loadFont("Interface/Fonts/Default.fnt");
    	fontSize 			= hudFont.getCharSet().getRenderedSize();

    	targetInfoText = new BitmapText(hudFont, false);
    	targetInfoText.setSize(fontSize);
    	inventoryText = new BitmapText(hudFont, false);
    	inventoryText.setSize(fontSize);
        inventoryText.setLocalTranslation(
        		settings.getWidth()-90, 
        		settings.getHeight(), 
        		0);
    	
        hudRoot.attachChild(targetInfoText);
    	hudRoot.attachChild(inventoryText);
    }
    
	public void init() {
		initCrosshair();
	}
    
    public SimpleHud (Node guiNode, BitmapFont guiFont,  AppSettings settings) {
        this.hudRoot 	= guiNode;
        this.settings 	= settings;
        this.hudFont 	= guiFont;
    }
    
    @Override
    public void update(float tpf) {
    	displayInventory();
    	
    	// show target info
     	targetInformation = targetingAppState.getTargetInformation();
     	if(targetInformation != null) {
     		logger.log(Level.INFO, "hud shows targetinfo");
     		
     		targetString = targetInformation.getName() + " " + (int) Math.round(targetInformation.getDistance()/10) + "m";
     		
     		// TEMP: check which controls we have...
     		for (Control control : targetInformation.getControls()) {
     		    logger.log(Level.INFO, control.toString());
     		    if(control instanceof ResourceControl) {
     		    	targetString += "\nResource: " + ((ResourceControl) control).getResourceName();
     		    	targetString += "\nQuantity: " + ((ResourceControl) control).getQuantity();
     		    }
     		    if(control instanceof QualityControl) {
     		    	targetString += "\nQuality: " + ((QualityControl) control).getQuality();
     		    }
     		}
     		
     		targetInfoText.setText(targetString);
     		targetInfoText.setLocalTranslation( // center under crosshair
	    			settings.getWidth() / 2 - targetInfoText.getLineWidth() / 2, // align center horiz
	    			settings.getHeight() / 2 - targetInfoText.getLineHeight()*2, 0);
     	} else {
     		targetInfoText.setText(" ");
     	}
    }
    
    private void displayInventory() {
    	Vector<String> items = inventory.getItemList();
    	Iterator<String> i = items.iterator();
    	String inventoryString = "Inventory:\n";
    	while(i.hasNext()) {
    		inventoryString += i.next() + "\n";
    	}
    	inventoryText.setText(inventoryString);
	}

    public void initCrosshair() {
        BitmapText ch = new BitmapText(hudFont, false);
        ch.setSize(fontSize*4/3);
        ch.setText("+"); // crosshairs
		ch.setLocalTranslation(settings.getWidth() / 2
				- hudFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        hudRoot.attachChild(ch);
    }
    
//    /**
//     * Checks if targets exists, then retrieves info from TargetInfo and displays
//     * on a gui node
//     */
//    public void displayTargetInfo() {
//    	if(targetingAppState.hasTarget()) { 
//    		targetInfoText.setText(targetingAppState.getTargetString());
//	    	targetInfoText.setLocalTranslation( // center under crosshair
//	    			settings.getWidth() / 2 - targetInfoText.getLineWidth() / 2,
//	    			settings.getHeight() / 2 - targetInfoText.getLineHeight()*2, 0);
//    	} else 
//    		targetInfoText.setText(" "); // it bugs if it's just ""
//   }
    
   public void setTargetInfoText(BitmapText text) {
	   targetInfoText = text;
   }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
