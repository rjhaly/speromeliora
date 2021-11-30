package speromeliora.http;

public class ArchiveProjectResponse {
	public String projectID;
	public int statusCode;
	public String error;
	
	public ArchiveProjectResponse (String pid, int statusCode) {
		this.projectID = "" + pid; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public ArchiveProjectResponse (int statusCode, String errorMessage) {
		this.projectID = ""; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "ArchiveProject(" + projectID + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
