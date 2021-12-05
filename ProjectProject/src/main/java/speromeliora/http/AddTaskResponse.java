package speromeliora.http;

import java.util.ArrayList;

import speromeliora.model.Project;
import speromeliora.model.Task;

public class AddTaskResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public AddTaskResponse (Project project, int statusCode) {
		this.project = project;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddTaskResponse (int statusCode, String errorMessage) {
		this.project = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "AddedTasks(" + project + ")";
			//fuck bitches get money get cash :)
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
