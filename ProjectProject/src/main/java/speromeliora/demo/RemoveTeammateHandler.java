package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.ArchiveProjectRequest;
import speromeliora.http.ArchiveProjectResponse;
import speromeliora.http.DeleteProjectRequest;
import speromeliora.http.DeleteProjectResponse;
import speromeliora.http.RemoveTeammateRequest;
import speromeliora.http.RemoveTeammateResponse;

public class RemoveTeammateHandler implements RequestHandler<RemoveTeammateRequest, RemoveTeammateResponse> {
	private AmazonS3 s3 = null;
    LambdaLogger logger;
    public RemoveTeammateHandler() {}

    // Test purpose only.
    RemoveTeammateHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public RemoveTeammateResponse handleRequest(RemoveTeammateRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String pid = "";
		String teammateName = "";
		try {
			pid = req.getArg1();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse pid";
			fail = true;
		}
		try {
			teammateName = req.getArg2();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse teammate Name";
			fail = true;
		}
		if (pid != "" && teammateName != "") {
			try {
				removeTeammateInRDS(pid, teammateName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
			}
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		RemoveTeammateResponse response;
		if (fail) {
			response = new RemoveTeammateResponse(400, failMessage);
		} else {
			response = new RemoveTeammateResponse(pid, 200);  // success
		}

		return response; 
	}
    
    public void removeTeammateInRDS(String pid, String teammateName) throws Exception {
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		dao.removeTeammate(pid, teammateName);
		if (logger != null) { logger.log("removed Teammate"); }
	}
}

