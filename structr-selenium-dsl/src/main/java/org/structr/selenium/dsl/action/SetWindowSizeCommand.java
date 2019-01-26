/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.awt.Rectangle;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class SetWindowSizeCommand extends AbstractAction {

	private Rectangle dim = new Rectangle();

	public SetWindowSizeCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		dim = parseCoordinates(args.string(context, false), "x", "setWindowSize");
	}

	@Override
	public void init(final SideTest test) {
		dim = parseCoordinates(test.getTarget(), "x", "setWindowSize");
	}

	@Override
	public boolean execute(final Terminal out) {

		final WebDriver driver = context.getWebDriver();

		driver.manage().window().setSize(new Dimension(dim.x, dim.y));

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "setWindowSize <WxH> - sets the dimensions of the test browser window.";
	}
}
