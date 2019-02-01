/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class SelectCommand extends AbstractAction {

	private AbstractSelector selector = null;
	private String value              = null;

	public SelectCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.abstractSelector(context, false);
		value    = args.string(context, false);
	}

	@Override
	public void init(final SideTest test) {
		selector = elementSelector(test.getTarget());
		value    = test.getValue();
	}

	@Override
	public boolean execute(final Terminal out) {

		final WebElement elem = getFirstOrPrint(out, collect(selector.get()));
		if (elem != null) {

			final String[] parts = value.split("[=]+", 2);
			final Select select  = new Select(elem);

			if (parts.length == 2) {

				final String type = parts[0];
				final String val  = parts[1];

				switch (type) {

					case "label":
						select.selectByVisibleText(val);
						break;

					case "value":
						select.selectByValue(val);
						break;

					case "index":
						select.selectByIndex(Integer.valueOf(val));
						break;

					case "id":
					default:
						throw new IllegalArgumentException("Selection by index is not supported yet.");
				}
				
			} else {

				throw new IllegalArgumentException("Value has wrong format: " + value);
			}
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Failure: " + selector.getElementMessage() + " not found";
	}

	@Override
	public String usage() {
		return "select <selector> - returns an element that matches the given selector.";
	}
}
