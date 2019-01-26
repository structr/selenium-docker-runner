/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.side;

import java.io.IOException;
import java.nio.file.Path;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.action.AbstractAction;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.command.CommandFactory;
import org.structr.selenium.dsl.common.AbstractTestRunner;

/**
 *
 * @author Christian Morgner
 */
public class SideFileRunner extends AbstractTestRunner {

	private int tests  = 0;
	private int errors = 0;
	private int passed = 0;
	private int failed = 0;

	public SideFileRunner(final Context context, final int width) {

		super(context, width);

		this.context = context;
	}

	public void runSideFile(final Path root, final Path sourcePath) {

		final CommandFactory factory = context.getCommandFactory();
		final String name            = root.relativize(sourcePath).toString();
		final String start           = "START " + name + " ";
		final String end             = "END " + name + " ";

		terminal.println(start + pad("", width - start.length() - 1, "#"));

		// count # of tests
		tests++;

		try {

			final SideFile side = new SideFile(sourcePath);
			int lineNumber           = 0;

			for (final SideTest test : side.getTests()) {

				final String trimmed  = test.toString();
				final int left        = width - trimmed.length() - 8;

				terminal.print(pad(lineNumber, 5, " ") + ": " + trimmed);

				try {

					final Command command = factory.fromSide(context, ++lineNumber, test);
					if (command != null) {

						if (command instanceof AbstractAction) {

							final AbstractAction action = (AbstractAction)command;

							if (command.execute(null)) {

								passed++;

								terminal.println(pad("OK", left, " "));

							} else {

								failed++;

								terminal.println(pad("FAILED", left, " "));
								terminal.println(action.getErrorMessage());

								break;
							}

						} else {

							errors++;

							terminal.println(pad("ERROR", left, " "));
							terminal.println("Error: command must be either action or assertion.");
						}

					} else {

						terminal.println(pad("ERROR", left, " "));
						terminal.println("Error: unknown command \"" + trimmed + "\"");
					}

				} catch (NullPointerException npe) {

					errors++;

					terminal.println(pad("ERROR", left, " "));

					npe.printStackTrace();

					break;

				} catch (Throwable t) {

					errors++;

					terminal.println(pad("ERROR", left, " "));
					terminal.println(t.getMessage());

					break;
				}
			}

		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		terminal.println(end + pad("", width - end.length() - 1, "#"));
	}

	public int getPassed() {
		return passed;
	}

	public int getErrors() {
		return errors;
	}

	public int getFailed() {
		return failed;
	}

	public int getTests() {
		return tests;
	}
}
