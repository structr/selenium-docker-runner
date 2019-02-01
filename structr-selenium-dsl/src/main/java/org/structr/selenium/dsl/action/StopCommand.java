/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class StopCommand extends AbstractAction {

	public StopCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = context.getCurrentScript();
		if (file != null) {

			context.setRecording(false);

			out.println("Recording stopped.");

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
		return "stop - stops recording of script commands to the current script.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
