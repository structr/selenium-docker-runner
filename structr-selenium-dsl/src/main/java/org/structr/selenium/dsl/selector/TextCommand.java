/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class TextCommand extends MultiElementSelector {

	private String text = null;

	public TextCommand() {
		super();
	}

	public TextCommand(final String text, final int waitTimeInSeconds) {

		super();

		this.text = text;
	}

	@Override
	public void init(final TokenQueue args) {
		text = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return true;
	}

	@Override
	public String getElementMessage() {
		return "elements with text \"" + text + "\"";
	}

	@Override
	public List<WebElement> get() {
		return text(text);
	}

	@Override
	public String usage() {
		return "text <string> - list elements that contain the given text.";
	}
}
