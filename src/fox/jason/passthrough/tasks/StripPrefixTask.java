/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

package fox.jason.passthrough.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

//
//	Converts a value to lower case
//

public class StripPrefixTask extends Task {

	/**
	 * Field string.
	 */
	private String string;

	/**
	 * Field to.
	 */
	private String to;

	/**
	 * Creates a new <code>StripPrefixTask</code> instance.
	 */
	public StripPrefixTask() {
		super();
		this.string = null;
		this.to = null;
	}

	/**
	 * Method setString.
	 *
	 * @param string String
	 */
	public void setString(String string) {
		this.string = string;
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
    public void execute() throws BuildException {
		//	@param  string -   The value to convert
		//	@param  to -  The property to set
		//
        if (this.to == null) {
            throw new BuildException("You must supply a value to convert");
        }
        if (this.string == null) {
            throw new BuildException("You must supply a property to set");
        }
		getProject().setProperty(this.to, this.string.substring(5));
	}
}