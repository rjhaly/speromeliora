

function processRemoveTeammateResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  console.log(js);
  var proj = document.getElementById("getProjectDisplay");
  var cons = document.getElementById("consoleMessageDisplay");
  
  var output = "";

  if(js["statusCode"] == 200) {
	output = "<p>" +
			 "Project Name: " + js["project"]["pid"] 		+ "<br>" +
			 "tasks: " 		  + js["project"]["tasks"] 		+ "<br>" +
			 "teammates: " 	  + js["project"]["teammates"] 	+ "<br>" +
			 "isArchived: "   + js["project"]["isArchived"] + "</p>";
	// Update computation result
	proj.innerHTML = output;
	cons.innerHTML = "<p>Console Message Display</p>";
  } else if (js["statusCode"] == 400) {
	output = "<p>" + js["error"] + "</p>";
	cons.innerHTML = output;
  }
  
}
function handleRemoveTeammateClick(e) {
  var projectForm = document.searchForm;
  var teammateForm = document.teammateForm;
 
  var data = {};
  data["arg1"] = projectForm.searchProjectName.value;
  data["arg2"] = teammateForm.addTeammateName.value;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", removeTeammate_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processRemoveTeammateResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processRemoveTeammateResponse("N/A");
    }
  };
}