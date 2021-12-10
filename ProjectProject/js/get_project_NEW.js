
/**
 * Respond to server JSON object.
 *
 * returns a project
 */

var output;

function processGetResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  const js = JSON.parse(result);
  let proj = document.getElementById("getProjectDisplay");
  let cons = document.getElementById("consoleMessageDisplay");
  let workingProject = document.getElementById("workingProject");

  if(js["statusCode"] == 200){
	// Project output
	output = "<p>" + "Project Name: " + js["project"]["pid"] + "<br>";
	
	const tsk_ids = js["project"]["identifiers"];
	const tsk_names = js["project"]["tasks"];
	
	output += "</p><div class=\"left\">";
	for (let i = 0; i < tsk_ids.length; i++) {
		const id = tsk_ids[i];
		const name = tsk_names[i];
		// count # of . for tabs
		for (let j = 0; j < id.length; j++)
		  if (id.charAt(j) == '.')
			output += "<span style=\"display:inline-block; width: 40px;\"></span>";
		// display individual task
		output += "<input type=\"checkbox\" id=\"checkbox" + id + "\" onclick=\"JavaScript:handleMarkTaskClick(this, " + id + ")\">" + id + ": " + name + "<br>";
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

