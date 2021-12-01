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
import speromeliora.http.AddTeammateRequest;
import speromeliora.http.AddTeammateResponse;
import speromeliora.http.CreateProjectRequest;
import speromeliora.http.CreateProjectResponse;
import speromeliora.model.Project;

public class AddTeammateHandler implements RequestHandler<AddTeammateRequest, AddTeammateResponse> {

    private AmazonS3 s3 = null;
    LambdaLogger logger;
    public AddTeammateHandler() {}

    // Test purpose only.
    AddTeammateHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public AddTeammateResponse handleRequest(AddTeammateRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String pid = "";
		String teammateName = "";
		Project project = new Project();
		try {
			pid = req.getArg1();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse pid";
			fail = true;
		}
		try {
			teammateName = req.getArg2();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse teammate name";
			fail = true;
		}
		if (pid != "" && teammateName != "") {
			try {
				project = addTeammateInRDS(pid, teammateName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
			}
		}
		else {
			failMessage = "One or more text fields left empty";
			fail = true;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		AddTeammateResponse response;
		if (fail) {
			response = new AddTeammateResponse(400, failMessage);
		} else {
			response = new AddTeammateResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project addTeammateInRDS(String pid, String teammateName) throws Exception {
		if (logger != null) { logger.log("in addTeammate"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.addTeammate(pid, teammateName);
		if (logger != null) { logger.log("teammate added"); }
		return project;
	}
    

}