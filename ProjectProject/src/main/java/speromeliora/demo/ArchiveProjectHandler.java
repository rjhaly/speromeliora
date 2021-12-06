package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.ArchiveProjectRequest;
import speromeliora.http.ArchiveProjectResponse;
import speromeliora.model.Project;

public class ArchiveProjectHandler implements RequestHandler<ArchiveProjectRequest, ArchiveProjectResponse>{
	private AmazonS3 s3 = null;
    LambdaLogger logger;
    public ArchiveProjectHandler() {}

    // Test purpose only.
    ArchiveProjectHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public ArchiveProjectResponse handleRequest(ArchiveProjectRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		String pid = "";
		Project project = new Project();
		try {
			pid = req.getArg1();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse pid";
			fail = true;
		}
		if (pid != "") {
			try {
				project = archiveProjectInRDS(pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
			}
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		ArchiveProjectResponse response;
		if (fail) {
			response = new ArchiveProjectResponse(400, failMessage);
		} else {
			response = new ArchiveProjectResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project archiveProjectInRDS(String pid) throws Exception {
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.archiveProject(pid);
		if (logger != null) { logger.log("archived Project"); }
		return project;
	}
}
