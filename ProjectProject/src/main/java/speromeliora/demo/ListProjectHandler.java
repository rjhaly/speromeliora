package speromeliora.demo;

import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;

import speromeliora.db.ProjectDAO;
import speromeliora.http.GetProjectResponse;
import speromeliora.http.ListProjectResponse;
import speromeliora.model.Project;

public class ListProjectHandler implements RequestHandler<Object, ListProjectResponse>{
	 private AmazonS3 s3 = null;
	    LambdaLogger logger;
	    public ListProjectHandler() {}

	    // Test purpose only.
	    ListProjectHandler(AmazonS3 s3) {
	        this.s3 = s3;
	    }
	    @Override
	    public ListProjectResponse handleRequest(Object input, Context context) {
	        logger = context.getLogger();
	        logger.log("Loading Java Lambda handler of RequestHandler");

			String failMessage = "";
			ArrayList<Project> projects;
			try {
				projects = listProjectsFromRDS();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				projects = null;
			}

			// compute proper response and return. Note that the status code is internal to the HTTP response
			// and has to be processed specifically by the client code.
			ListProjectResponse response;
			if (projects == null) {
				failMessage = "an error occured";
				response = new ListProjectResponse(400, failMessage);
			} else {
				response = new ListProjectResponse(projects, 200);  // success
			}

			return response; 
		}
	    
	    public ArrayList<Project> listProjectsFromRDS() throws Exception {
			if (logger != null) { logger.log("in getProject"); }
			ProjectDAO dao = new ProjectDAO(logger);
			if (logger != null) { logger.log("retrieved DAO"); }
			ArrayList<Project> projects = dao.listProjects();
			if (logger != null) { logger.log("retrieved project");}
			return projects;
		}
}
