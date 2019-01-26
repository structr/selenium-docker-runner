/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.util.List;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class ListCommand extends AbstractAction {

	public ListCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = context.getCurrentScript();
		if (file != null) {

			final List<String> lines = file.getLines();
			int lineNumber           = 1;

			if (lines.isEmpty()) {

				out.println("Current script is empty, use `record` to add commands.");

			} else {

				for (final String line : file.getLines()) {

					// pad left
					if (lineNumber <   10) { out.print(" "); }
					if (lineNumber <  100) { out.print(" "); }
					if (lineNumber < 1000) { out.print(" "); }

					out.println(lineNumber, ": ", line);

					lineNumber++;

					if ((lineNumber % 20) == 0) {
						out.read("----- press any key -----");
					}
				}
			}

		} else {

			throw new IllegalArgumentException("No script file loaded, use `load` to load one or `new` to create a new script.");
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "no script file loaded.";
	}

	@Override
	public String usage() {
		return "list <num|start> <end>  - lists all commands in a loaded script file, use `list 5` to list the first 5 commands, `list 10 20` to list commands 10 to 20.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
