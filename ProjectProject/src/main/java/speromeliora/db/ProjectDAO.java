package speromeliora.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    public int findTaskID(String pid, String identifier) throws Exception{
    	PreparedStatement ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ?;");
    	ps.setNString(1, pid);
    	ResultSet resultSet = ps.executeQuery();
    	int tid = -1;
    	while(resultSet.next()) {
    		int currentTid = resultSet.getInt("tsk_id");
    		ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
    		ps.setInt(1, currentTid);
    		ResultSet resultSet1 = ps.executeQuery();
    		if(resultSet1.next()) {
    		if(resultSet1.getString("tsk_identifier").equals(identifier)) {
    			tid = currentTid;
    		}}
    	}
    	if(tid == -1) {
    		throw new Exception("Failed to find task in project with specified identifier");
    	}
    	return tid;
    }

    public Project createProject(String pid) throws Exception {
    	
    	Project createdProject = null;
    	
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
            
            ArrayList<String> tasks = new ArrayList<String>();
            ArrayList<String> teammates = new ArrayList<String>();
            ArrayList<String> identifiers = new ArrayList<String>();
            boolean isArchived = false;
            
            createdProject = new Project(pid, tasks, teammates, identifiers, isArchived);

        } catch (Exception e) {
            throw new Exception("Failed to create project: " + e.getMessage());
        }
        
        return createdProject;
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
            			if(identifiers.size() == 0) {
            				tasks.add(attribute);
            				identifiers.add(resultSet1.getNString("tsk_identifier"));
            			}
            			else {
            			for(int i = 0; i < identifiers.size(); i++) {
            				if(resultSet1.getNString("tsk_identifier").compareTo(identifiers.get(i)) < 0) {
            					tasks.add(i,attribute);
            					identifiers.add(i,resultSet1.getNString("tsk_identifier"));
            					break;
            				}
            				else if(i == identifiers.size() - 1) {
            					tasks.add(i + 1,attribute);
            					identifiers.add(i + 1,resultSet1.getNString("tsk_identifier"));
            					break;
            				}            				
            			}
            			}
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
    
    public Project archiveProject(String pid) throws Exception {
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
            return getProject(pid);
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
        		throw new Exception("Failed to add task: could not find project");
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
        			ps1.setString(1, parentTaskIdentifier);
        			ResultSet resultSet1 = ps1.executeQuery();
        			ArrayList<Integer> taskIDs = new ArrayList<>();
        			while(resultSet1.next()) {
        				logger.log("Looking at task: " + resultSet1.getNString("tsk_name"));
        				taskIDs.add(resultSet1.getInt("tsk_id"));
        			}
        			boolean taskFound = false;
        			int foundTaskID = -1;
        			String foundIdentifier = "";
        			for(int j = 0; j < taskIDs.size(); j++) {
        				logger.log("" + taskIDs.get(j));
        				ps1 = conn.prepareStatement("SELECT * FROM lookup_table WHERE tsk_id = ?;");
        				ps1.setInt(1, taskIDs.get(j));
        				resultSet1 = ps1.executeQuery();
        				
        				if(!resultSet1.next()) {
            			}
        				else {
        					logger.log("checking project against current project: " + resultSet1.getString("pid"));
        					if(resultSet1.getString("pid").equals(pid)) {
        						taskFound = true;
        						foundTaskID = taskIDs.get(j);
        					}
        				}
        			}
        			logger.log("outside j loop");
        			if(!taskFound) {
        				throw new Exception("Failed to add task: Parent task ID not associated with this project");
        			}
        			ps1 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
        			ps1.setInt(1, foundTaskID);
        			resultSet1 = ps1.executeQuery();
        			resultSet1.next();
        			foundIdentifier = resultSet1.getString("tsk_identifier");
        			
        			ps.setInt(2, foundTaskID);
        			ps1 = conn.prepareStatement("SELECT * FROM tasks WHERE parent_tsk_id = ?;",
                            ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
        			ps1.setInt(1, foundTaskID);
        			resultSet1 = ps1.executeQuery();
        			
        			if(!resultSet1.next()) {
        				logger.log("in base case");
        				String newIdent = foundIdentifier + "." + 1;
        				ps.setNString(3, newIdent);
        			}
        			else {
        				resultSet1.last();
        				String lastIdent = resultSet1.getString("tsk_identifier");
        				String lastNum = lastIdent.substring(lastIdent.length() - 1);
        				int num = Integer.parseInt(lastNum);
        				logger.log("new task identifier last number: " + num + "plus 1");
        				int newNum = num + 1;
        				String newIdent = foundIdentifier + "." + newNum;
        				ps.setNString(3, newIdent);
        			}
        			if(foundTaskID != -1) {
        				ps1 = conn.prepareStatement("UPDATE tasks SET isBottomLevel = false WHERE tsk_id = ?;");
        				ps1.setInt(1, foundTaskID);
        				ps1.execute();
        			}
        		}
        		else {
        			logger.log("creating task without parent");
        			ps.setNString(2, null);
        			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ? && tmt_id IS NULL;",
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
        				int max = -1;
        				PreparedStatement ps3 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
    					ps3.setInt(1, resultSet2.getInt("tsk_id"));
    					logger.log("passed initial resultset2 call");
    					ResultSet resultSet3 = ps3.executeQuery();
    					if(resultSet3.next()) {
        				if(resultSet3.getString("tsk_identifier").length() == 1) {
        					max = Integer.parseInt(resultSet3.getString("tsk_identifier"));
        				}
        				while(resultSet2.next()) {
        					ps3 = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
        					ps3.setInt(1, resultSet2.getInt("tsk_id"));
        					resultSet3 = ps3.executeQuery();
        					if(resultSet3.next()) {
        						if(resultSet3.getString("tsk_identifier").length() == 1) {
        							int newNum = Integer.parseInt(resultSet3.getString("tsk_identifier"));
        							if(newNum > max) {
        								max = newNum;
        							}
                				}
        					}
        				}
    					}
        				ps.setString(3, "" + (max + 1));
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
    
    public Project renameTask(String newName, String identifier, String pid) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ? && tmt_id IS NULL;");
            ps.setString(1, pid);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
            	ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
            	ps.setInt(1, resultSet.getInt("tsk_id"));
            	ResultSet resultSet1 = ps.executeQuery();
            	if(!resultSet1.next()) {
            		throw new Exception("Failed to rename task: " + "failed to find task");
            	}
            	if(resultSet1.getString("tsk_identifier").equals(identifier)) {
            		logger.log("found task to update");
            		ps = conn.prepareStatement("UPDATE tasks SET tsk_name = ? WHERE tsk_id = ?;");
            		ps.setNString(1, newName);
            		ps.setInt(2, resultSet.getInt("tsk_id"));
            		ps.execute();
            	}
            }
            logger.log("task updated");
            return getProject(pid);
            
            
        } catch (Exception e) {
        	logger.log("exception thrown");
            throw new Exception("Failed to rename task: " + e.getMessage());
        }
    }
    
    public Project allocateTeammate(String pid, String tid, String identifier) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid = ? && tmt_id = ?;");
    		ps.setNString(1, pid);
    		ps.setNString(2, tid);
    		ResultSet resultSet = ps.executeQuery();
    		if(!resultSet.next()) {
    			throw new Exception("Failed to allocate teammate: teammate not added to specified project");
    		}
    		
    		ps = conn.prepareStatement("SELECT * FROM projects WHERE pid = ?;");
    		ps.setNString(1, pid);
    		resultSet = ps.executeQuery();
    		if(!resultSet.next()) {
    			throw new Exception("Failed to allocate teammate: project does not exist");
    		}
    		if(resultSet.getBoolean("isArchived")) {
    			throw new Exception("Failed to allocate teammate: project is archived");
    		}
    		logger.log("finding taskID");
    		int tsk_id = findTaskID(pid, identifier);
    		logger.log("found taskID");
    		ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
    		ps.setInt(1, tsk_id);
    		resultSet = ps.executeQuery();
    		if(!resultSet.next()) {
    			throw new Exception("Failed to allocate teammate: task does not exist");
    		}
    		if(!resultSet.getBoolean("isBottomLevel") || resultSet.getBoolean("isComplete")) {
    			throw new Exception("Failed to allocate teammate: task is not applicable to have a teammate added");
    		}
    		
    		ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE tsk_id = ? && tmt_id = ?;");
    		ps.setInt(1, tsk_id);
    		ps.setNString(2, tid);
    		resultSet = ps.executeQuery();
    		if(resultSet.next()) {
    			throw new Exception("Failed to allocate teammate: teammate already allocated to this task");
    		}
    		ps = conn.prepareStatement("INSERT INTO lookup_table (tsk_id,tmt_id) values (?,?);");
    		ps.setInt(1, tsk_id);
    		ps.setString(2, tid);
    		ps.execute();
    		return getProject(pid);
        } catch (Exception e) {
        	logger.log("exception thrown");
            throw new Exception("Failed to allocate teammate: " + e.getMessage());
        }
    }
    
    public Project deallocateTeammate(String pid, String tmt_id, String identifier) throws Exception {
    	int tsk_id = findTaskID(pid, identifier);
    	
    	PreparedStatement ps = conn.prepareStatement("DELETE FROM lookup_table WHERE tsk_id = ? && tmt_id = ?;");
    	ps.setInt(1, tsk_id);
    	ps.setNString(2, tmt_id);
    	ps.execute();
    	return getProject(pid);
	}
    
    public Task getTask(String pid, String identifier) throws Exception {
    	try {
    		int task_id = findTaskID(pid, identifier);
    		
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tasks WHERE tsk_id = ?;");
            ps.setInt(1, task_id);
            ResultSet resultSet = ps.executeQuery();
            
            if(!resultSet.next()) {
                resultSet.close();
                logger.log("No task with project " + pid + " and identifier " + identifier);
                return null;
            } else {
            	int tsk_id = task_id;
            	boolean isComplete = resultSet.getBoolean("isComplete");
            	boolean isBottomLevel = resultSet.getBoolean("isBottomLevel");
            	ArrayList<String> subTasks = new ArrayList<>();
            	ArrayList<String> teammates = new ArrayList<>();
            	String name = resultSet.getString("tsk_name");
            	String tsk_identifier = resultSet.getNString("tsk_identifier");

        		// start building subTasks list
            	ps = conn.prepareStatement("SELECT * FROM tasks WHERE parent_tsk_id = ?;");
            	ps.setInt(1, tsk_id);
            	resultSet = ps.executeQuery();
            	
            	while(resultSet.next()) {
            		logger.log("found value in tasks table");
            		subTasks.add(resultSet.getNString("tsk_name"));
            	}
            	
            	// start building teammates list
            	ps = conn.prepareStatement("SELECT * FROM lookup_table WHERE pid IS NULL && tsk_id = ?;");
            	ps.setInt(1, tsk_id);
            	resultSet = ps.executeQuery();
            	
            	while (resultSet.next()) {
            		logger.log("found value in lookup_table");
            		teammates.add(resultSet.getNString("tmt_id"));
            	}
            	
            	Task task = new Task(tsk_id, isComplete, isBottomLevel, subTasks, name, tsk_identifier, teammates);
            	return task;
            }
        } catch (Exception e) {
            throw new Exception("Unable to retrieve task: " + e.getMessage());
        }
    }
    
}
