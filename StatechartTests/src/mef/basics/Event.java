package mef.basics;

public class Event {

	private String name;
	private String output;
	
	public Event(String n, String o) {
		name = n;
		output = o;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String o) {
		output = o;
	}
	
	public boolean equals(Event other){
		return (this.name.equals(other.name));
	}
}
