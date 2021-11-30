package speromeliora.http;

import java.util.ArrayList;

import speromeliora.model.Project;

public class ListProjectResponse {
	public ArrayList<Project> projects;
	public int statusCode;
	public String error;
	
	public ListProjectResponse (ArrayList<Project> projects, int statusCode) {
		this.projects = projects; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public ListProjectResponse (int statusCode, String errorMessage) {
		this.projects = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "GetProject()";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
