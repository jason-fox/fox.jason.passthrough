/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

//	Iterator function to run a given macro against a set of files
//
//	@param macro - A macro to run.
//	@param fileset - A set of files
//	@param dir - The input directory
//	@param format - The output directory

var filesets = elements.get("fileset");
var macro = attributes.get("macro");
var dir = attributes.get("dir");
var format = attributes.get("format");

for (var i = 0; i < filesets.size(); ++i) {
  var fileset = filesets.get(i);
  var scanner = fileset.getDirectoryScanner(project);
  scanner.scan();
  var files = scanner.getIncludedFiles();
  for (var j = 0; j < files.length; j++) {
    var task = project.createTask("dita-passthrough");
    if (files[i] !== "") {
      try {
        task.setDynamicAttribute("file", dir + "/" + files[j]);
        task.setDynamicAttribute("macro", macro);
        task.setDynamicAttribute("format", format);
        task.execute();
      } catch (err) {
        throw(err);
      }
    }
  }
}