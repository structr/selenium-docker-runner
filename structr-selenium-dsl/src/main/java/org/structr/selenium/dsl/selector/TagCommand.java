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
public class TagCommand extends MultiElementSelector {

	private String name = null;

	public TagCommand() {
		super();
	}

	public TagCommand(final String css, final int waitTimeInSeconds) {

		super();

		this.name  = css;
	}

	@Override
	public void init(final TokenQueue args) {
		name = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return true;
	}

	@Override
	public String getElementMessage() {
		return "elements with tag \"" + name + "\"";
	}

	@Override
	public List<WebElement> get() {
		return tagName(name);
	}

	@Override
	public String usage() {
		return "tag <tagName> - list the elements with the given tag name.";
	}
}
