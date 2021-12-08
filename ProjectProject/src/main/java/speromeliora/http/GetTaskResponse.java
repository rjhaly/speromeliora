package speromeliora.http;

import speromeliora.model.Task;

public class GetTaskResponse {
	public Task task;
	public int statusCode;
	public String error;
	
	public GetTaskResponse (Task gottenTask, int statusCode) {
		this.task = gottenTask; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public GetTaskResponse (int statusCode, String errorMessage) {
		this.task = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "GetTask(" + task.getName() + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
	
}
