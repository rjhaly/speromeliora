

function processAddTeammateResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var proj = document.getElementById('getProjectDisplay');
  
  var output = "";

  if(js["statusCode"] === 200) {
	output = "Project Name: " + js["project"]["pid"] 		+ "<br>" +
			 "tasks: " 		  + js["project"]["tasks"] 		+ "<br>" +
			 "teammates: " 	  + js["project"]["teammates"] 	+ "<br>" +
			 "isArchived: "   + js["project"]["isArchived"];
  } else {
	output = "Could not retrieve project";
	proj.innerHTML = output;
  }

  // Update computation result
  proj.innerHTML = output;
}
function handleAddTeammateClick(e) {
  var form = document.addTeammateForm;
 
  var data = {};
  data["arg1"] = form.addTeammateName.value;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", addTeammate_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processAddTeammateResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processAddTeammateResponse("N/A");
    }
  };
}