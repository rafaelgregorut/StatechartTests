package mef.basics;

import java.util.ArrayList;

public class State {

	private String name;
	
	private ArrayList<State> substates;

	State(String n) {
		name = n;
		substates = new ArrayList<State>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<State> getSubstates() {
		return this.substates;
	}
	
	public void addSubstate(State sub) {
		substates.add(sub);
	}
	
	public boolean ehPai(State s) {
		for (State sub : substates) {
			if (sub.getName() == s.getName())
				return true;
		}
		return false;
	}
	
}
