package speromeliora.http;

public class RemoveTeammateResponse {
	public String pid;
	public int statusCode;
	public String error;
	
	public RemoveTeammateResponse (String pid, int statusCode) {
		this.pid = pid; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public RemoveTeammateResponse (int statusCode, String errorMessage) {
		this.pid = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "AddTeammate(" + pid + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
