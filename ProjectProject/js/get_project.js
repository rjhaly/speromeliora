
/**
 * Respond to server JSON object.
 *
 * returns a project
 */
function processGetResponse(result) {
	

  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var proj = document.getElementById('project');
  
  var output = "";
  for (var i = 0; i < js.list.length; i++) {
    var projectJson = js.list[i];
    console.log(projectJson);
    
    var pname = projectJson["name"];
    output = output + "<div id=\"proj" + pname + "\"><b>" + pname + ":</b> = " + "(<a href='javaScript:requestDelete(\"" + pname + "\")'><img src='deleteIcon.png'></img></a>) <br></div>";
  }

  // Update computation result
  projList.innerHTML = output;
}
function handleGetProjectClick(e){
	var form = document.searchForm;

  var js = JSON.stringify(form.insertName.value);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("GET", getProject_url, true);
  xhr.send(js);

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
}

