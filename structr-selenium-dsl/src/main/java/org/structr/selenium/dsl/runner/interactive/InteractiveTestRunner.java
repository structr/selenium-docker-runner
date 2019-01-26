/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.interactive;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.action.AbstractAction;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.command.CommandFactory;
import org.structr.selenium.dsl.common.AbstractTestRunner;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.token.TokenQueue;

/**
 *
 */
public class InteractiveTestRunner extends AbstractTestRunner implements TerminalHandler {

	private final List<String> commandHistory = new LinkedList<>();
	private TerminalEmulator term             = null;

	public InteractiveTestRunner(final Context context, final int width) {

		super(context, width);

		this.context = context;
		this.commandHistory.add("mouseOver div text \"div\"");
	}

	public int run() throws InterruptedException {

		final JTextPane out = new JTextPane();

		// start swing thread..
		new Thread(() -> {

			final JFrame frame  = new JFrame("Structr Selenium Runner");

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(new JScrollPane(out), BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setBounds(getBoundsFromSettings());

			final DefaultCaret caret = (DefaultCaret)out.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {

					try {

						final File settings = new File(".structr-selenium-runner");

						try (final PrintWriter writer = new PrintWriter(new FileWriter(settings))) {

							final Rectangle bounds = frame.getBounds();

							writer.println("position: " + bounds.x + "," + bounds.y);
							writer.println("dimensions: " + bounds.width + "x" + bounds.height);

							writer.flush();
							writer.close();
						}

					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			});

		}).start();

		term = new TerminalEmulator(out, this);

		term.start();
		term.join();

		return 0;
	}

	// ----- interface TerminalHandler -----
	@Override
	public List<String> getCommandHistory() {
		return commandHistory;
	}

	@Override
	public String getPrompt() {
		return "selenium> ";
	}

	@Override
	public void handleLine(final String line) {

		final CommandFactory factory = context.getCommandFactory();
		final String trimmed         = line.trim();

		if (trimmed.length() > 0) {

			if ("exit".equals(trimmed)) {

				term.println("Exiting..");
				term.quit();
				return;
			}

			try {

				commandHistory.add(trimmed);

				final Command command = factory.fromLine(context, 0, trimmed);
				if (command != null) {

					if (command instanceof AbstractAction) {

						final AbstractAction action = (AbstractAction)command;

						if (!command.execute(term)) {

							term.printlnRed("Failed: " + action.getErrorMessage());

						} else if (context.isRecording() && action.isRecordable()) {

							final ScriptFile file = context.getCurrentScript();
							if (file != null) {

								term.println("Action recorded.");
								file.addCommand(trimmed);
							}
						}

					} else {

						term.printlnRed("Error: command must be either action or assertion.");
					}

				} else {

					term.printlnRed("Error: unknown command \"" + trimmed + "\"");
				}

			} catch (Throwable t) {

				term.printlnRed("Error: " + t.getMessage());
			}
		}
	}

	@Override
	public void handleTab(int tabCount) {

		final List<Completion> possibilities = new LinkedList<>();
		final CommandFactory factory         = context.getCommandFactory();
		final String line                    = term.getCurrentLine();
		final TokenQueue tokens              = factory.split(line);
		final List<String> parts             = tokens.getRawTokens();

		if (line.endsWith(" ")) {
			parts.add("");
		}

		final int count                      = parts.size();
		final String last                    = count == 0 ? "" : stripQuotes(parts.get(count - 1));

		if (count == 0) {

			collectCommands(possibilities, line);

		} else {

			boolean hasResults = false;

			if (count > 1) {

				final String previous = parts.get(count - 2);
				final Command cmd     = factory.uninitializedForName(previous);

				if (cmd != null) {

					cmd.setContext(context);

					// command found, let it handle tab completion
					final List<Completion> results = cmd.getAutocompleteResults(last);
					if (results != null) {

						possibilities.addAll(results);
						hasResults = true;
					}
				}
			}

			if (!hasResults) {
				collectCommands(possibilities, last);
			}
		}

		doTabCompletion(possibilities, tabCount, line, last);
	}

	@Override
	public String getPeriodReplacement(final String line) {

		if (line.endsWith(" .")) {

			final CommandFactory factory = context.getCommandFactory();
			final TokenQueue tokens      = factory.split(line.substring(0, line.length() - 2));

			if (tokens.size() == 1 && !commandHistory.isEmpty()) {

				final String previous = commandHistory.get(commandHistory.size() - 1);

				// return everything after the first space character
				return previous.substring(previous.indexOf(" ") + 1);
			}
		}

		return null;
	}

	private void collectCommands(final List<Completion> possibilities, final String part) {

		for (final String name : context.getCommandFactory().getCommandNames()) {

			if (name.startsWith(part)) {

				possibilities.add(new Completion(name, true));
			}
		}
	}

	private void doTabCompletion(final List<Completion> possibilities, final int tabCount, final String line, final String part) {

		if (possibilities.size() == 1) {

			final Completion result = possibilities.get(0);
			final String repl       = result.getCompletion(part);

			//print replacement
			term.print(repl);

			// append space?
			if (result.appendSpace() && !line.endsWith(" ")) {
				term.print(" ");
			}

			term.clearTabCount();

		} else if (!possibilities.isEmpty()) {

			if (tabCount == 1) {

				Completion buf = possibilities.get(0);

				// find longest common string
				for (final Completion candidate : possibilities) {

					diff(buf, candidate);
				}

				// print string part that is common to all possibilities
				if (buf.getValue().length() > 0) {

					term.print(buf.getCompletion(part));
				}

			} else {

				boolean show = true;

				if (possibilities.size() > 50) {

					term.println();

					show = term.prompt("Display all " + possibilities.size() + " possibilities? (y/n) ");
				}

				term.println();

				if (show) {
					AbstractAction.printPadded(term, possibilities.stream().map(Completion::getDisplayValue).collect(Collectors.toList()));
				}

				term.printBlackBold(getPrompt());
				term.print(line);
				term.clearTabCount();
			}
		}
	}

	private String stripQuotes(final String source) {

		if (source != null) {

			String result = source;

			if (result.startsWith("\"") || result.startsWith("\'")) {
				result = result.substring(1);
			}

			if (result.endsWith("\"") || result.endsWith("\'")) {
				result = result.substring(0, result.length() - 1);
			}

			return result;
		}

		return null;
	}

	private void diff(final Completion source, final Completion other) {

		final StringBuilder buf = new StringBuilder();
		final char[] c1         = source.getValue().toCharArray();
		final char[] c2         = other.getValue().toCharArray();
		final int l1            = c1.length;
		final int l2            = c2.length;
		int i                   = 0;

		while (i < l1 && i < l2 && c1[i] == c2[i]) {

			buf.append(c1[i++]);
		}

		source.setValue(buf.toString());
	}

	private Rectangle getBoundsFromSettings() {

		final File settings    = new File(".structr-selenium-runner");
		final Rectangle bounds = new Rectangle(0, 0, 800, 400);

		if (settings.exists()) {

			try (final BufferedReader reader = new BufferedReader(new FileReader(settings))) {

				reader.lines().forEach(line -> {

					if (line.startsWith("position: ")) {

						final Rectangle position = AbstractAction.parseCoordinates(line.substring(10), ",", "getBoundsFromSettings");

						bounds.x = position.x;
						bounds.y = position.y;
					}

					if (line.startsWith("dimensions: ")) {

						final Rectangle position = AbstractAction.parseCoordinates(line.substring(12), "x", "getBoundsFromSettings");

						bounds.width  = position.x;
						bounds.height = position.y;
					}
				});

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return bounds;
	}
}
