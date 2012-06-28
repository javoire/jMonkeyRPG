package testgame.items;

public class Item {

	private String name;
	private double quality;
	
	public Item(String name) {
		this.name = name;
		this.quality = 1.0; // default
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
