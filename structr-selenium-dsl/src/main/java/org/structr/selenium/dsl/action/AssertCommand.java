/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.action.AbstractAction;
import org.structr.selenium.dsl.selector.StringSelector;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class AssertCommand extends AbstractAction {

	private StringSelector selector = null;
	private String value             = null;

	public AssertCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		selector = args.stringSelector(context, false);
		value    = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return value.equals(selector.get());
	}

	@Override
	public String getErrorMessage() {
		return "Assertion failure: " + selector.getElementMessage() + " not found.";
	}

	@Override
	public String usage() {
		return "assert <expected> <actual> - compares expected and actual values.";
	}
}
