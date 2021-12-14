package speromeliora.model;

import java.util.ArrayList;

public class Project {
	
	String pid;
	ArrayList<Task> tasks;
	ArrayList<String> teammates;
	boolean isArchived;
	
	public Project(String projectID, ArrayList<Task> newtasks, ArrayList<String> newteammates, boolean archived) {
		pid = projectID;
		tasks = newtasks;
		teammates = newteammates;
		isArchived = archived;
	}
	
	public Project() {}

	public String getPid() { return pid; }
	public ArrayList<Task> getTasks(){ return tasks; }
	public ArrayList<String> getTeammates() { return teammates; }
	public boolean getIsArchived() { return isArchived; }
	
	public void setPid(String projectID) { pid = projectID; }
	public void setTasks(ArrayList<Task> newTasks) { tasks = newTasks; }
	public void setTeammates(ArrayList<String> newTeammates) { teammates = newTeammates; }
	public void setIsArchived(boolean archived) { isArchived = archived; }
	
}
