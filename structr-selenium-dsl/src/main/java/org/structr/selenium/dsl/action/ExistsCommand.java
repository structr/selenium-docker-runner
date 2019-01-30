/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class ExistsCommand extends AbstractSelectorAction {

	private AbstractSelector selector = null;

	public ExistsCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.multiElementSelector(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
	
		return collect(selector.get()).size() > 0;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "exists <selector> - return true if given selector contains at least one element.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
