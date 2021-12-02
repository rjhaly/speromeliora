package speromeliora.http;

public class MarkTaskRequest {
	int arg1;

	public int getArg1() { return arg1; }
	public void setArg1(int arg1) { this.arg1 = arg1; }

	public String toString() {
		return "Mark Task(" + arg1 + ")";
	}
	
	public MarkTaskRequest (int arg1) {
		this.arg1 = arg1;
	}
	
	public MarkTaskRequest() {
	}
}
