/**
 * Refresh project list from server
 *
 *    GET list_url
 *    RESPONSE  list of [name, value, system] constants 
 */
function refreshProjectsList() {
   var xhr = new XMLHttpRequest();
   xhr.open("GET", list_url, true);
   xhr.send();
   
   console.log("sent");

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    if (xhr.readyState == XMLHttpRequest.DONE) {
      console.log ("XHR:" + xhr.responseText);
      processListResponse(xhr.responseText);
    } else {
      processListResponse("N/A");
    }
  };
}

/**
 * Respond to server JSON object.
 *
 * Replace the contents of 'constantList' with a <br>-separated list of name,value pairs.
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

