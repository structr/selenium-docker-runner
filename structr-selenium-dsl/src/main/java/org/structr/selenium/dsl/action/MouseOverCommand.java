/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.awt.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class MouseOverCommand extends AbstractSelectorAction {

	private Rectangle offset          = new Rectangle();
	private AbstractSelector selector = null;

	public MouseOverCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		selector = args.abstractSelector(context, false);

		final int x = Math.max(0, args.number(context, true));
		final int y = Math.max(0, args.number(context, true));

		offset.x = x;
		offset.y = y;
	}

	@Override
	public void init(final SideTest test) {

		selector = elementSelector(test.getTarget());
		offset   = parseCoordinates(test.getValue(), ",", "mouseOver");
	}

	@Override
	public boolean execute(final Terminal out) {

		final Actions actions = context.getActions();
		final WebElement elem = getFirstOrPrint(out, collect(selector.get()));

		if (elem != null) {

			actions.moveToElement(elem, offset.x, offset.y).perform();
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Failure: " + selector.getElementMessage() + " not found";
	}

	@Override
	public String usage() {
		return "mouseDownAt <selector> <x> <y> - creates a mouse over event at the given element with the given optional x and y offsets.";
	}
}
