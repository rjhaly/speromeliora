package speromeliora.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import speromeliora.model.Project;
import speromeliora.model.Task;
import speromeliora.model.Teammate;

public class ProjectDAO {
	java.sql.Connection conn;
	LambdaLogger logger;
    public ProjectDAO(LambdaLogger log) {
    	logger = log;
    	logger.log("makingDAO");
    	try  {
    		conn = DatabaseUtil.connect(logger);
    		logger.log("connected to database");
    	} catch (Exception e) {
    		logger.log("failed to connect to database");
    		e.printStackTrace();
    		conn = null;
    	}
    }

    public boolean createProject(String pid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects WHERE pid = ?;");
            ps.setString(1, pid);
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                resultSet.close();
                logger.log("Project name already in use");
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO projects (pid,isArchived) values(?,?);");
            ps.setString(1,  pid);
            ps.setBoolean(2,  false);
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to create project: " + e.getMessage());
        }
    }
    public Project getProject(String pid) throws Exception{
    	try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects WHERE pid = ?;");
            ps.setString(1, pid);
            ResultSet resultSet = ps.executeQuery();
            
            if(!resultSet.next()) {
                resultSet.close();
                logger.log("No project with name " + pid);
                return null;
            }
            else {
            	String name = pid;
            	ArrayList<String> tasks = new ArrayList<>();
            	ArrayList<String> teammates= new ArrayList<>();
            	boolean isArchived = resultSet.getBoolean("isArchived");
            	ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ?;");
            	ps.setString(1, pid);
            	resultSet = ps.executeQuery();
            	
            	while(resultSet.next()) {
            		String attribute;
            		if((attribute = resultSet.getString("tsk_id")) != null) {
            			tasks.add(attribute);
            		}
            		else {
            			attribute = resultSet.getNString("tmt_id");
            			teammates.add(attribute);
            		}
            	}
            	Project project = new Project(name, tasks, teammates, isArchived);
            	return project;
            }
            
        } catch (Exception e) {
            throw new Exception("Unable to retrieve Project: " + e.getMessage());
        }
    }
    public void archiveProject(String pid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE projects SET isArchived = true WHERE pid = ?;");
            ps.setString(1, pid);
            ps.execute();
        } catch (Exception e) {
            throw new Exception("Failed to archive project: " + e.getMessage());
        }
    }
    
    public void deleteProject(String pid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM projects WHERE pid = ?;");
            ps.setString(1, pid);
            ps.execute();
        } catch (Exception e) {
            throw new Exception("Failed to delete project: " + e.getMessage());
        }
    }
    public ArrayList<Project> listProjects() throws Exception{
    	ArrayList<Project> projects = new ArrayList<Project>();
    	try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects;");
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()) {
            	String pid = resultSet.getString("pid");
            	boolean isArchived = resultSet.getBoolean("isArchived");
            	ArrayList<String> tasks = new ArrayList<>();
            	ArrayList<String> teammates= new ArrayList<>();
            	
            	PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ?;");
            	ps1.setString(1, pid);
            	ResultSet resultSet1 = ps1.executeQuery();
            	
            	while(resultSet1.next()) {
            		String attribute;
            		if((attribute = resultSet1.getString("tsk_id")) != null) {
            			tasks.add(attribute);
            		}
            		else {
            			attribute = resultSet1.getNString("tmt_id");
            			teammates.add(attribute);
            		}
            	}
            	Project project = new Project(pid, tasks, teammates, isArchived);
            	projects.add(project);
            }
            return projects;
            
        } catch (Exception e) {
            throw new Exception("Unable to retrieve Project: " + e.getMessage());
        }
    }
}
