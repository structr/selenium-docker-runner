/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class XPathCommand extends MultiElementSelector {

	private String path = null;
	private int wait    = 0;

	public XPathCommand() {
		super();
	}

	public XPathCommand(final String path, final int waitTimeInSeconds) {

		super();

		this.wait = waitTimeInSeconds;
		this.path = path;
	}

	@Override
	public void init(final TokenQueue args) {
		path = args.string(context, false);
	}

	@Override
	public void init(final SideTest test) {
		path = test.getTarget();
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "element with xpath \"" + path + "\"";
	}

	@Override
	public List<WebElement> get() {
		return xpath(path, wait);
	}

	@Override
	public String usage() {
		return "xpath <path> - returns the element with the given xpath.";
	}
}
