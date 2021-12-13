

function processCreateResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("project name:" + result);

  var js = JSON.parse(result);
  var proj = document.getElementById("getProjectDisplay");
  var cons = document.getElementById("consoleMessageDisplay");
  var workingProject = document.getElementById("workingProject");

  var status = js["statusCode"];

  if (status === 200) {
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
	// Update Displays
	workingProject.innerHTML = js["project"]["pid"];
  	cons.innerHTML = "<p>Project successfully created.</p>";
	proj.innerHTML = output;
  } else if (status === 400) {
  	cons.innerHTML = "<p>Project failed to create.</p>";
  }

  console.log(status);

}
function handleCreateClick(e) {
  var form = document.createForm;
 
  var data = {};
  data["arg1"] = form.createProjectName.value;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", createProject_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processCreateResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processCreateResponse("N/A");
    }
  };
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