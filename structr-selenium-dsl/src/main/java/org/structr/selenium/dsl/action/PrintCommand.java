/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class PrintCommand extends AbstractAction {

	final StringBuilder buf = new StringBuilder();

	public PrintCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		while (!args.isEmpty()) {

			final String value = args.string(context, false);

			buf.append(value);
		}
	}

	@Override
	public boolean execute(final Terminal out) {

		out.println();
		out.println("Print: " + buf.toString());

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "print <...> - prints the given strings to the log file.";
	}
}
