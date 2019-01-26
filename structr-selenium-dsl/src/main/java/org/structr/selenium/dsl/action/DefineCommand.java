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
public class DefineCommand extends AbstractAction {

	private String name  = null;
	private Object value = null;

	public DefineCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		name  = args.keywordToken(context).name();
		value = args.any(context);
	}

	@Override
	public boolean execute(final Terminal out) {

		context.define(name, value);

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "define <key> <value> - defines a new keyword with the given value.";
	}
}
