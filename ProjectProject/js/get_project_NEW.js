
/**
 * Respond to server JSON object.
 *
 * returns a project
 */

let output;
let identifier;
let tid;
let pid;
let isCompleted = false;

function processGetProjectResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  const js = JSON.parse(result);
  let proj = document.getElementById("getProjectDisplay");
  let cons = document.getElementById("consoleMessageDisplay");
  let workingProject = document.getElementById("workingProject");

  if(js["statusCode"] == 200){
	// Project output
	pid = js["project"]["pid"];
	output = "<p>" + "Project Name: " + pid + "<br>";
	
	const tsk_ids = js["project"]["identifiers"];
	const tsk_names = js["project"]["tasks"];
	
	output += "</p><div class=\"left\">";
	for (let i = 0; i < tsk_ids.length; i++) {
		identifier = tsk_ids[i];
		const name = tsk_names[i];
		getTaskCompletionStatus(); // update tid, isCompleted
		// count # of . for tabs
		for (let j = 0; j < identifier.length; j++)
		  if (identifier.charAt(j) == '.')
			output += "<span style=\"display:inline-block; width: 40px;\"></span>";
		// display individual task
		console.log(tid);
		output += "<input type=\"checkbox\"" + (isCompleted ? "checked" : "unchecked") + " id=\"checkbox" + tid +
				  "\" onclick=\"JavaScript:getTaskCompletionStatus(); JavaScript:handleMarkTaskClick(this, " + tid + ")\">" +
				  identifier + ": " + name + "<br>";
	}
	output += "</div><p>";
	
	output +="teammates: " 	  + js["project"]["teammates"] 	+ "<br>" +
			 "isArchived: "   + js["project"]["isArchived"] + "</p>";
	// Update computation result
	proj.innerHTML = output;
	cons.innerHTML = "<p>Console Message Display</p>";
	workingProject.innerHTML = js["project"]["pid"];
  } else if (js["statusCode"] == 400) {
	output = "<p>Could not retrieve project</p>";
	proj.innerHTML = "<p></p>";
	workingProject.innerHTML = "";
	cons.innerHTML = output;
  }
}

function handleGetProjectClick(e){
  var form = document.searchForm;
	
  var newURL = getProject_url + "/" + form.searchProjectName.value;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", newURL, true);
  xhr.send();

  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processGetProjectResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processGetProjectResponse("N/A");
    }

  }

}

function getTaskCompletionStatus() {
  var newURL = getTask_url + "?arg1=" + pid + "&arg2=" + identifier;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", newURL, true);
  xhr.send();

  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      const js = JSON.parse(xhr.responseText);
		  tid = js["task"]["taskID"];
		  isCompleted = js["task"]["isCompleted"];
		  console.log(tid);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    }
  }
}

