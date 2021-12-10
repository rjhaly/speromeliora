function processRenameTaskResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  console.log(js);
  var proj = document.getElementById("getProjectDisplay");
  var cons = document.getElementById("consoleMessageDisplay");
  
  var output = "";

  if(js["statusCode"] == 200) {
	// Project Output
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
	cons.innerHTML = "<p>Task Successfully Renamed</p>";
  } else if (js["statusCode"] == 400) {
	output = js["error"];
	cons.innerHTML = output;
  }
  
}
function handleRenameTaskClick(e) {
  var tasksForm = document.renameTaskForm;
 
  var data = {};
  data["arg1"] = tasksForm.renameTaskName.value;
  data["arg2"] = tasksForm.renameTaskID.value;
  data["arg3"] = document.getElementById("workingProject").innerHTML;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", renameTask_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processRenameTaskResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processRenameTaskResponse("N/A");
    }
  };
}