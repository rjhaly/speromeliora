package speromeliora.http;

public class DeleteProjectRequest {
	String arg1;

	public String getArg1() { return arg1; }
	public void setArg1(String arg1) { this.arg1 = arg1; }

	public String toString() {
		return "DeleteProject(" + arg1 + ")";
	}
	
	public DeleteProjectRequest (String arg1) {
		this.arg1 = arg1;
	}
	
	public DeleteProjectRequest() {
	}
}
