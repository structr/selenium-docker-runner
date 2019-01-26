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
public class TypeCommand extends AbstractSelectorAction {

	private AbstractSelector selector = null;
	private String text               = null;

	public TypeCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.multiElementSelector(context, false);
		text     = args.string(context, false);
	}

	@Override
	public void init(final SideTest test) {
		selector = elementSelector(test.getTarget());
		text     = test.getValue();
	}

	@Override
	public boolean execute(final Terminal out) {

		final WebElement elem = getFirstOrPrint(out, collect(selector.get()));
		if (elem != null) {

			elem.sendKeys(text);
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Failure: " + selector.getElementMessage() + " not found";
	}

	@Override
	public String usage() {
		return "type <selector> <text> - types the given text in the element selected by selector.";
	}
}
