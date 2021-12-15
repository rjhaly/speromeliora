


function processRemoveTeammateResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  console.log(js);
  var proj = document.getElementById("getProjectDisplay");
  var cons = document.getElementById("consoleMessageDisplay");

  var output = "";

  if(js["statusCode"] == 200) {
	
	if (workingView.innerHTML === "projectView") {
	   percentage = 0;
		
		// Project View output
		const pid = js["project"]["pid"];
		output += "<p>" + "Project Name: " + pid + "<br>";
		
		const tasks = js["project"]["tasks"];
		
		output += "</p><div class=\"left\">";
		// Display Tasks
		for (let i = 0; i < tasks.length; i++) {
			t = t + 1;
			const task = tasks[i];
			const identifier = task["taskIdentifier"];
			const name = task["name"];
			const teammates = task["teammates"];
			// count # of . for tabs
			for (let j = 0; j < identifier.length; j++)
			  if (identifier.charAt(j) == '.')
				output += "<span style='display:inline-block; width: 40px;'></span>";
			
			// checkbox for completion status
			const isCompleted = task["isCompleted"];
			if (isCompleted) {
				output += "<img src='checkbox.png'></img>";
				p = p + 1;
			} else {
				output += "<img src='uncheckbox.png'></img>";
			}
			
			// display individual task
			output += identifier + ": " + name + " (" + teammates + ")" + "<br>";
		}
		output += "</div><p>";
		
		output +="teammates: " 	  + js["project"]["teammates"] 	+ "<br>" +
				 "isArchived: "   + js["project"]["isArchived"] + "</p>";
		if (t == 0){
			percentage = 0;
		}
		else {percentage = Math.round(p/t * 100);}
		
		console.log( "t = " + t);
		console.log( "p = " + p);
		console.log(percentage+"%");
		
		
	} else {
		// Team View output
		const pid = js["project"]["pid"];
		output += "<p>" + "Project Name: " + pid + "<br>";
		
		const tasks = js["project"]["tasks"];
		var teammateTasks = [];
		
		// add teammates and tasks to list
		const teammates = js["project"]["teammates"];
		for (let i = 0; i < teammates.length; i++) {
			var newTmt = [];
			const tmtName = teammates[i];
			newTmt.push(tmtName); // first item is tmt name
			// search through tasks to find which have newTmt assigned
			for (let j = 0; j < tasks.length; j++) {
				const task = tasks[j];
				if (task["teammates"].indexOf(tmtName) >= 0) {
					var newTask = [];
					newTask.push(task["taskIdentifier"]);
					newTask.push(task["name"]);
					newTask.push(task["isCompleted"]);
					newTmt.push(newTask);
				}
			}
			teammateTasks.push(newTmt);
		}
		
		
		output += "</p><div class=\"left\">";
		// Display Teammates
		for (let i = 0; i < teammateTasks.length; i++) {
			const teammateTask = teammateTasks[i];
			const tmtName = teammateTask[0];
			output += "<b>" + tmtName + "</b><br>";
			// display teammate's tasks
			for (let j = 1; j < teammateTask.length; j++) {
				output += "<span style='display:inline-block; width: 40px;'></span>";
				const taskIdentifier = teammateTask[j][0];
				const taskName = teammateTask[j][1];
				const isCompleted = teammateTask[j][2];
				
				if (isCompleted)
					output += "<img src='checkbox.png'></img>";
				else
					output += "<img src='uncheckbox.png'></img>";
				
				output += taskIdentifier + ": " + taskName + "<br>";
			}
			output = output.substring(0, output.length - 1) + "<br><br>";
		}
		output += "</div><p>";
	}
	
	// Update computation result
	proj.innerHTML = output;
	cons.innerHTML = "<p>Teammate was removed</p>";
  } else if (js["statusCode"] == 400) {
	output = "<p>" + js["error"] + "</p>";
	cons.innerHTML = output;
  }
  
}
function handleRemoveTeammateClick(e) {
  var projectForm = document.searchForm;
  var teammateForm = document.teammateForm;
 
  var data = {};
  data["arg1"] = document.getElementById("workingProject").innerHTML;
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

