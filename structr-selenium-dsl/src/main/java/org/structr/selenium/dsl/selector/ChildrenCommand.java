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
public class ChildrenCommand extends MultiElementSelector {

	private AbstractSelector selector = null;

	public ChildrenCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.abstractSelector(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "child elements of " + selector.getElementMessage();
	}

	@Override
	public List<WebElement> get() {

		final List<WebElement> list = collect(selector.get());
		final int count             = list.size();

		switch (count) {

			case 0:
				throw new IllegalStateException("No results.");

			case 1:
				return list.get(0).findElements(By.xpath("child::*"));

			default:
				throw new IllegalArgumentException("Ambiguous result: " + count + " elements found.");
		}
	}

	@Override
	public String usage() {
		return "children <selector> - returns the child elements of the result of the given selector.";
	}
}
