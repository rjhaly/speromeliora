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
import speromeliora.http.RenameTaskRequest;
import speromeliora.http.RenameTaskResponse;
import speromeliora.model.Project;

public class RenameTaskHandler implements RequestHandler<RenameTaskRequest, RenameTaskResponse>{
	private AmazonS3 s3 = null;
    LambdaLogger logger;
    public RenameTaskHandler() {}

    // Test purpose only.
    RenameTaskHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public RenameTaskResponse handleRequest(RenameTaskRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of RequestHandler");
		logger.log(req.toString());

        // Get the object from the event and show its content type
		boolean fail = false;
		String failMessage = "";
		Project updatedProject = new Project();
		String newName = "";
		String identifier = "";
		String pid = "";
		try {
			newName = req.getArg1();
			identifier = req.getArg2();
			pid = req.getArg3();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse parameters";
			fail = true;
		}
		if (newName != "" && identifier != "" && pid != "") {
			try {
				updatedProject = renameTaskInRDS(newName, identifier, pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail = true;
			}
		}

		// compute proper response and return. Note that the status code is internal to the HTTP response
		// and has to be processed specifically by the client code.
		RenameTaskResponse response;
		if (fail) {
			response = new RenameTaskResponse(400, failMessage);
			//eat ass smoke grass
		} else {
			response = new RenameTaskResponse(updatedProject, 200);  // success
		}

		return response; 
	}
    
    public Project renameTaskInRDS(String newName, String identifier, String pid) throws Exception {
		if (logger != null) { logger.log("in renameTask"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.renameTask(newName, identifier, pid);
		if (logger != null) { logger.log("renamedTask"); }
		return project;
	}
}