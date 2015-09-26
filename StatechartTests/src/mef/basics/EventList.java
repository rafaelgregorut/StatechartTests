package mef.basics;
import java.util.ArrayList;
import java.util.Iterator;


public class EventList {

	private ArrayList<Event> list;
	
	private String type;
	
	public EventList() {
		list = new ArrayList<Event>();
		type = "";
	}
	
	public void add(Event e) {
		list.add(e);
	}
	
	public Event get(int index) {
		return list.get(index);
	}
	
	public void remove(int index) {
		list.remove(index);
	}
	
	public Iterator<Event> iterator() {
		return list.iterator();
	}
	
	public int size() {
		return list.size();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean contains(Event e) {
		return list.contains(e);
	}
	
	public void print() {
		int i = 0;
		for (i = 0; i < this.list.size() - 1; i++)
			System.out.print(this.list.get(i).getName()+",");
		System.out.println(this.list.get(i).getName());
	}
}
