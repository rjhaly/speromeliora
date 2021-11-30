package speromeliora.model;

import java.util.ArrayList;

public class Task {
	int tsk_id;
	boolean isCompleted;
	boolean isBottomLevel;
	ArrayList<String> subTasks;
	String name;
	String identifier;
	ArrayList<String> teammates;
	
	public Task(int taskID, boolean completed, boolean bottomLevel, ArrayList<String> newSubTasks, String taskName, String taskIdentifier, ArrayList<String> newTeammates) {
		tsk_id = taskID;
		isCompleted = completed;
		isBottomLevel = bottomLevel;
		subTasks = newSubTasks;
		name = taskName;
		identifier = taskIdentifier;
		teammates = newTeammates;
	}

	public Task() {
	
	}
	public int getTaskID() {
		return tsk_id;
	}
	public void setTaskID(int taskID) {
		tsk_id = taskID;
	}
	public String getName() {
		return name;
	}
	public void setName(String taskName) {
		name = taskName;
	}
	public String getTaskIdentifier() {
		return identifier;
	}
	public void setTaskIdentifier(String taskIdentifier) {
		identifier = taskIdentifier;
	}
	public boolean getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(boolean completed) {
		isCompleted = completed;
	}
	public boolean getIsBottomLevel() {
		return isBottomLevel;
	}
	public void setIsBottomLevel(boolean bottomLevel) {
		isBottomLevel = bottomLevel;
	}
	public ArrayList<String> getSubTasks(){
		return subTasks;
	}
	public void setSubTasks(ArrayList<String> taskSubTasks) {
		subTasks = taskSubTasks;
	}
	public ArrayList<String> getTeammates(){
		return teammates;
	}
	public void setTeammates(ArrayList<String> taskTeammates) {
		teammates = taskTeammates;
	}
}
