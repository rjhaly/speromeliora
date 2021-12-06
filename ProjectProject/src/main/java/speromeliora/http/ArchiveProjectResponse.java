package speromeliora.http;

import speromeliora.model.Project;

public class ArchiveProjectResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public ArchiveProjectResponse (Project project, int statusCode) {
		this.project = project; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public ArchiveProjectResponse (int statusCode, String errorMessage) {
		this.project = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "ArchiveProject(" + project.getPid() + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
