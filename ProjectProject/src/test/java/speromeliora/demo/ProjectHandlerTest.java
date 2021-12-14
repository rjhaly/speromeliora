package speromeliora.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;

import speromeliora.http.AddTaskRequest;
import speromeliora.http.AddTaskResponse;
import speromeliora.http.ArchiveProjectRequest;
import speromeliora.http.ArchiveProjectResponse;
import speromeliora.http.CreateProjectRequest;
import speromeliora.http.CreateProjectResponse;
import speromeliora.http.DeleteProjectRequest;
import speromeliora.http.DeleteProjectResponse;
import speromeliora.http.GetProjectResponse;
import speromeliora.http.ListProjectResponse;
import speromeliora.http.MarkTaskRequest;
import speromeliora.http.MarkTaskResponse;
import speromeliora.http.RenameTaskRequest;
import speromeliora.http.RenameTaskResponse;
import speromeliora.model.Project;
import speromeliora.model.Task;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectHandlerTest extends LambdaTest{

    private final String CONTENT_TYPE = "image/jpeg";
    private S3Event event;
    private clearDB nuke = new clearDB();

    @Mock
    private AmazonS3 s3Client;
    @Mock
    private S3Object s3Object;

    @Captor
    private ArgumentCaptor<GetObjectRequest> getObjectRequest;

    
    void testInput(String incoming, String outgoing) throws IOException {
    	CreateProjectHandler handler = new CreateProjectHandler();
    	CreateProjectRequest req = new Gson().fromJson(incoming, CreateProjectRequest.class);
        CreateProjectResponse response = handler.handleRequest(req, createContext("compute"));

        Assert.assertEquals(outgoing, response.project.getPid());
        Assert.assertEquals(200, response.statusCode);
    }
    
    void testGetInput(String incoming, Project outgoing) throws IOException {
    	GetProjectHandler handler = new GetProjectHandler();
    	String req = incoming;
        GetProjectResponse response = handler.handleRequest(req, createContext("compute"));

        Assert.assertEquals(outgoing.getPid(), response.project.getPid());
        Assert.assertEquals(outgoing.getTasks(), response.project.getTasks());
        Assert.assertEquals(outgoing.getTeammates(), response.project.getTeammates());
        Assert.assertEquals(outgoing.getIsArchived(), response.project.getIsArchived());
        Assert.assertEquals(200, response.statusCode);
    }
    
    void testArchiveInput(String incoming, String outgoing) throws IOException {
    	ArchiveProjectHandler handler = new ArchiveProjectHandler();
    	ArchiveProjectRequest req = new Gson().fromJson(incoming, ArchiveProjectRequest.class);
        ArchiveProjectResponse response = handler.handleRequest(req, createContext("compute"));

        Assert.assertEquals(outgoing, response.project.getPid());
        Assert.assertEquals(200, response.statusCode);
    }

    @Test
    public void testprojectHandler() throws SQLException {
    	nuke.nukeDB();
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"project1\"}";
        String RESULT = "project1";
        
        try {
        	testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    @Test
    public void testgetprojectHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler handler = new CreateProjectHandler();
    	CreateProjectRequest req = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	handler.handleRequest(req, createContext("compute"));
    	String SAMPLE_INPUT_STRING = "project1";
        Project RESULT = new Project("project1", new ArrayList<>(), new ArrayList<>(), false);
        
        try {
        	testGetInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    @Test
    public void testArchiveProjectHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler handler = new CreateProjectHandler();
    	CreateProjectRequest req = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	handler.handleRequest(req, createContext("compute"));
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"project1\"}";
        String RESULT = "project1";
        
        try {
        	testArchiveInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    @Test
    public void testDeleteProjectHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler chandler = new CreateProjectHandler();
    	CreateProjectRequest creq = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	chandler.handleRequest(creq, createContext("compute"));
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"project1\"}";
        int RESULT = 200;
        
        DeleteProjectHandler handler = new DeleteProjectHandler();
		DeleteProjectRequest req = new Gson().fromJson(SAMPLE_INPUT_STRING, DeleteProjectRequest.class);
		DeleteProjectResponse response = handler.handleRequest(req, createContext("compute"));

		Assert.assertEquals(RESULT, response.statusCode);
		Assert.assertEquals(200, response.statusCode);
    }
    
    @Test
    public void testListProjectHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler chandler = new CreateProjectHandler();
    	CreateProjectRequest creq = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	chandler.handleRequest(creq, createContext("compute"));
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"project1\"}";
        Project project = new Project("project1", new ArrayList<Task>(), new ArrayList<String>(), false);
        ArrayList<Project> testProjies = new ArrayList<>();
        testProjies.add(project);
        ListProjectHandler handler = new ListProjectHandler();
        
		ListProjectResponse response = handler.handleRequest(SAMPLE_INPUT_STRING, createContext("compute"));
		for(int i = 0; i < testProjies.size(); i++) {
		 Assert.assertEquals(testProjies.get(i).getPid(), response.projects.get(i).getPid());
		 Assert.assertEquals(testProjies.get(i).getTeammates(), response.projects.get(i).getTeammates());
		 Assert.assertEquals(testProjies.get(i).getIsArchived(), response.projects.get(i).getIsArchived());
		 Assert.assertEquals(testProjies.get(i).getTasks(), response.projects.get(i).getTasks());
	        Assert.assertEquals(200, response.statusCode);
		}
    }
    @Test
    public void testAddTaskHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler chandler = new CreateProjectHandler();
    	CreateProjectRequest creq = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	chandler.handleRequest(creq, createContext("compute"));
    	AddTaskRequest treq = new Gson().fromJson("{\"arg1\": \"task1\", \"arg2\": \"\", \"arg3\": \"project1\"}", AddTaskRequest.class);
    	AddTasksHandler thandler = new AddTasksHandler();
    	ArrayList<Task> tasks = new ArrayList<>();
    	Task task = new Task(1,false, true, new ArrayList<>(), "task1", "1", new ArrayList<>());
    	tasks.add(task);
        Project project = new Project("project1", tasks, new ArrayList<String>(), false);
        AddTaskResponse response = thandler.handleRequest(treq, createContext("compute"));
		Assert.assertEquals(project.getPid(), response.project.getPid());
		Assert.assertEquals(project.getTasks().get(0).getName(), response.project.getTasks().get(0).getName());
		Assert.assertEquals(project.getTasks().get(0).getTaskIdentifier(), response.project.getTasks().get(0).getTaskIdentifier());
		Assert.assertEquals(200, response.statusCode);
    }
    @Test
    public void testRenameTaskHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler chandler = new CreateProjectHandler();
    	CreateProjectRequest creq = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	chandler.handleRequest(creq, createContext("compute"));
    	AddTaskRequest treq = new Gson().fromJson("{\"arg1\": \"task1\", \"arg2\": \"\", \"arg3\": \"project1\"}", AddTaskRequest.class);
    	AddTasksHandler thandler = new AddTasksHandler();
    	thandler.handleRequest(treq, createContext("compute"));
    	RenameTaskHandler rhandler = new RenameTaskHandler();
    	RenameTaskRequest rreq = new Gson().fromJson("{\"arg1\": \"task1.0\", \"arg2\": \"1\", \"arg3\": \"project1\"}", RenameTaskRequest.class);
    	RenameTaskResponse response = rhandler.handleRequest(rreq, createContext("compute"));
    	ArrayList<Task> tasks = new ArrayList<>();
    	Task task = new Task(1,false, true, new ArrayList<>(), "task1.0", "1", new ArrayList<>());
    	tasks.add(task);
        Project project = new Project("project1", tasks, new ArrayList<String>(), false);
        Assert.assertEquals(project.getPid(), response.project.getPid());
		Assert.assertEquals(project.getTasks().get(0).getName(), response.project.getTasks().get(0).getName());
		Assert.assertEquals(project.getTasks().get(0).getTaskIdentifier(), response.project.getTasks().get(0).getTaskIdentifier());
		Assert.assertEquals(200, response.statusCode);
    }
    
    @Test
    public void testMarkTaskHandler() throws SQLException {
    	nuke.nukeDB();
    	CreateProjectHandler chandler = new CreateProjectHandler();
    	CreateProjectRequest creq = new Gson().fromJson("{\"arg1\": \"project1\"}", CreateProjectRequest.class);
    	chandler.handleRequest(creq, createContext("compute"));
    	AddTaskRequest treq = new Gson().fromJson("{\"arg1\": \"task1\", \"arg2\": \"\", \"arg3\": \"project1\"}", AddTaskRequest.class);
    	AddTasksHandler thandler = new AddTasksHandler();
    	thandler.handleRequest(treq, createContext("compute"));
    	MarkTaskHandler mhandler = new MarkTaskHandler();
    	MarkTaskRequest mreq = new Gson().fromJson("{\"arg1\": \"project1\", \"arg2\": \"1\"}", MarkTaskRequest.class);
    	MarkTaskResponse response = mhandler.handleRequest(mreq, createContext("compute"));
    	ArrayList<Task> tasks = new ArrayList<>();
    	Task task = new Task(1,true, true, new ArrayList<>(), "task1", "1", new ArrayList<>());
    	tasks.add(task);
        Project project = new Project("project1", tasks, new ArrayList<String>(), false);
        Assert.assertEquals(project.getPid(), response.project.getPid());
		Assert.assertEquals(project.getTasks().get(0).getName(), response.project.getTasks().get(0).getName());
		Assert.assertEquals(project.getTasks().get(0).getTaskIdentifier(), response.project.getTasks().get(0).getTaskIdentifier());
		Assert.assertEquals(project.getTasks().get(0).getIsCompleted(), response.project.getTasks().get(0).getIsCompleted());
		Assert.assertEquals(200, response.statusCode);
    }
}
