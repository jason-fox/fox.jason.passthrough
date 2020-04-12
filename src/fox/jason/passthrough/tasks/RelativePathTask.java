/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

package fox.jason.passthrough.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

//
//	Converts a value to relative path
//
public class RelativePathTask extends Task {

	/**
	 * Field string.
	 */
	private String src;

	/**
	 * Field to.
	 */
	private String to;

	/**
	 * Creates a new <code>RelativePathTask</code> instance.
	 */
	public RelativePathTask() {
		super();
		this.src = null;
		this.to = null;
	}

	/**
	 * Method setSrc.
	 *
	 * @param src String
	 */
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * Method setTo.
	 *
	 * @param to String
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
     * Method execute.
     *
     * @throws BuildException if something goes wrong
     */
	@Override
    public void execute() {
		//	@param  src -   The source to convert
		//	@param  to -  The property to set
		//
        if (this.to == null) {
            throw new BuildException("You must supply a property to set");
        }
        if (this.src == null) {
            throw new BuildException("You must supply a source to convert");
        }

		String temp = getProject().getProperty("args.input.dir");
		String relative = this.src.replace(temp, "");

		getProject().setProperty(this.to, relative);
	}
}