package speromeliora.http;

public class RenameTaskRequest {
	String arg1, arg2, arg3;
	
	public String getArg1() { return arg1; }
	public String getArg2() { return arg2; }
	public String getArg3() { return arg3; }
	public void setArg1(String arg1) { this.arg1 = arg1; }
	public void setArg2(String arg2) { this.arg2 = arg2; }
	public void setArg3(String arg3) { this.arg3 = arg3; }

	public String toString() {
		return "Rename Task(" + arg1 + ")";
	}
	
	public RenameTaskRequest (String arg1, String arg2, String arg3) {
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
	}
	
	public RenameTaskRequest() {
	}
}
