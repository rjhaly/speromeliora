package speromeliora.demo;

import speromeliora.db.ProjectDAO;

public class clearDB {
	public clearDB() {
		
	}
	
	public void nukeDB() {
		
	ProjectDAO dao = new ProjectDAO();
	dao.emptyDB();
	}
}
