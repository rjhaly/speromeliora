// all access driven through BASE. Must end with a SLASH
// be sure you change to accommodate your specific API Gateway entry point
var base_url = "https://umrdxh6f76.execute-api.us-east-2.amazonaws.com/Alphiwalphi";

var list_url   = base_url + "projectList";    // GET
var create_url = base_url + "projectCreation";    // POST
var get_url    = base_url + "project";

// var delete_url = base_url + "projects";    // POST with {name}   [challenge in getting DELETE to work]