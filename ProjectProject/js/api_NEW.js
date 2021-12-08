// all access driven through BASE. Must end with a SLASH
// be sure you change to accommodate your specific API Gateway entry point
var base_url = "https://umrdxh6f76.execute-api.us-east-2.amazonaws.com/Alphiwalphi/";

var listProject_url    = base_url + "projectList";    // GET
var createProject_url  = base_url + "projectCreation";    // POST
var getProject_url     = base_url + "project";
var addTeammate_url    = base_url + "teammateAddition";
var removeTeammate_url = base_url + "teammateRemoval";
var deleteProject_url  = base_url + "projectDeletion";
var addTasks_url       = base_url + "taskAddition";
var renameTask_url	   = base_url + "taskRename";
var archive_url		   = base_url + "projectArchival";
var assignTeammate_url = base_url + "teammateAllocation";
var deassignTeammate_url = base_url + "teammateDeallocation";

//so the change show up//

// var delete_url = base_url + "projects";    // POST with {name}   [challenge in getting DELETE to work]