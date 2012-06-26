package testgame.objects;

import testgame.controls.HarvestingControl;
import testgame.game.LeavesLodControl;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class Tree extends Node {
	
	private Spatial bark;
	private Spatial leaves;
	/**
	 * how much wood can be harvested
	 */
	private int amount;
	/**
	 * modifier for how much wood that is harvested per hit
	 */
	private double quality;
	
	public Tree(Application app) {
		setBark(app.getAssetManager().loadModel("Models/tree/tree_bark.j3o"));
    	setLeaves(app.getAssetManager().loadModel("Models/tree/tree_leaves.j3o"));
    	amount = 200;
    	quality = 0.7;
    	
    	Control leavesLodControl = new LeavesLodControl(getLeaves(), app.getCamera());
    	Control HarvestingControl = new HarvestingControl();
    	getLeaves().addControl(leavesLodControl);
    	getBark().addControl(HarvestingControl);
    	
    	attachChild(getBark());
    	attachChild(getLeaves());
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public Spatial getBark() {
		return bark;
	}

	public void setBark(Spatial bark) {
		this.bark = bark;
	}

	public Spatial getLeaves() {
		return leaves;
	}

	public void setLeaves(Spatial leaves) {
		this.leaves = leaves;
	}
}
