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
import speromeliora.http.AllocateTeammateRequest;
import speromeliora.http.AllocateTeammateResponse;
import speromeliora.http.CreateProjectRequest;
import speromeliora.http.CreateProjectResponse;
import speromeliora.model.Project;

public class AllocateTeammateHandler implements RequestHandler<AllocateTeammateRequest, AllocateTeammateResponse> {

    private AmazonS3 s3 = null;
    LambdaLogger logger;
    public AllocateTeammateHandler() {}

    // Test purpose only.
    AllocateTeammateHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public AllocateTeammateResponse handleRequest(AllocateTeammateRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String pid = "";
		String teammateName = "";
		String identifier = "";
		Project project = new Project();
		try {
			pid = req.getArg1();
			teammateName = req.getArg2();
			identifier = req.getArg3();
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
		try {
			identifier = req.getArg3();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse task Identifier";
			fail = true;
		}
		if (pid != "" && teammateName != "" && identifier != "") {
			try {
				project = allocateTeammateInRDS(pid, teammateName, identifier);
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
		AllocateTeammateResponse response;
		if (fail) {
			response = new AllocateTeammateResponse(400, failMessage);
		} else {
			response = new AllocateTeammateResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project allocateTeammateInRDS(String pid, String teammateName, String identifier) throws Exception {
		if (logger != null) { logger.log("in allocateTeammate"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.allocateTeammate(pid, teammateName, identifier		);
		if (logger != null) { logger.log("teammate allocated"); }
		return project;
	}
    

}
