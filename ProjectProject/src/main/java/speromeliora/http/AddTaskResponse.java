package speromeliora.http;

import java.util.ArrayList;

import speromeliora.model.Task;

public class AddTaskResponse {
	public ArrayList<Task> tasks;
	public int statusCode;
	public String error;
	
	public AddTaskResponse (ArrayList<Task> tasks, int statusCode) {
		this.tasks = tasks; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddTaskResponse (int statusCode, String errorMessage) {
		this.tasks = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "AddedTasks(" + tasks.get(0) + "...)";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
