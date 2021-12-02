package speromeliora.http;

import speromeliora.model.Project;

public class MarkTaskResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public MarkTaskResponse (Project updatedProject, int statusCode) {
		this.project = updatedProject;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public MarkTaskResponse (int statusCode, String errorMessage) {
		this.project = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "markTask(" + project + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
