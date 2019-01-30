package org.structr.selenium.dsl.action;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 *
 */
public abstract class AbstractAction extends Command {

	public AbstractAction() {
		super();
	}

	public abstract String getErrorMessage();

	public boolean isRecordable() {
		return true;
	}

	// ----- public static methods -----
	public static void printPadded(final Terminal out, final Collection<String> list) {

		final Optional<Integer> value = list.stream().map(String::length).max(Integer::compare);
		final int pad                 = value.orElse(0);

		list.stream().map(e -> { return StringUtils.rightPad(e, pad + 3, " "); }).forEach(out::print);

		out.println();
	}

	public static String getDescription(final WebElement element) {

		final StringBuilder buf = new StringBuilder();
		final String id         = element.getAttribute("id");
		final String css        = element.getAttribute("class");

		buf.append(element.getTagName());

		if (StringUtils.isNotBlank(id)) {
			buf.append("#");
			buf.append(id);
		}

		if (StringUtils.isNotBlank(css)) {
			buf.append(".");
			buf.append(css.replaceAll("[\\s]+", "."));
		}

		return buf.toString();
	}

	public static Rectangle parseCoordinates(final String source, final String separator, final String commandName) {

		final String[] parts = source.split(separator, 2);
		if (parts.length == 2) {

			return new Rectangle(Integer.valueOf(parts[0].trim()), Integer.valueOf(parts[1].trim()), 0, 0);
		}

		return new Rectangle();
	}

	// ----- protected methods -----
	protected WebElement getFirstOrPrint(final Terminal out, final List<WebElement> result) {

		final int count = result.size();

		switch (count) {

			case 0:
				printOrThrow(out, "No results.");
				break;

			case 1:
				return result.get(0);

			default:
				printOrThrow(out, "Ambiguous result: " + count + " elements found.");
				AbstractAction.printPadded(out, result.stream().map(AbstractAction::getDescription).collect(Collectors.toList()));
				break;
		}

		return null;
	}

	protected Set<Integer> options(final int... options) {

		final Set<Integer> keys = new LinkedHashSet<>();

		for (final int key : options) {

			keys.add(key);
		}

		return keys;
	}

	// ----- private methods -----
	private void printOrThrow(final Terminal out, final String message) {

		if (out.isInteractive()) {

			out.println(message);

		} else {

			throw new IllegalArgumentException(message);
		}
	}
}
