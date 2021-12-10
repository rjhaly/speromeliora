package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import speromeliora.db.ProjectDAO;
import speromeliora.http.CreateProjectRequest;
import speromeliora.http.CreateProjectResponse;
import speromeliora.model.Project;

public class CreateProjectHandler implements RequestHandler<CreateProjectRequest, CreateProjectResponse> {

    private AmazonS3 s3 = null;
    LambdaLogger logger;
    public CreateProjectHandler() {}

    // Test purpose only.
    CreateProjectHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public CreateProjectResponse handleRequest(CreateProjectRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String pid = req.getArg1();
		Project project = null;
		
		if (pid.length() > 0) {
			try {
				project = createProjectInRDS(pid);
			} catch (Exception e) {
				e.printStackTrace();
				fail = true;
			}
		} else {
			fail = true;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		CreateProjectResponse response;
		if (fail) {
			response = new CreateProjectResponse(400, failMessage);
		} else {
			response = new CreateProjectResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project createProjectInRDS(String pid) throws Exception {
    	Project project;
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		project = dao.createProject(pid);
		if (logger != null) { logger.log("created Project"); }
		return project;
	}
    

}