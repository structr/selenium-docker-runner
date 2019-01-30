/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.nio.file.Paths;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class RunCommand extends AbstractScriptAction {

	private String name = null;

	public RunCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		name = args.string(context, true);
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = getScriptFile(name);
		int lineNumber        = 0;

		out.println();
		out.setInteractive(false);

		for (final String line : file.getLines()) {

			context.runLine(out, line, ++lineNumber, 5);
		}
		
		out.setInteractive(true);

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

	private ScriptFile getScriptFile(final String path) {

		if (path != null) {

			return new ScriptFile(context.getPathRelativeToWorkDir(Paths.get(path)).toString());
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
