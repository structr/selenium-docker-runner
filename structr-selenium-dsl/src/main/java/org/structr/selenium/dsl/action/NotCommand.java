/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class NotCommand extends AbstractSelectorAction {

	private Command command = null;

	public NotCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		command = args.command(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return !command.execute(out);
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "not <command> - inverts the return value of the given command.";
	}

	@Override
	public boolean isRecordable() {
		return true;
	}
}
