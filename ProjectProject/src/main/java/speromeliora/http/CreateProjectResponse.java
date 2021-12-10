package speromeliora.http;

import speromeliora.model.Project;

public class CreateProjectResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public CreateProjectResponse(Project createdProject, int statusCode) {
		this.project = createdProject; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public CreateProjectResponse(int statusCode, String errorMessage) {
		this.project = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "CreateProject(" + project.getPid() + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
