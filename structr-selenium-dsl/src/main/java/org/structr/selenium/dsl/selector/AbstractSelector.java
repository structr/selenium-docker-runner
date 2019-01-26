package org.structr.selenium.dsl.selector;

import org.structr.selenium.dsl.command.Command;

/**
 *
 */
public abstract class AbstractSelector<T> extends Command {

	public AbstractSelector() {
		super();
	}

	public abstract T get();
	public abstract String getElementMessage();
}
