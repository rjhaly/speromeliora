
/**
 * Respond to server JSON object.
 *
 * returns a project
 */
function processGetResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var proj = document.getElementById("getProjectDisplay");
  var cons = document.getElementById("consoleMessageDisplay");
  var workingProject = document.getElementById("workingProject");
  var output = "";

  if(js["statusCode"] == 200){
	
	output = "<p>" + "Project Name: " + js["project"]["pid"] + "<br>";
	
	const tsk_ids = js["project"]["identifiers"];
	const tsk_names = js["project"]["tasks"];
	
	for (let i = 0; i < tsk_ids.length; i++) {
		const id = tsk_ids[i];
		const name = tsk_names[i];
		output += "<input type=\"checkbox\" id=\"checkbox" + id + "\" onclick=\"JavaScript:handleMarkTaskClick(this, " + id + ")\">" + id + ": " + name + "<br>";
	}
	
	output +="teammates: " 	  + js["project"]["teammates"] 	+ "<br>" +
			 "isArchived: "   + js["project"]["isArchived"] + "</p>";
	// Update computation result
	proj.innerHTML = output;
	cons.innerHTML = "<p>Console Message Display</p>";
	workingProject.innerHTML = js["project"]["pid"];
  } else if (js["statusCode"] == 400) {
	output = "<p>Could not retrieve project</p>";
	proj.innerHTML = "<p></p>";
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
	      processGetResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processGetResponse("N/A");
    }

}
}

