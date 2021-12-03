function processDeleteResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("deleted :" + result);
  
  refreshProjectsList();
}

function requestDelete(val) {
   if (confirm("Request to delete " + name)) {
     processDelete(val);
   }
}

function processDelete(name) {

  var xhr = new XMLHttpRequest();
  xhr.open("POST", delete_url + "/" + name, true);    // ISSUE with POST v. DELETE in CORS/API Gateway

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
	  console.log(xhr);
	  console.log(xhr.request);
	  if (xhr.readyState == XMLHttpRequest.DONE) {
		  if (xhr.status == 200) {
			  console.log ("XHR:" + xhr.responseText);
			  processDeleteResponse(xhr.responseText);
		  } else {
			  console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["error"];
			  alert (err);
		  }
	  } else {
		  processDeleteResponse("N/A");
	  }
  };
  
  xhr.send(null);  //  NEED TO GET IT GOING
}

function handleProjectDeleteClick(e){
	var form = document.getElementById("textBox").value;

  var data = {};
  data["arg1"] = document.getElementById("textBox").value;

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
	      requestDelete(xhr.responseText);
		reload();

    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      requestDelete("N/A");
    }

}
}

