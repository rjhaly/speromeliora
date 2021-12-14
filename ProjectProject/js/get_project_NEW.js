
/**
 * Respond to server JSON object.
 *
 * returns a project
 */

function processGetProjectResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  const js = JSON.parse(result);
  let proj = document.getElementById("getProjectDisplay");
  let cons = document.getElementById("consoleMessageDisplay");
  let workingProject = document.getElementById("workingProject");
  var workingView = document.getElementById("workingView");
  p = 0;
  t = 0;
  percentage = 0;
  let per = document.getElementById("percentagedisplay");

  var output = "";
  

  if(js["statusCode"] == 200){
	
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
			output += identifier + ": " + name + "<br>";
		}
		output += "</div><p>";
		
		output +="teammates: " 	  + js["project"]["teammates"] 	+ "<br>" +
				 "isArchived: "   + js["project"]["isArchived"] + "</p>";
			
		percentage = Math.round(p/t * 100);
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
			output += "<b>" + tmtName + "</b><br>" + 
					  "<span style='display:inline-block; width: 40px;'></span>";
			for (let j = 1; j < teammateTask.length; j++) {
				const taskIdentifier = teammateTask[j][0];
				const taskName = teammateTask[j][1];
				output += taskIdentifier + ",";
			}
			output = output.substring(0, output.length - 1) + "<br>";
		}
		output += "</div><p>";
	}
	
	// Update computation result
	proj.innerHTML = output;
	cons.innerHTML = "<p>Console Message Display</p>";
	workingProject.innerHTML = js["project"]["pid"];
	per.innerHTML = "<p>"+ "Percentage complete:"+ percentage+ "%" + "</p>";
	
	
  } else {
	output = "<p>Could not retrieve project</p>";
	proj.innerHTML = "<p></p>";
	workingProject.innerHTML = "";
	cons.innerHTML = output;
	per.innerHTML = "";
  }
  
}

function handleGetProjectClick(e){
  var form = document.searchForm;
	
  var newURL = getProject_url + "/" + form.searchProjectName.value;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", newURL, true);
  xhr.send();

  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processGetProjectResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processGetProjectResponse("N/A");
    }

  }

}

function refreshProjectClick() {
  const workingProject = document.getElementById("workingProject").innerHTML;

  if (workingProject.length > 0) {
	
	  var newURL = getProject_url + "/" + workingProject;
	  var xhr = new XMLHttpRequest();
	  xhr.open("GET", newURL, true);
	  xhr.send();
	
	  xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	 if (xhr.status == 200) {
		      console.log ("XHR:" + xhr.responseText);
		      processGetProjectResponse(xhr.responseText);
	    	 } else {
	    		 console.log("actual:" + xhr.responseText)
				  var js = JSON.parse(xhr.responseText);
				  var err = js["response"];
				  alert (err);
	    	 }
	    } else {
	      processGetProjectResponse("N/A");
	    }
	
	  }
  } else {
	  console.log("no project, no need to toggle view");
  }
}


