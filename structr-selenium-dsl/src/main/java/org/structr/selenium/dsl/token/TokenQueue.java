/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.token;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.selector.ElementSelector;
import org.structr.selenium.dsl.selector.StringSelector;

/**
 *
 * @author Christian Morgner
 */
public class TokenQueue {

	private final Queue<Token> queue = new LinkedList<>();

	public void add(final Token token) {
		queue.add(token);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}

	public Token next(final boolean optional) {

		if (queue.isEmpty() && !optional) {
			throw new IllegalArgumentException("Error: missing argument.");
		}

		return queue.poll();
	}

	public CommandToken commandToken(final Context context, final boolean optional) {

		final Token next = next(optional);
		if (next != null) {

			return next.resolveCommandToken(context, this);
		}

		return null;
	}

	public KeywordToken keywordToken(final Context context) {

		final Token next = next(false);
		if (next != null) {

			return next.resolveKeywordToken(context, this);
		}

		return null;
	}

	public StringToken stringToken(final Context context, final boolean optional) {

		final Token next = next(optional);
		if (next != null) {

			return next.resolveStringToken(context, this);
		}

		return null;
	}

	public NumberToken numberToken(final Context context, final boolean optional) {

		final Token next = next(optional);
		if (next != null) {

			return next.resolveNumberToken(context, this);
		}

		return null;
	}

	public AnyToken anyToken(final Context context) {

		final Token next = next(false);
		if (next != null) {

			return next.resolveAnyToken(context, this);
		}

		return null;
	}

	public Command command(final Context context, final boolean optional) {

		final CommandToken commandToken = commandToken(context, optional);
		if (commandToken != null) {

			return commandToken.resolve(context, this);
		}

		return null;
	}

	public String string(final Context context, final boolean optional) {

		final StringToken token = stringToken(context, optional);
		if (token != null) {

			return token.resolve(context, this);
		}

		return null;
	}

	public int number(final Context context, final boolean optional) {

		final NumberToken token = numberToken(context, optional);
		if (token != null) {

			return token.resolve(context, this);
		}

		return -1;
	}

	public AbstractSelector multiElementSelector(final Context context, final boolean optional) {

		final Command command = command(context, optional);

		if (command instanceof AbstractSelector) {

			return (AbstractSelector)command;
		}

		if (!optional) {

			throw new IllegalArgumentException("Command is not an AbstractSelector");
		}

		return null;
	}

	public ElementSelector elementSelector(final Context context, final boolean optional) {

		final Command command = command(context, optional);

		if (command instanceof ElementSelector) {

			return (ElementSelector)command;
		}

		if (!optional) {

			throw new IllegalArgumentException("Command is not a ElementSelector");
		}

		return null;
	}

	public StringSelector stringSelector(final Context context, final boolean optional) {

		final Command command = command(context, optional);

		if (command instanceof StringSelector) {

			return (StringSelector)command;

		}

		if (!optional) {

			throw new IllegalArgumentException("Command is not a StringSelector");
		}

		return null;
	}

	public Object any(final Context context) {

		final AnyToken any = anyToken(context);

		return any.resolve(context, this);
	}

	public List<String> getRawTokens() {

		final List<String> list = new LinkedList<>();

		for (final Token token : this.queue) {

			list.add(token.name());
		}

		return list;
	}
}
