package speromeliora.demo;

import static org.mockito.Mockito.when;

import java.io.IOException;
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

import speromeliora.http.CreateProjectRequest;
import speromeliora.http.CreateProjectResponse;
import speromeliora.http.GetProjectResponse;
import speromeliora.model.Project;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectHandlerTest extends LambdaTest{

    private final String CONTENT_TYPE = "image/jpeg";
    private S3Event event;

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

        Assert.assertEquals(outgoing, response.projectID);
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

    @Test
    public void testprojectHandler() {
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"project1\"}";
        String RESULT = "project1";
        
        try {
        	testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    @Test
    public void testgetprojectHandler() {
    	String SAMPLE_INPUT_STRING = "project1";
        Project RESULT = new Project("project1", new ArrayList<>(), new ArrayList<>(), false);
        
        try {
        	testGetInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
}
