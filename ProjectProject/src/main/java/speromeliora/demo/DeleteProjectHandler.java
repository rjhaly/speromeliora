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

public class DeleteProjectHandler implements RequestHandler<DeleteProjectRequest, DeleteProjectResponse> {
	private AmazonS3 s3 = null;
    LambdaLogger logger;
    public DeleteProjectHandler() {}

    // Test purpose only.
    DeleteProjectHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public DeleteProjectResponse handleRequest(DeleteProjectRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String pid = "";
		try {
			pid = req.getArg1();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse pid";
			fail = true;
		}
		if (pid != "") {
			try {
				deleteProjectInRDS(pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
			}
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		DeleteProjectResponse response;
		if (fail) {
			response = new DeleteProjectResponse(400, failMessage);
		} else {
			response = new DeleteProjectResponse(200);  // success
		}

		return response; 
	}
    
    public void deleteProjectInRDS(String pid) throws Exception {
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		dao.deleteProject(pid);
		if (logger != null) { logger.log("deleted Project"); }
	}
}
