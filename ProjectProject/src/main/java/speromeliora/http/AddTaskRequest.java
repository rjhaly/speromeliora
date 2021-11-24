package speromeliora.http;

import java.util.ArrayList;

public class AddTaskRequest {
	ArrayList<String> arg1;
	String arg2;

	public ArrayList<String> getArg1() { return arg1; }
	public String getArg2() { return arg2; }
	public void setArg1(ArrayList<String> arg1) { this.arg1 = arg1; }
	public void setArg2(String arg2) { this.arg2 = arg2; }

	public String toString() {
		return "Add Tasks(" + arg1.get(0) + "...)";
	}
	
	public AddTaskRequest (ArrayList<String> arg1, String arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public AddTaskRequest() {
	}
}
