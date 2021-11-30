// all access driven through BASE. Must end with a SLASH
// be sure you change to accommodate your specific API Gateway entry point
var base_url = "https://67rrc28l32.execute-api.us-east-2.amazonaws.com/Alpha/";

var add_url    = base_url + "calculator";   // POST
var list_url   = base_url + "projects";    // GET
var create_url = base_url + "projects";    // POST

var delete_url = base_url + "projects";    // POST with {name}   [challenge in getting DELETE to work]