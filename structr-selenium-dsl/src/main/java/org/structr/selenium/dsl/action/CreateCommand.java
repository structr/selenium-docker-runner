/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class CreateCommand extends AbstractAction {

	private String type = null;
	private String data = null;

	public CreateCommand() {
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
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "create <type> <JSON> - creates an object with then given data via REST.";
	}
}
