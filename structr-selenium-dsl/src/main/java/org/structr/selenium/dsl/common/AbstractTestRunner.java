/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.common;

import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public abstract class AbstractTestRunner {

	protected final Terminal terminal = new NoninteractiveTerminal();
	protected Context context         = null;

	public AbstractTestRunner(final Context context) {
		this.context = context;
	}

	// ----- public static methods -----
	public static String pad(final int number, final int padTo, final String padChar) {
		return pad(number + "", padTo, padChar);
	}

	public static String pad(final String src, final int padTo, final String padChar) {

		final StringBuilder buf = new StringBuilder();

		buf.append(src);

		while (buf.length() < padTo) {
			buf.insert(0, padChar);
		}

		return buf.toString();
	}
}
