package speromeliora.model;

import java.util.ArrayList;

public class Teammate {
	
	String name;
	ArrayList<String> tasks;
	
	public Teammate(String teammateName, ArrayList<String> assignedTasks) {
		name = teammateName;
		tasks = assignedTasks;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
