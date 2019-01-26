/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class StringCommand extends AbstractSelector<String> {

	final StringBuilder buf = new StringBuilder();

	public StringCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		while (!args.isEmpty()) {

			final Object value = args.any(context);

			buf.append(value);
		}
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String get() {
		return buf.toString();
	}

	@Override
	public String getElementMessage() {
		return "toString";
	}

	@Override
	public String usage() {
		return "string <args..> - concatenates all its arguments into a single string.";
	}
}
