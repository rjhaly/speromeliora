package speromeliora.demo;

import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.AddTaskRequest;
import speromeliora.http.AddTaskResponse;
import speromeliora.http.CreateProjectResponse;
import speromeliora.model.Project;
import speromeliora.model.Task;

public class AddTasksHandler implements RequestHandler<AddTaskRequest, AddTaskResponse>{
	private AmazonS3 s3 = null;
    LambdaLogger logger;
    public AddTasksHandler() {}

    // Test purpose only.
    AddTasksHandler(AmazonS3 s3) {
        this.s3 = s3;
    }
	@Override
	public AddTaskResponse handleRequest(AddTaskRequest input, Context context) {
		logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(input.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String taskString = "";
		Project updatedProject = new Project();
		String parentTask = "";
		String project = "";
		try {
			taskString = input.getArg1();
			parentTask =input.getArg2();
			project = input.getArg3();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse add task info";
			fail = true;
		}
		if (taskString != "" && project != "") {
			try {
				String[] tasks = taskString.split(",");
				updatedProject = addTasksInRDS(tasks, parentTask, project);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
				failMessage = "task name is empty";
			}
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		AddTaskResponse response;
		if (fail) {
			response = new AddTaskResponse(400, failMessage);
		} else {
			response = new AddTaskResponse(updatedProject, 200);  // success
		}

		return response; 
	}
    
    public Project addTasksInRDS(String[] tasks, String parentTask, String project) throws Exception {
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project updatedProject = dao.addTasks(tasks, parentTask, project);
		if (logger != null) { logger.log("created Project"); }
		return updatedProject;
	}
	}


