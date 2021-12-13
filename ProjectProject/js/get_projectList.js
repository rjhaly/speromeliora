/**
 * Refresh constant list from server
 *
 *    GET list_url
 *    RESPONSE  list of [name, value, system] constants 
 */
function processGetListResponse(result) {
	

  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var proj = document.getElementById('projectListDisplay');

  if(js["statusCode"] === 200){
	for(var i = 0; i < js["projects"].length; i++){
		output = output + "Project Name: " + js["projects"][i]["pid"] + "<br>" +
						  "tasks: " + js["projects"][i]["tasks"] +  "<br>" +
						  "teammates: " + js["projects"][i]["teammates"] +  "<br>" +
						  "isArchived: "  + js["projects"][i]["isArchived"] +  "<br><br>";
	}
}
  else{ 
	output = "Could not retrieve projects";
}

  // Update computation result
  proj.innerHTML = output;
}

function handleListProjectClick(e){
	
  var xhr = new XMLHttpRequest();
  xhr.open("GET", listProject_url, true);
  xhr.send();

  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processGetListResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processGetListResponse("N/A");
    }

}
}

