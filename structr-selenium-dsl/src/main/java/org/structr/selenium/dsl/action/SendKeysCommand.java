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
public class SendKeysCommand extends AbstractSelectorAction {

	private String text = null;

	public SendKeysCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		text     = args.string(context, false);
	}

	@Override
	public void init(final SideTest test) {
		text     = test.getValue();
	}

	@Override
	public boolean execute(final Terminal out) {

		context.getActions().sendKeys(text).perform();

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "sendKeys <text> - sends the given text to the browser window.";
	}
}
