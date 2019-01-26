/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.interactive;

/**
 */
public class Completion {

	private boolean appendSpace = false;
	private String displayValue = null;
	private String value        = null;

	public Completion(final String value, final boolean appendSpace) {
		this(value, value, appendSpace);
	}

	public Completion(final String displayValue, final String value, final boolean appendSpace) {

		this.appendSpace  = appendSpace;
		this.displayValue = displayValue;
		this.value        = value;

	}

	public boolean appendSpace() {
		return appendSpace;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getCompletion(final String part) {
		return value.substring(part.length());
	}
}
