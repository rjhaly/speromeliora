package speromeliora.http;

public class CreateProjectResponse {
	public String projectID;
	public int statusCode;
	public String error;
	
	public CreateProjectResponse (String pid, int statusCode) {
		this.projectID = "" + pid; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public CreateProjectResponse (int statusCode, String errorMessage) {
		this.projectID = ""; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "CreatedProject(" + projectID + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
