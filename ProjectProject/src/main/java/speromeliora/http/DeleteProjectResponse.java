package speromeliora.http;

public class DeleteProjectResponse {
	public int statusCode;
	public String error;
	
	public DeleteProjectResponse (int statusCode) {
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public DeleteProjectResponse (int statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "DeleteProject()";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
