package speromeliora.model;

import java.util.ArrayList;

public class Task {
	String tsk_id;
	boolean isInProgress;
	boolean isBottomLevel;
	ArrayList<String> subTasks;
	String name;
	String identifier;
	ArrayList<Teammate> teammates;
	
	public Task(String taskID, boolean inProgress, boolean bottomLevel, ArrayList<String> newSubTasks, String taskName, String taskIdentifier, ArrayList<Teammate> newTeammates) {
		tsk_id = taskID;
		isInProgress = inProgress;
		isBottomLevel = bottomLevel;
		subTasks = newSubTasks;
		name = taskName;
		identifier = taskIdentifier;
		teammates = newTeammates;
	}
}
