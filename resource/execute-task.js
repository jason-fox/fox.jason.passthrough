/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

//	Run a given macro against an input and output
//
//	@param macro - A macro to run.
//	@param src - A set of files
//	@param dest - The input directory

var macro = attributes.get("macro");
var src = attributes.get("src");
var dest = attributes.get("dest");
var title = attributes.get("src").replace(/_/g," ");

if(title.contains("/")) {
	title = title.substring(title.lastIndexOf("/") + 1, title.length());
	if(title.contains(".")) {
		title = title.substring(0, title.lastIndexOf("."));
	}
}

var task = project.createTask(macro);
 
try {
	task.setDynamicAttribute("src", src);
	task.setDynamicAttribute("dest", dest);
	task.setDynamicAttribute("title", title);
	task.execute();
} catch (err) {
	task.log("Execution error: " + err.message);
}