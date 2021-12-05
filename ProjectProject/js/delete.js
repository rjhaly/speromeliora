function processDelete(response) {
var workingProject = document.getElementById("workingProject");
var projectButton = document.getElementById("getProjectDisplay");

projectButton.innerHTML = "";
workingProject.innerHTML = "";
}

function handleProjectDeleteClick(e){
	

  var data = {};
  data["arg1"] = document.getElementById("workingProject").innerHTML;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", deleteProject_url, true);

  // send the collected data as JSON
  xhr.send(js);

  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processDelete(xhr.responseText)

    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processDelete("N/A");
		
    }

}
}

