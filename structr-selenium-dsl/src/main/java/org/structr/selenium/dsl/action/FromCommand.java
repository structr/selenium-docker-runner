/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class FromCommand extends AbstractScriptAction {

	private String prefix = null;

	public FromCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		prefix = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {

		final File file = new File(prefix);

		if (file.exists()) {

			if (file.isDirectory()) {

				context.setWorkDirectory(file);

				return true;
				
			} else {

				throw new IllegalArgumentException(prefix + " is not a directory.");

			}
		}

		throw new IllegalArgumentException(prefix + " not found.");
	}

	@Override
	public String getErrorMessage() {
		return "Assertion failure: directory " + prefix + " not found.";
	}

	@Override
	public String usage() {
		return "from <directory> - set work directory for subsequent run, save and load actions.";
	}
}
