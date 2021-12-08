package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.GetProjectResponse;
import speromeliora.http.GetTaskRequest;
import speromeliora.http.GetTaskResponse;
import speromeliora.model.Project;
import speromeliora.model.Task;

public class GetTaskHandler implements RequestHandler<GetTaskRequest, GetTaskResponse>{
	
	 private AmazonS3 s3 = null;
	 
    LambdaLogger logger;
    public GetTaskHandler() {}

    // Test purpose only.
    GetTaskHandler(AmazonS3 s3) {
        this.s3 = s3;
    }
    @Override
    public GetTaskResponse handleRequest(GetTaskRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

		String failMessage = "";
		String pid = req.getArg1();
		String identifier = req.getArg2();
		Task task;
		try {
			task = getTaskInRDS(pid, identifier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			task = null;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		GetTaskResponse response;
		if (task == null) {
			failMessage = "Task not found";
			response = new GetTaskResponse(400, failMessage);
		} else {
			response = new GetTaskResponse(task, 200);  // success
		}

		return response; 
	}
    
    public Task getTaskInRDS(String pid, String identifier) throws Exception {
		if (logger != null) { logger.log("in getTask"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Task task = dao.getTask(pid, identifier);
		if (logger != null) { logger.log("retrieved project");}
		return task;
	}
    
}

