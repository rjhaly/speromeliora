package speromeliora.http;

public class RenameTaskRequest {
	int arg1;
	String arg2;
	
	public int getArg1() { return arg1; }
	public String getArg2() { return arg2; }
	public void setArg1(int arg1) { this.arg1 = arg1; }
	public void setArg2(String arg2) { this.arg2 = arg2; }

	public String toString() {
		return "Rename Task(" + arg1 + ")";
	}
	
	public RenameTaskRequest (int arg1, String arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public RenameTaskRequest() {
	}
}
