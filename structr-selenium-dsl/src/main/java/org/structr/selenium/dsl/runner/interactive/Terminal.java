/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.structr.selenium.dsl.runner.interactive;

import java.util.Set;

/**
 */
public interface Terminal {

	boolean isInteractive();
	void setInteractive(final boolean interactive);

	void print(final Object... o);
	void println(final Object... o);

	void printBlack(final Object... o);
	void printBlackBold(final Object... o);
	void printRed(final Object... o);

	void printlnBlack(final Object... o);
	void printlnBlackBold(final Object... o);
	void printlnRed(final Object... o);

	void println();

	int prompt(final String message, final Set<Integer> options);
	boolean prompt(final String message);
	void read(final String message);

	boolean receivedOutput();
	void resetReceivedOutputFlag();
}
