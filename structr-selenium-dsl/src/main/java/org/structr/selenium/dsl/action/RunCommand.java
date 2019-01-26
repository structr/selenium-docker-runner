/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.command.CommandFactory;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class RunCommand extends AbstractScriptAction {

	private String path = null;

	public RunCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		path = args.string(context, true);
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = getScriptFile(path);
		int lineNumber        = 1;

		for (final String line : file.getLines()) {

			// pad left
			if (lineNumber <   10) { out.print(" "); }
			if (lineNumber <  100) { out.print(" "); }
			if (lineNumber < 1000) { out.print(" "); }

			out.println(lineNumber, ": ", line);

			if (!run(line, out, lineNumber++)) {
				break;
			}
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "no script file loaded.";
	}

	@Override
	public String usage() {
		return "run <path> - runs commands from a loaded script file, optionally loads it from the given path.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}

	// ----- private methods -----
	private boolean run(final String line, final Terminal output, final int lineNumber) {

		try {

			final CommandFactory factory = context.getCommandFactory();
			final Command command        = factory.fromLine(context, lineNumber, line);
			final String trimmed         = line.trim();

			if (command != null) {

				if (command instanceof AbstractAction) {

					final AbstractAction action = (AbstractAction)command;

					if (!command.execute(output)) {

						output.printlnRed("FAILED: " + action.getErrorMessage());

						return false;
					}

				} else {

					output.printlnRed("Error: command must be either action or assertion.");

					return false;
				}

			} else {

				output.printlnRed("Error: unknown command \"" + trimmed + "\"");

				return false;
			}

		} catch (Throwable t) {

			output.printlnRed(t.getMessage());

			return false;
		}

		return true;
	}

	private ScriptFile getScriptFile(final String path) {

		if (path != null) {

			return new ScriptFile(path);
		}

		final Object f = context.getDefined("script");
		if (f != null) {

			if (f instanceof ScriptFile) {

				return (ScriptFile)f;

			} else {

				throw new IllegalArgumentException("Expected script file, got " + f.getClass() + ".");
			}

		} else {

			throw new IllegalArgumentException("No script file loaded, use `load` to load one.");
		}
	}
}
