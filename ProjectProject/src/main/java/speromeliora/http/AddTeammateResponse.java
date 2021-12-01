package speromeliora.http;

import speromeliora.model.Project;

public class AddTeammateResponse {
	public String tmt_id;
	public int statusCode;
	public String error;
	
	public AddTeammateResponse (String teammateName, int statusCode) {
		this.tmt_id = teammateName; 
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddTeammateResponse (int statusCode, String errorMessage) {
		this.tmt_id = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "AddTeammate(" + tmt_id + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}
