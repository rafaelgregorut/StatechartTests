package mef.basics;

public class State {

	private String name;

	State(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
