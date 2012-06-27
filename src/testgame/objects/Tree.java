package testgame.objects;

import testgame.controls.HarvestingControl;
import testgame.controls.LeavesLodControl;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
		setLeaves(app.getAssetManager()
				.loadModel("Models/tree/tree_leaves.j3o"));
		amount = 200;
		quality = 0.7;
		bark.setName("Tree");
		bark.setUserData("amount", amount);
		bark.setUserData("isHarvestable", true);
		leaves.setName("Tree Leaves");

		getLeaves().addControl(
				new LeavesLodControl(getLeaves(), app.getCamera()));
		getBark().addControl(new HarvestingControl(app));

		attachChild(getBark());
		attachChild(getLeaves());
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
		bark.setUserData("amount", amount);
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
