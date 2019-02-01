/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;
import org.structr.selenium.dsl.selector.AbstractSelector;

/**
 */
public class AssertCommand extends AbstractAction {

	private AbstractSelector selector = null;
	private Object value              = null;

	public AssertCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		selector = args.abstractSelector(context, false);
		value    = args.any(context);
	}

	@Override
	public boolean execute(final Terminal out) {
		return value.equals(selector.get());
	}

	@Override
	public String getErrorMessage() {
		return "expected \"" + value + "\", got \"" + selector.get() + "\"";
	}

	@Override
	public String usage() {
		return "assert <expected> <actual> - compares expected and actual values.";
	}
}
