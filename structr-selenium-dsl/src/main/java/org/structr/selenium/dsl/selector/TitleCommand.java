/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import org.openqa.selenium.WebDriver;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class TitleCommand extends StringSelector {

	public TitleCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "page title";
	}

	@Override
	public String get() {

		final WebDriver driver = context.getWebDriver();

		return driver.getTitle();
	}

	@Override
	public String usage() {
		return "title - return the current HTML page title.";
	}
}
