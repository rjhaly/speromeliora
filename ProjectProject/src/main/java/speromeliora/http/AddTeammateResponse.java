package speromeliora.http;

import speromeliora.model.Project;

public class AddTeammateResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public AddTeammateResponse (Project updatedProject, int statusCode) {
		this.project = updatedProject;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddTeammateResponse (int statusCode, String errorMessage) {
		this.project = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "AddTeammate(" + project + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
