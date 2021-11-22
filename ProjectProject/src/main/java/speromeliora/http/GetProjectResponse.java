package speromeliora.http;

import speromeliora.model.Project;

public class GetProjectResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public GetProjectResponse (Project gottenProject, int statusCode) {
		this.project = gottenProject; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public GetProjectResponse (int statusCode, String errorMessage) {
		this.project = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "GetProject(" + project.getPid() + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
