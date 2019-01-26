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
public class RemoveCommand extends AbstractAction {

	private int line = -1;

	public RemoveCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		line = args.number(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = context.getCurrentScript();
		if (file != null) {

			final List<String> lines = file.getLines();

			if (lines.isEmpty()) {

				throw new IllegalArgumentException("Current script is empty, cannot remove any more lines.");

			} else {

				if (line > file.size()) {
					throw new IllegalArgumentException("cannot remove line " + line + ", script has only " + file.size() + " lines");
				}

				file.removeCommand(line - 1);
			}

		} else {

			throw new IllegalArgumentException("no script file loaded, use `load` to load one or `new` to create a new script.");
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "no script file loaded.";
	}

	@Override
	public String usage() {
		return "remove <index>  - removes the line with the given index from the current script file.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
