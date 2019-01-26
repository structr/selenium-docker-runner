/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.util.LinkedList;
import java.util.List;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.token.Token;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class HelpCommand extends AbstractAction {

	private final List<String> params = new LinkedList<>();

	public HelpCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		while (!args.isEmpty()) {

			final Token token = args.next(true);
			if (token != null) {

				params.add(token.name());
			}
		}
	}

	@Override
	public boolean execute(final Terminal out) {

		if (out != null) {

			if (params.isEmpty()) {

				AbstractAction.printPadded(out, context.getCommandFactory().getCommandNames());
				out.println();

			} else {

				for (final String param : params) {

					final Command cmd = context.getCommandFactory().uninitializedForName(param);
					if (cmd != null) {

						out.println(cmd.usage());
					}
				}
			}
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Failure: ";
	}

	@Override
	public String usage() {
		return "help <command> - prints the help text for the given command.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
