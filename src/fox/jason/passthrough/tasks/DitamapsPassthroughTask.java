
/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */
package fox.jason.passthrough.tasks;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MacroInstance;

//
//	Iterator function to run a given macro against a set of files
//
public class DitamapsPassthroughTask extends Task {
	/**
	 * Field filesets.
	 */
	private List<FileSet> filesets;
	/**
	 * Field macro.
	 */
	private String macro;
	/**
	 * Field dir.
	 */
	private String dir;
	/**
	 * Field format.
	 */
	private String format;

	/**
	 * Creates a new <code>DitamapsPassthroughTask</code> instance.
	 */
	public DitamapsPassthroughTask() {
		this.macro = null;
		this.filesets = new ArrayList<>();
		this.dir = null;
		this.format = null;
	}


	/**
     * @param set FileSet
     */
    public void addFileset(FileSet set) {
    	this.filesets.add(set);
    }

    /**
	 * Method setMacro.
	 *
	 * @param macro String
	 */
	public void setMacro(String macro) {
		this.macro = macro;
	}

	/**
	 * Method setDir.
	 *
	 * @param dir String
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * Method setFormat.
	 *
	 * @param format String
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
     * Method execute.
     *
     * @throws BuildException if something goes wrong
     */
	@Override
    public void execute() {
		//	@param macro - A macro to run.
		//	@param fileset - A set of files
		//	@param dir - The input directory
		//	@param format - The output directory
		if (macro == null) {
            throw new BuildException("You must supply a macro to run");
        }
        if (filesets.isEmpty()) {
            throw new BuildException("You must supply a set of files");
        }
        if (dir == null) {
            throw new BuildException("You must supply an input directory");
        }
        if (format == null) {
            throw new BuildException("You must supply a format");
        }

		for( FileSet fileset : this.filesets ) {
			DirectoryScanner scanner = fileset.getDirectoryScanner(getProject());
			scanner.scan();

			String[] files = scanner.getIncludedFiles();
			for (int j = 0; j < files.length; j++) {
		    	MacroInstance task = (MacroInstance) getProject().createTask("dita-passthrough");

				try {
					task.setDynamicAttribute("file", this.dir + "/" + files[j]);
					task.setDynamicAttribute("macro", this.macro);
					task.setDynamicAttribute("format", this.format);
					task.execute();
				} catch (Exception err) {
					throw(err);
				}
			}
		}
	}
}