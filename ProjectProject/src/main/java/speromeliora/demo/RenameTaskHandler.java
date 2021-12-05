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
		Project project = new Project();
		int tid = -1;
		String name = "";
		try {
			tid = req.getArg1();
			name = req.getArg2();
		} catch (NumberFormatException e) {
			failMessage = "Unable to parse parameters";
			fail = true;
		}
		if (tid != -1 && name != "") {
			try {
				renameTaskInRDS(tid, name);
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
		} else {
			response = new RenameTaskResponse(project, 200);  // success
		}

		return response; 
	}
    
    public Project renameTaskInRDS(int tid, String name) throws Exception {
		if (logger != null) { logger.log("in renameTask"); }
		ProjectDAO dao = new ProjectDAO(logger);
		if (logger != null) { logger.log("retrieved DAO"); }
		Project project = dao.renameTask(tid, name);
		if (logger != null) { logger.log("renamedTask"); }
		return project;
	}
}