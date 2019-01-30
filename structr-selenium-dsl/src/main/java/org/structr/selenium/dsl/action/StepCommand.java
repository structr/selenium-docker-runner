/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class StepCommand extends AbstractAction {

	public StepCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = context.getCurrentScript();
		if (file != null) {

			final Set<Integer> allOptions = new LinkedHashSet<>(Arrays.asList(KeyEvent.VK_R, KeyEvent.VK_N, KeyEvent.VK_Q, KeyEvent.VK_P, KeyEvent.VK_D));
			final List<String> lines      = file.getLines();
			boolean finished              = false;
			int currentLine               = 0;

			out.setInteractive(false);

			while (!finished && currentLine < lines.size()) {

				final String line = lines.get(currentLine);
				
				if (line.length() > 0 && !line.startsWith("#")) {

					context.runLine(out, line, currentLine + 1, 0);

					// file done?
					if (currentLine >= lines.size()) {
						break;
					}

					switch (out.prompt("----- next/previous/repeat/delete/quit? (n/p/r/d/q) ", allOptions)) {

						case KeyEvent.VK_D:
							file.removeCommand(currentLine);
							break;

						case KeyEvent.VK_R:
							currentLine--;
							break;

						case KeyEvent.VK_P:
							currentLine -= 2;
							break;

						case KeyEvent.VK_N:
							break;

						case KeyEvent.VK_Q:
							finished = true;
							break;
					}

					if (finished) {
						break;
					}

					// file done?
					if (currentLine >= lines.size()) {
						finished = true;
					}

				}

				currentLine++;
			}

			out.setInteractive(true);

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
		return "step - runs tests in a loaded script file interactively.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
