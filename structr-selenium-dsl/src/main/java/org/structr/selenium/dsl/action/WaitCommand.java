/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class WaitCommand extends AbstractAction {

	private int delay = -1;

	public WaitCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		this.delay = args.numberToken(context, false).resolve(context, args);
	}

	@Override
	public void init(final SideTest test) {
		this.delay = Integer.valueOf(test.getTarget());
	}

	@Override
	public boolean execute(final Terminal out) {

		try { Thread.sleep(delay); } catch (Throwable t) {}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "pause <milliseconds> - waits for the given amount of milliseconds.";
	}
}
