/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.side;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Christian Morgner
 */
public class SideTest extends SideBase {

	private SideFile parent      = null;
	private List<String> targets = null;
	private String id            = null;
	private String comment       = null;
	private String command       = null;
	private String target        = null;
	private String value         = null;


	public SideTest(final SideFile parent, final Map<String, Object> source) {

		this.parent  = parent;
		this.id      = getString(source,     "id",      false);
		this.comment = getString(source,     "comment", true);
		this.command = getString(source,     "command", false);
		this.target  = getString(source,     "target",  false);
		this.value   = getString(source,     "value",   true);
		this.targets = getStringList(source, "targets", true);
	}

	@Override
	public String toString() {

		final StringBuilder buf = new StringBuilder();

		buf.append(this.command);
		buf.append(" ");
		buf.append(this.target);
		buf.append(" ");
		buf.append(this.value);

		return buf.toString();
	}

	public SideFile getParent() {
		return parent;
	}

	public List<String> getTargets() {
		return targets;
	}

	public String getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}

	public String getCommand() {
		return command;
	}

	public String getTarget() {
		return target;
	}

	public String getValue() {
		return value;
	}
}