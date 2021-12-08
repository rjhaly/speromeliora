package speromeliora.http;

public class GetTaskRequest {
	
	String arg1, arg2;
	
	public GetTaskRequest (String arg1, String arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public GetTaskRequest() {}
	
	public String getArg1() { return arg1; }
	public String getArg2() { return arg2; }
	
	public void setArg1(String arg1) { this.arg1 = arg1; }
	public void setArg2(String arg2) { this.arg2 = arg2; }

	@Override
	public String toString() {
		return "GetTask(" + arg1 + ", " + arg2 + ")";
	}
	
}
