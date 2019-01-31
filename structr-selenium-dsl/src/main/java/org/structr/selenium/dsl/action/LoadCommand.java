/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class LoadCommand extends AbstractScriptAction {

	private String name = null;

	public LoadCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		name = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {

		final Path path             = getWorkDir().resolve(Paths.get(name));
		final ScriptFile scriptFile = new ScriptFile(path.toString());

		context.undefine("script");
		context.define("script", scriptFile);

		out.println(name + " successfully loaded, " + scriptFile.size() + " tests.");
		out.println("Use `list` to examine tests, `run` to run, `step` to step through interactively.");

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "File " + name + " not found";
	}

	@Override
	public String usage() {
		return "load <path> - Loads a script file from the given path.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
