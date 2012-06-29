package testgame.objects;

import testgame.controls.HarvestingControl;
import testgame.controls.LeavesLodControl;
import testgame.items.resources.Resource.ResourceType;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Tree extends Node {

	private Spatial bark;
	private Spatial leaves;

	public Tree(Application app) {
		setBark(app.getAssetManager().loadModel("Models/tree/tree_bark.j3o"));
		setLeaves(app.getAssetManager()
				.loadModel("Models/tree/tree_leaves.j3o"));
		bark.setName("Tree");
		leaves.setName("Tree Leaves");

		leaves.addControl(
				new LeavesLodControl(leaves, app.getCamera()));
		HarvestingControl woodHarvester = new HarvestingControl(ResourceType.WOOD);
		woodHarvester.setQuantity(200);
		HarvestingControl foliageHarvester = new HarvestingControl(ResourceType.FOLIAGE);
		foliageHarvester.setQuantity(200);
		foliageHarvester.setMinDistance(4);
		leaves.addControl(foliageHarvester);
		bark.addControl(woodHarvester);

		attachChild(getBark());
		attachChild(getLeaves());
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
