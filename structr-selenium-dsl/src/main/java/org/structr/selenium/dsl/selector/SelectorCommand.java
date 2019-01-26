/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class SelectorCommand extends MultiElementSelector {

	private String selector = null;
	private int wait        = 0;

	public SelectorCommand() {
		super();
	}

	public SelectorCommand(final String selector, final int waitTimeInSeconds) {

		super();

		this.selector = selector;
		this.wait     = waitTimeInSeconds;
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "element with CSS selector \"" + selector + "\"";
	}

	@Override
	public List<WebElement> get() {
		return selector(selector, wait);
	}

	@Override
	public String usage() {
		return "selector <css> - return the elements that match the given css selector.";
	}
}
