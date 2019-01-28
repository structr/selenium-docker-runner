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
import org.apache.commons.lang3.StringUtils;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.command.CommandFactory;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class StepCommand extends AbstractAction {

	private int start = 0;

	public StepCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		start = args.number(context, true);

		// "empty" value is -1
		if (start == -1) {
			start = 0;
		}
	}

	@Override
	public boolean execute(final Terminal out) {

		final ScriptFile file = context.getCurrentScript();
		if (file != null) {

			final Set<Integer> allOptions = new LinkedHashSet<>(Arrays.asList(KeyEvent.VK_R, KeyEvent.VK_N, KeyEvent.VK_Q, KeyEvent.VK_P, KeyEvent.VK_D));
			final List<String> lines      = file.getLines();
			boolean finished              = false;
			int currentLine               = start;

			while (!finished) {

				run(lines.get(currentLine), out, currentLine++);

				// file done?
				if (currentLine >= lines.size()) {
					return true;
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
					return true;
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
		return "step - runs tests in a loaded script file interactively.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}

	// ----- private methods -----
	private boolean run(final String line, final Terminal output, final int lineNumber) {

		output.println(StringUtils.leftPad((lineNumber + 1) + "", 3), ": ", line);

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
}
