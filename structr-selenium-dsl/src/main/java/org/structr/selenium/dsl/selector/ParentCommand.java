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
public class ParentCommand extends ElementSelector {

	private AbstractSelector selector = null;

	public ParentCommand() {
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
		return "parent element of " + selector.getElementMessage();
	}

	@Override
	public WebElement get() {

		final List<WebElement> list = collect(selector.get());
		final int count             = list.size();

		switch (count) {

			case 0:
				throw new IllegalStateException("No results.");

			case 1:
				return list.get(0).findElement(By.xpath("parent::*"));

			default:
				throw new IllegalArgumentException("Ambiguous result: " + count + " elements found.");
		}
	}

	@Override
	public String usage() {
		return "parent <selector> - returns the parent element of the result of the given selector.";
	}
}
