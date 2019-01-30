/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.common;

import java.util.Set;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class NoninteractiveTerminal implements Terminal {

	private boolean output = false;

	@Override
	public void print(Object... input) {

		output = true;

		for (final Object o : input) {
			System.out.print(o);
		}
	}

	@Override
	public void println(Object... input) {
		print(input);
		println();
	}

	@Override
	public void printBlack(Object... o) {
		print(o);
	}

	@Override
	public void printBlackBold(Object... o) {
		print(o);
	}

	@Override
	public void printRed(Object... o) {
		print(o);
	}

	@Override
	public void printlnBlack(Object... o) {
		println(o);
	}

	@Override
	public void printlnBlackBold(Object... o) {
		println(o);
	}

	@Override
	public void printlnRed(Object... o) {
		println(o);
	}

	@Override
	public void println() {

		output = true;
		
		System.out.println();
	}

	@Override
	public int prompt(String message, Set<Integer> options) {
		return -1;
	}

	@Override
	public boolean prompt(String message) {
		return false;
	}

	@Override
	public void read(String message) {
	}

	@Override
	public boolean isInteractive() {
		return false;
	}

	@Override
	public boolean receivedOutput() {
		return output;
	}

	@Override
	public void resetReceivedOutputFlag() {
		output = false;
	}

	@Override
	public void setInteractive(boolean interactive) {
	}
}
