package speromeliora.demo;

import java.sql.SQLException;

import speromeliora.db.ProjectDAO;

public class clearDB {
	public clearDB() {
		
	}
	
	public void nukeDB() throws SQLException {
		
	ProjectDAO dao = new ProjectDAO();
	dao.emptyDB();
	}
}
