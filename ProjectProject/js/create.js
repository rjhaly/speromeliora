function processCreateResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("project name:" + result);

  var js = JSON.parse(result);
  var cons = document.getElementById("consoleMessageDisplay");

  var output = "";
  var status = js["statusCode"];
  if (status === 200) {
	status = "<p>Project successfully created.</p>";
  } else if (status === 400) {
	status = "<p>Project failed to create.</p>";
  }

  console.log(status);

  cons.innerHTML = status;
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