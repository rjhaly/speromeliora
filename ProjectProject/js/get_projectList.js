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

  var output = "";

  if (js["statusCode"] === 200) {
	for (let i = 0; i < js["projects"].length; i++) {
		output += "Project Name: " + js["projects"][i]["pid"] + "<br>" +
				  "tasks: ";
		
		const tasks = js["projects"][i]["tasks"];
		for (let j = 0; j < tasks.length; j++)
		  output += tasks[j]["name"] + ",";
		output = output.substring(0, output.length - 1); // trim last ,
		
		output += "<br>" +
				  "teammates: " + js["projects"][i]["teammates"] +  "<br>" +
				  "isArchived: "  + js["projects"][i]["isArchived"] +  "<br><br>";
	}
  } else {
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

