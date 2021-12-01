
/**
 * Respond to server JSON object.
 *
 * returns a project
 */
function processGetResponse(result) {
	

  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var proj = document.getElementById('getProjectDisplay');
  
  var output = "";

  if(js["statusCode"] === 200){
	output = "Project Name: " + js["project"]["pid"] + "<br>" +
			 "tasks: " + js["project"]["tasks"] +  "<br>" +
			 "teammates: " + js["project"]["teammates"] +  "<br>" +
			 "isArchived: "  + js["project"]["isArchived"];
}
  else{
	output = "Could not retrieve project";
	proj.innerHTML = output;
}

  // Update computation result
  proj.innerHTML = output;
}

function handleGetProjectClick(e){
	var form = document.searchForm;

  var newURL = getProject_url + "/" + form.insertName.value;
  console.log("JS:" + form.insertName.value);
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

