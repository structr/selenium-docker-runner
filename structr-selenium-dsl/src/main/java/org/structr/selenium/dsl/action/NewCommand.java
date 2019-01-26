/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import static com.sun.glass.events.KeyEvent.VK_A;
import static com.sun.glass.events.KeyEvent.VK_N;
import static com.sun.glass.events.KeyEvent.VK_Y;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class NewCommand extends AbstractAction {

	public NewCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile current = context.getCurrentScript();

		if (current != null && current.hasChanges()) {

			switch (out.prompt("Current script (" + current.getPath() + ") has unsaved changes,\ndo you want to save them, or abort? (y/n/a) ", options(VK_Y, VK_N, VK_A))) {

				case 'y':
					current.save();
					break;

				case 'n':
					break;

				case 'a':
					return true;
			}
		}

		context.setCurrentScript(new ScriptFile());
		out.println("New script created.");

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "no script file loaded.";
	}

	@Override
	public String usage() {
		return "new - creates and stores a new script file in the current context.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
