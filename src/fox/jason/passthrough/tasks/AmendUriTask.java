/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

package fox.jason.passthrough.tasks;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

//
//	Converts a URI using a relative path.
//

public class AmendUriTask extends Task {

	/**
	 * Field to.
	 */
	private String to;

	/**
	 * Field path.
	 */
	private String path;

	/**
	 * Field href.
	 */
	private String href;

	/**
	 * Creates a new <code>AmendUriTask</code> instance.
	 */
	public AmendUriTask() {
		super();
		this.path = null;
		this.href = null;
		this.to = null;
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
	 * Method setPath.
	 *
	 * @param path String
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Method setHref.
	 *
	 * @param href String
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
     * Method execute.
     *
     * @throws BuildException if something goes wrong
     */
	@Override
    public void execute() {
		//	@param  path -   The relative path of the file
		//	@param  href -  The relative path of the image
		//
        if (this.path == null) {
            throw new BuildException("You must supply a path");
        }
        if (this.href == null) {
            throw new BuildException("You must supply an href");
        }
        if (this.to == null) {
            throw new BuildException("You must supply a property to set");
        }
		

		List<String> fullPath = new ArrayList<>();

		for (String elem : this.path.split("/")) {
			if (!"".equals(elem)){
				fullPath.add(elem);
			}
		}


		for (String elem : this.href.split("/")) {
			if ("".equals(elem)){
				// Do nothing
			} else if ("..".equals(elem)){
				fullPath.remove(fullPath.size() - 1);
			} else {
				fullPath.add(elem);
			}
		}

		getProject().setProperty(this.to,  String.join("/", fullPath));
	}
}