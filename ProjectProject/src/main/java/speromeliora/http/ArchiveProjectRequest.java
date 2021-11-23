package speromeliora.http;

public class ArchiveProjectRequest {
	String arg1;

	public String getArg1() { return arg1; }
	public void setArg1(String arg1) { this.arg1 = arg1; }

	public String toString() {
		return "ArchiveProject(" + arg1 + ")";
	}
	
	public ArchiveProjectRequest (String arg1) {
		this.arg1 = arg1;
	}
	
	public ArchiveProjectRequest() {
	}
}
