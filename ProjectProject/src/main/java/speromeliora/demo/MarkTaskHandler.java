package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.ArchiveProjectRequest;
import speromeliora.http.ArchiveProjectResponse;
import speromeliora.http.MarkTaskRequest;
import speromeliora.http.MarkTaskResponse;
import speromeliora.model.Project;

public class MarkTaskHandler implements RequestHandler<MarkTaskRequest, MarkTaskResponse>{
	private AmazonS3 s3 = null;
    LambdaLogger logger;
    public MarkTaskHandler() {}

    // Test purpose only.
    MarkTaskHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public MarkTaskResponse handleRequest(MarkTaskRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		Project project = new Project();
		String pid = "";
		String identifier = "";
		try {
			pid = req.getArg1();
			identifier = req.getArg2();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse parameters";
			fail = true;
		}
		if (pid != "" && identifier != "") {
			try {
				project = markTaskInRDS(pid, identifier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
			}
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		MarkTaskResponse response;
		if (fail) {
			response = new MarkTaskResponse(400, failMessage);
		} else {
			response = new MarkTaskResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project markTaskInRDS(String pid, String identifier) throws Exception {
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.markTask(pid, identifier);
		if (logger != null) { logger.log("markedTask"); }
		return project;
	}
}