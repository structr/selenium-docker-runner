/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class ClearCommand extends AbstractSelectorAction {

	private AbstractSelector selector = null;

	public ClearCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.abstractSelector(context, false);
	}

	@Override
	public void init(final SideTest test) {
		selector = elementSelector(test.getTarget());
	}

	@Override
	public boolean execute(final Terminal out) {

		final WebElement elem = getFirstOrPrint(out, collect(selector.get()));
		if (elem != null) {

			elem.clear();
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Failure: " + selector.getElementMessage() + " not found";
	}

	@Override
	public String usage() {
		return "clear <selector> - removes all text from the given editable element.";
	}
}
