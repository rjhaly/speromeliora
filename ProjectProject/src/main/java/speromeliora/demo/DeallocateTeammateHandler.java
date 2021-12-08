package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.DeallocateTeammateRequest;
import speromeliora.http.DeallocateTeammateResponse;
import speromeliora.model.Project;

public class DeallocateTeammateHandler implements RequestHandler<DeallocateTeammateRequest, DeallocateTeammateResponse> {

    private AmazonS3 s3 = null;
    LambdaLogger logger;
    public DeallocateTeammateHandler() {}

    // Test purpose only.
    DeallocateTeammateHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public DeallocateTeammateResponse handleRequest(DeallocateTeammateRequest req, Context context) {
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
		
		if (!pid.equals("") && !teammateName.equals("") && !identifier.equals("")) {
			try {
				project = deallocateTeammateInRDS(pid, teammateName, identifier);
			} catch (Exception e) {
				e.printStackTrace();
				fail = true;
			}
		} else {
			failMessage = "One or more text fields left empty";
			fail = true;
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		DeallocateTeammateResponse response;
		if (fail) {
			response = new DeallocateTeammateResponse(400, failMessage);
		} else {
			response = new DeallocateTeammateResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project deallocateTeammateInRDS(String pid, String teammateName, String identifier) throws Exception {
		if (logger != null) { logger.log("in deallocateTeammate"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.deallocateTeammate(pid, teammateName, identifier);
		if (logger != null) { logger.log("teammate deallocated"); }
		return project;
	}
    

}
