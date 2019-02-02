/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.io.File;
import java.nio.file.Paths;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class SaveCommand extends AbstractScriptAction {

	private String path = null;

	public SaveCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		path = args.string(context, true);
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile scriptFile = context.getCurrentScript();
		if (scriptFile != null) {

			if (scriptFile.getPath() == null) {

				if (path != null) {

					final File file = getWorkDir().resolve(Paths.get(path)).toFile();

					if (!file.exists() || out.prompt("File exists, overwrite? (y/n) ") ) {

						scriptFile.setPath(file.getAbsolutePath());
						scriptFile.save();
					}

				} else {

					out.println("Script file has no path yet, please provide path parameter.");
				}

			} else {

				scriptFile.save();
			}

		} else {

			out.println("No script to save, use `load` or `new` to change that.");
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "File " + path + " not found";
	}

	@Override
	public String usage() {
		return "save <path> - Saves a script file (asks for a path if none exists yet)";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
