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
                throw new Exception("Failed to create project: project name already exists");
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
            	ArrayList<String> identifiers = new ArrayList<>();
            	boolean isArchived = resultSet.getBoolean("isArchived");
            	ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ?;");
            	ps.setString(1, pid);
            	resultSet = ps.executeQuery();
            	
            	while(resultSet.next()) {
            		logger.log("found value in lookup table");
            		String attribute;
            		if(resultSet.getString("tsk_id") != null) {
            			logger.log("Looking at task id " + resultSet.getInt("tsk_id"));
            			PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
            			ps1.setInt(1, resultSet.getInt("tsk_id"));
            			ResultSet resultSet1 = ps1.executeQuery();
            			logger.log("" + resultSet1.next());
            			attribute = resultSet1.getNString("tsk_name");
            			tasks.add(attribute);
            			identifiers.add(resultSet1.getNString("tsk_identifier"));
            		}
            		else {
            			attribute = resultSet.getNString("tmt_id");
            			teammates.add(attribute);
            		}
            	}
            	Project project = new Project(name, tasks, teammates, identifiers, isArchived);
            	return project;
            }
            
        } catch (Exception e) {
            throw new Exception("Unable to retrieve Project: " + e.getMessage());
        }
    }
    public void archiveProject(String pid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects WHERE pid = ?;");
            ps.setString(1, pid);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	throw new Exception("Failed to archive project: " + "project ID does not exist");
            }
            logger.log("project found");
            ps =conn.prepareStatement("UPDATE projects SET isArchived = true WHERE pid = ?");
            ps.setNString(1, pid);
            ps.execute();
        } catch (Exception e) {
        	logger.log("exception thrown");
            throw new Exception("Failed to archive project: " + "project ID does not exist");
        }
    }
    
    public void deleteProject(String pid) throws Exception {
        try {
        	logger.log("deleting project: " + pid);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM projects WHERE pid = ?;");
            ps.setString(1, pid);
            ps.execute();
            ps = conn.prepareStatement("DELETE FROM lookup_table WHERE pid = ?;");
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
            	ArrayList<String> identifiers = new ArrayList<>();
            	
            	PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ?;");
            	ps1.setString(1, pid);
            	ResultSet resultSet1 = ps1.executeQuery();
            	
            	while(resultSet1.next()) {
            		String attribute;
            		if(resultSet1.getString("tsk_id") != null) {
            			logger.log("Looking at task id " + resultSet1.getInt("tsk_id"));
            			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
            			ps2.setInt(1, resultSet1.getInt("tsk_id"));
            			ResultSet resultSet2 = ps2.executeQuery();
            			logger.log("" + resultSet2.next());
            			attribute = resultSet2.getNString("tsk_name");
            			tasks.add(attribute);
            			identifiers.add(resultSet2.getNString("tsk_identifier"));
            		}
            		else {
            			attribute = resultSet1.getNString("tmt_id");
            			teammates.add(attribute);
            		}
            	}
            	Project project = new Project(pid, tasks, teammates, identifiers, isArchived);
            	projects.add(project);
            }
            return projects;
            
        } catch (Exception e) {
            throw new Exception("Unable to retrieve Project: " + e.getMessage());
        }
    }
    public Project addTasks(String[] tasks, String parentTaskIdentifier, String pid) throws Exception {
        try {
        	ArrayList<Task> newTasks = new ArrayList<Task>();
        	logger.log("starting to create task");
        	PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects WHERE pid = ?;");
        	ps.setNString(1, pid);
        	ResultSet resultSet = ps.executeQuery();
        	
        	if(!resultSet.next()) {
        		throw new Exception("Fai	led to add task: could not find project");
        	}	
        	for(int i = 0; i < tasks.length; i++) {
        		logger.log("creating a new task");
        		Task task = new Task();
        		task.setIsBottomLevel(true);;
        		task.setIsCompleted(false);
        		task.setSubTasks(new ArrayList<String>());
        		task.setTeammates(new ArrayList<String>());
        		task.setName(tasks[i]);
        		logger.log("made a new task");
        		ps = conn.prepareStatement(
        				"INSERT INTO tasks (tsk_name, isComplete, isBottomLevel, parent_tsk_id, tsk_identifier) "
        				+ "values(?,false,true,?,?);");
        		ps.setString(1,  tasks[i]);
        		if(parentTaskIdentifier != "") {
        			logger.log("creating task with parent");
        			PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_identifier = ?;");
        			ps1.setNString(1, parentTaskIdentifier);
        			ResultSet resultSet1 = ps1.executeQuery();
        			if(!resultSet1.next()) {
        				throw new Exception("Failed to add task: Parent task identifier not found");
        			}
        			ArrayList<Integer> taskIDs = new ArrayList<>();
        			logger.log("before first do");
        			do {
        				int taskID = resultSet1.getInt("tsk_id");
        				taskIDs.add(taskID);
        			}while(resultSet.next());
        			logger.log("after first do");
        			boolean taskFound = false;
        			int foundTaskID = -1;
        			String foundIdentifier = "";
        			for(int j = 0; i < taskIDs.size(); i++) {
        				ps1 = conn.prepareStatement("SELECT * FROM lookup_table WHERE tsk_id = ?;");
        				ps1.setInt(1, taskIDs.get(j));
        				resultSet1 = ps1.executeQuery();
        				
        				if(!resultSet1.next()) {
            			}
        				else {
        					if(resultSet1.getString("pid").equals(pid)) {
        						taskFound = true;
        						foundTaskID = taskIDs.get(j);
        					}
        				}
        			}
        			if(!taskFound) {
        				throw new Exception("Failed to add task: Parent task ID not associated with this project");
        			}
        			ps1 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
        			ps1.setInt(1, foundTaskID);
        			resultSet1 = ps1.executeQuery();
        			resultSet1.next();
        			foundIdentifier = resultSet1.getString("tsk_identifier");
        			
        			ps.setInt(2, foundTaskID);
        			ps1 = conn.prepareStatement("SELECT * FROM tasks WHERE parent_tsk_id = ?;");
        			ps1.setInt(1, foundTaskID);
        			resultSet1 = ps1.executeQuery();
        			
        			if(!resultSet1.next()) {
        				String newIdent = foundIdentifier + "." + 1;
        				ps.setNString(3, newIdent);
        			}
        			else {
        				resultSet1.last();
        				String lastIdent = resultSet1.getString("tsk_identifier");
        				String lastNum = lastIdent.substring(lastIdent.length() - 1);
        				int num = Integer.parseInt(lastNum);
        				int newNum = num++;
        				String newIdent = foundIdentifier + "." + newNum;
        				ps.setNString(3, newIdent);
        			}
        		}
        		else {
        			logger.log("creating task without parent");
        			ps.setNString(2, null);
        			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ?;",
                            ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
        			ps2.setNString(1, pid);
        			ResultSet resultSet2 = ps2.executeQuery();
        			if(!resultSet2.next()) {
        				logger.log("adding base task");
        				ps.setNString(3, "1");
            			task.setTaskIdentifier("1");
        			}
        			else {
        			logger.log("before last");
        			resultSet2.last();
        			logger.log("after last");
        			String lastTskID = resultSet2.getString("tsk_id");
        			logger.log("found task: " + lastTskID);
        			ps2 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
        			ps2.setNString(1, lastTskID);
        			resultSet2 = ps2.executeQuery();
        			resultSet2.next();
        			String lastIdent = resultSet2.getString("tsk_identifier");
        			logger.log("last identifier:" + lastIdent +"");
        			int identNum = Integer.parseInt(lastIdent); 
        			int newIdent = identNum + 1;
        			String newIdentifier = String.valueOf(newIdent);
        			ps.setNString(3, newIdentifier);
        			task.setTaskIdentifier(newIdentifier);
        		}}
	            ps.execute();
	            logger.log("added task to db");
	            ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id=(SELECT max(tsk_id) FROM tasks);");
	            resultSet = ps.executeQuery();
	            resultSet.next();
	            logger.log("executed querey");
	            int taskID = resultSet.getInt("tsk_id");
	            logger.log("retrieved taskID");
	            task.setTaskID(taskID);
	            logger.log("set Task id");
	            newTasks.add(task);
	            ps = conn.prepareStatement("INSERT INTO lookup_table (pid, tsk_id) values (?,?);");
	            ps.setNString(1, pid);
	            ps.setInt(2, taskID);
	            ps.execute();
	            logger.log("finished creating new task");
        	}
        	return getProject(pid);
        } catch (Exception e) {
            throw new Exception("Failed to add task: " + e.getMessage());
        }
    }
    
    public Project addTeammate(String pid, String teammateName) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects WHERE pid = ?;");
            ps.setString(1, pid);
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            if(!resultSet.next()) {
                resultSet.close();
                logger.log("No project with name " + pid);
                throw new Exception("Unable to retrieve Project: No project with given name");
            }
            else {
            	ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ? AND tmt_id = ?;");
            	ps.setNString(1, pid);
            	ps.setNString(2, teammateName);
            	resultSet = ps.executeQuery();
            	
            	while (resultSet.next()) {
                    resultSet.close();
                    logger.log("Teammate already assigned to this project");
                    throw new Exception("Failed to add teammate: teamate already added");
                }
            	ps = conn.prepareStatement("SELECT * FROM teammates WHERE tmt_id = ?;");
            	ps.setNString(1, teammateName);
            	resultSet = ps.executeQuery();
            	if(!resultSet.next()) {
            	logger.log("creating teammate in db");
            		ps = conn.prepareStatement("INSERT INTO teammates (tmt_id) values (?);");
            		ps.setNString(1,  teammateName);
            		ps.execute();
            	}
                logger.log("created teammate, adding into lookup table");
                ps = conn.prepareStatement("INSERT INTO lookup_table (pid,tmt_id) values(?,?);");
                ps.setNString(1, pid);
                ps.setString(2, teammateName);
                ps.execute();
                
                return getProject(pid);
            }
            
        } catch (Exception e) {
            throw new Exception("Unable to add Teammate: " + e.getMessage());
        }
    }
    public Project removeTeammate(String pid, String teammateName) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM lookup_table WHERE pid = ? && tmt_id = ?;");
            ps.setString(1, pid);
            ps.setNString(2, teammateName);
            ps.execute();
            return getProject(pid);
        } catch (Exception e) {
            throw new Exception("Failed to remove teammate: " + e.getMessage());
        }
    }
    
    public Project markTask(int tid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
            ps.setInt(1, tid);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	throw new Exception("Failed to mark task: " + "Unable to find task");
            }
            logger.log("task found");
            ps =conn.prepareStatement("UPDATE tasks SET isComplete = !isComplete WHERE tsk_id = ?");
            ps.setInt(1, tid);
            ps.execute();
            logger.log("task updated");
            ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE tsk_id = ?;");
            ps.setInt(1, tid);
            resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	throw new Exception("Failed to mark task: " + "could not find task");
            }
            String pid = resultSet.getString("pid");
            return getProject(pid);
            
            
        } catch (Exception e) {
        	logger.log("exception thrown");
            throw new Exception("Failed to mark task: " + "could not find task");
        }
    }
    
    public Project renameTask(int tid, String name) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
            ps.setInt(1, tid);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	throw new Exception("Failed to rename task: " + "Unable to find task");
            }
            logger.log("task found");
            ps =conn.prepareStatement("UPDATE tasks SET tsk_name = ? WHERE tsk_id = ?");
            ps.setString(1, name);
            ps.setInt(2, tid);
            ps.execute();
            logger.log("task updated");
            ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE tsk_id = ?;");
            ps.setInt(1, tid);
            resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	throw new Exception("Failed to rename task: " + "could not find task in lookup table");
            }
            String pid = resultSet.getString("pid");
            logger.log("returning project " + pid) ;
            Project updatedProject = getProject(pid);
            return updatedProject;
            //bros before hoes except without clothes
            
            
        } catch (Exception e) {
        	logger.log("exception thrown");
            throw new Exception("Failed to rename task: " + e.getMessage());
        }
    }
    }
