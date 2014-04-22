package model;

public class Statistics {
	private String name;
	private String value;
	
	Statistics(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return name + ":  " + value;
	}
}
