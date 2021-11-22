package speromeliora.model;

import java.util.ArrayList;

public class Project {
	String pid;
	ArrayList<String> tasks;
	ArrayList<String> teammates;
	boolean isArchived;
	
	public Project(String projectID, ArrayList<String> newtasks, ArrayList<String> newteammates, boolean archived) {
		pid = projectID;
		tasks = newtasks;
		teammates = newteammates;
		isArchived = archived;
	}
	
	public Project() {
		
	}

	public String getPid() {
		return pid;
	}
	public void setPid(String projectID) {
		pid = projectID;
	}
	public ArrayList<String> getTasks(){
		return tasks;
	}
	public void setTasks(ArrayList<String> newTasks) {
		tasks = newTasks;
	}
	public ArrayList<String> getTeammates(){
		return teammates;
	}
	public void setTeammates(ArrayList<String> newTeammates) {
		teammates = newTeammates;
	}
	public boolean getIsArchived() {
		return isArchived;
	}
	public void setIsArchived(boolean archived) {
		isArchived = archived;
	}
}
