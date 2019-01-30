/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class ChildCommand extends MultiElementSelector {

	private int index                 = -1;
	private AbstractSelector selector = null;

	public ChildCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		index    = args.number(context, false);
		selector = args.multiElementSelector(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "child element #" + index + " of " + selector.getElementMessage();
	}

	@Override
	public List<WebElement> get() {

		final List<WebElement> list = collect(selector.get());

		return list.subList(index, index+1);
	}

	@Override
	public String usage() {
		return "# <index> <selector> - returns the child element with the given index of the result of the given selector.";
	}
}
