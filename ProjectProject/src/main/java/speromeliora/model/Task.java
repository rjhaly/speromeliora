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

	public Task() {}
	
	public int getTaskID() { return tsk_id; }
	public String getName() { return name; }
	public String getTaskIdentifier() { return identifier; }
	public boolean getIsCompleted() { return isCompleted; }
	public boolean getIsBottomLevel() { return isBottomLevel; }
	public ArrayList<String> getSubTasks() { return subTasks; }
	public ArrayList<String> getTeammates() { return teammates; }

	public void setTaskID(int taskID) { tsk_id = taskID; }
	public void setName(String taskName) { name = taskName; }
	public void setTaskIdentifier(String taskIdentifier) { identifier = taskIdentifier; }
	public void setIsCompleted(boolean completed) { isCompleted = completed; }
	public void setIsBottomLevel(boolean bottomLevel) { isBottomLevel = bottomLevel; }
	public void setSubTasks(ArrayList<String> taskSubTasks) { subTasks = taskSubTasks; }
	public void setTeammates(ArrayList<String> taskTeammates) { teammates = taskTeammates; }
}
