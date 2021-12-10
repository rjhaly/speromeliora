package speromeliora.model;

import java.util.ArrayList;

public class Task {

	int tsk_id;
	String name;
	String identifier;
	ArrayList<String> subTasks;
	ArrayList<String> teammates;
	boolean isCompleted;
	boolean isBottomLevel;
	
	public Task(int tid, boolean completed, boolean bottomLevel, ArrayList<String> newSubTasks, String taskName, String taskIdentifier, ArrayList<String> newTeammates) {
		tsk_id = tid;
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
	
	@Override
	public String toString() {
		return identifier + " " + name;
	}
	
}
