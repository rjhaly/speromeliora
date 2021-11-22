package speromeliora.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.CreateProjectRequest;
import speromeliora.http.CreateProjectResponse;
import speromeliora.http.GetProjectResponse;
import speromeliora.model.Project;

public class GetProjectHandler implements RequestHandler<String, GetProjectResponse>{
	 private AmazonS3 s3 = null;
	    LambdaLogger logger;
	    public GetProjectHandler() {}

	    // Test purpose only.
	    GetProjectHandler(AmazonS3 s3) {
	        this.s3 = s3;
	    }
	    @Override
	    public GetProjectResponse handleRequest(String req, Context context) {
	        logger = context.getLogger();
	        logger.log("Loading Java Lambda handler of RequestHandler");
			logger.log(req.toString());

			String failMessage = "";
			String pid = req;
			Project project;
			try {
				project = getProjectInRDS(pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				project = null;
			}

			// compute proper response and return. Note that the status code is internal to the HTTP response
			// and has to be processed specifically by the client code.
			GetProjectResponse response;
			if (project == null) {
				failMessage = "Project not found";
				response = new GetProjectResponse(400, failMessage);
			} else {
				response = new GetProjectResponse(project, 200);  // success
			}

			return response; 
		}
	    
	    public Project getProjectInRDS(String pid) throws Exception {
			if (logger != null) { logger.log("in getProject"); }
			ProjectDAO dao = new ProjectDAO(logger);
			if (logger != null) { logger.log("retrieved DAO"); }
			Project project = dao.getProject(pid);
			if (logger != null) { logger.log("retrieved project");}
			return project;
		}
}
