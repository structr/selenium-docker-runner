/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class RunScriptCommand extends AbstractAction {

	private String script = null;

	public RunScriptCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		script = args.string(context, false);
	}

	@Override
	public void init(final SideTest test) {
		script = test.getTarget();
	}

	@Override
	public boolean execute(final Terminal out) {

		final WebDriver driver = context.getWebDriver();

		((JavascriptExecutor)driver).executeScript(script);

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Failure: script error";
	}

	@Override
	public String usage() {
		return "runScript <script> - runs the given Javascript expression.";
	}
}
