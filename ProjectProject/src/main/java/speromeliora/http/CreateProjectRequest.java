package speromeliora.http;

public class CreateProjectRequest {
	String arg1;

	public String getArg1() { return arg1; }
	public void setArg1(String arg1) { this.arg1 = arg1; }

	public String toString() {
		return "CreateProject(" + arg1 + ")";
	}
	
	public CreateProjectRequest (String arg1) {
		this.arg1 = arg1;
	}
	
	public CreateProjectRequest() {
	}
}
