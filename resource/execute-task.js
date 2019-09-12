/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

//	Run a given macro against an input and output
//
//	@param macro - A macro to run.
//	@param src - The source topic filename
//	@param dest - The destination topic filename
//	@param metadata - Topic metadata

var macro = attributes.get("macro");
var src = attributes.get("src");
var dest = attributes.get("dest");
var title = attributes.get("src").replace(/_/g," ");
var metadata = attributes.get("metadata");
var regex= /^\/[a-z]:/i;

if(title.contains("/")) {
	title = title.substring(title.lastIndexOf("/") + 1, title.length());
	if(title.contains(".")) {
		title = title.substring(0, title.lastIndexOf("."));
	}
}

if (regex.test(src)){
	src = src.substring(1);
}
if (regex.test(dest)){
	dest = dest.substring(1);
}

var task = project.createTask(macro);
 
try {
	task.setDynamicAttribute("src", src);
	task.setDynamicAttribute("dest", dest);
	task.setDynamicAttribute("title", title);
	task.setDynamicAttribute("metadata", metadata);
	task.execute();
} catch (err) {
	task.log("Execution error: " + err.message);
}
