package speromeliora.model;

import java.util.ArrayList;

public class Project {
	
	String pid;
	ArrayList<String> tasks;
	ArrayList<String> teammates;
	ArrayList<String> identifiers;
	boolean isArchived;
	
	public Project(String projectID, ArrayList<String> newtasks, ArrayList<String> newteammates, ArrayList<String> newIdentifiers, boolean archived) {
		pid = projectID;
		tasks = newtasks;
		teammates = newteammates;
		identifiers = newIdentifiers;
		isArchived = archived;
	}
	
	public Project() {}

	public String getPid() { return pid; }
	public ArrayList<String> getTasks(){ return tasks; }
	public ArrayList<String> getTeammates() { return teammates; }
	public ArrayList<String> getIdentifiers(){ return identifiers; }
	public boolean getIsArchived() { return isArchived; }
	
	public void setPid(String projectID) { pid = projectID; }
	public void setTasks(ArrayList<String> newTasks) { tasks = newTasks; }
	public void setTeammates(ArrayList<String> newTeammates) { teammates = newTeammates; }
	public void setIdentifiers(ArrayList<String> newIdentifiers) { identifiers = newIdentifiers; }
	public void setIsArchived(boolean archived) { isArchived = archived; }
	
}
