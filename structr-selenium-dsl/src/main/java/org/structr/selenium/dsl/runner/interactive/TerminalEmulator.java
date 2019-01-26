/**
 * Copyright (C) 2010-2018 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.selenium.dsl.runner.interactive;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.apache.commons.lang3.StringUtils;

/**
 *
 *
 */
public class TerminalEmulator extends Thread implements KeyListener, Terminal {

	protected Queue<Integer> keyQueue = new ConcurrentLinkedQueue<>();
	protected JTextPane out           = null;
	protected TerminalHandler handler = null;
	protected boolean running         = false;
	protected boolean echo            = true;
	protected int commandBufferIndex  = 0;
	protected int tabCount            = 0;
	protected Style defaultStyle      = null;
	protected Style boldStyle         = null;
	protected Style redStyle          = null;

	public TerminalEmulator(final JTextPane out, final TerminalHandler handler) {

		this.handler = handler;
		this.out     = out;

		out.addKeyListener(this);

		defaultStyle = (Style)StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE).copyAttributes();
		boldStyle    = (Style)StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE).copyAttributes();
		redStyle     = (Style)StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE).copyAttributes();

		final String font = "Monospaced";

		StyleConstants.setBold(boldStyle, true);
		StyleConstants.setForeground(redStyle, Color.decode("0xcc0000"));
		StyleConstants.setFontFamily(defaultStyle, font);
		StyleConstants.setFontFamily(boldStyle, font);
		StyleConstants.setFontFamily(redStyle, font);
	}

	@Override
	public void run() {

		running = true;

		try { Thread.sleep(100); } catch (Throwable t) {}

		println("Welcome to the Structr Selenium Console.");
		println("Enter \"help\" or \"?\" to get a list of possible commands.");
		println("You can use tab completion for all commands.");
		println();

		printBlackBold(handler.getPrompt());

		while (running) {

			try { Thread.sleep(10); } catch (Throwable t) {}

			final Integer key = keyQueue.poll();
			if (key != null) {

				handleKeyInTerminalEmulatorThread(key);
			}
		}
	}

	public void quit() {
		running = false;
	}

	public void handleNewline() {

		println();

		handleLineInternal(getCurrentLine());

		// let the terminal handler display its prompt
		printBlackBold(handler.getPrompt());
	}

	public void handleTab(final int tabCount) {
		handler.handleTab(tabCount);
	}

	public void handleCursorUp() {

		final List<String> commandHistory = handler.getCommandHistory();
		if (commandHistory != null && echo) {

			final int commandBufferSize = commandHistory.size();

			if (commandBufferIndex >= 0 && commandBufferIndex < commandBufferSize) {

				displaySelectedCommand(commandHistory.get(commandBufferSize - commandBufferIndex - 1));

				if (commandBufferIndex < commandBufferSize - 1) {
					commandBufferIndex++;
				}
			}
		}
	}

	public void handleCursorDown() {

		final List<String> commandHistory = handler.getCommandHistory();
		if (commandHistory != null && echo) {

			if (commandBufferIndex > 0) {

				final int commandBufferSize = commandHistory.size();

				if (commandBufferIndex >= 0 && commandBufferIndex <= commandBufferSize) {

					commandBufferIndex--;
					displaySelectedCommand(commandHistory.get(commandBufferSize - commandBufferIndex - 1));
				}

			} else {

				displaySelectedCommand("");
			}
		}
	}

	public void clearTabCount() {
		tabCount = 0;
	}

	public String getCurrentLine() {
		return getCurrentLine(1);
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		handleKeyInEventDispatcherThread(e, false);
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		handleKeyInEventDispatcherThread(e, true);
	}

	@Override
	public boolean isInteractive() {
		return true;
	}

	// ----- print methods -----
	@Override
	public void print(final Object... text) {
		printBlack(text);
	}

	@Override
	public void printBlack(final Object... text) {
		print(defaultStyle, collect(text), false);
	}

	@Override
	public void printBlackBold(final Object... text) {
		print(boldStyle, collect(text), false);
	}

	@Override
	public void printRed(final Object... text) {
		print(redStyle, collect(text), false);
	}

	@Override
	public void println(final Object... text) {
		print(defaultStyle, collect(text), true);
	}

	@Override
	public void println() {
		print(defaultStyle, "", true);
	}

	@Override
	public void printlnBlack(final Object... text) {
		print(defaultStyle, collect(text), true);
	}

	@Override
	public void printlnBlackBold(final Object... text) {
		print(boldStyle, collect(text), true);
	}

	@Override
	public void printlnRed(final Object... text) {
		print(redStyle, collect(text), true);
	}

	@Override
	public int prompt(final String message, final Set<Integer> options) {

		Integer result = null;

		print(message);

		while (result == null) {

			final Integer key = keyQueue.poll();
			if (key != null) {

				if (options.contains(key)) {

					result = key;
				}
			}
		}

		println();

		return result;
	}

	@Override
	public boolean prompt(final String message) {

		Boolean done = null;

		print(message);

		while (done == null) {

			final Integer key = keyQueue.poll();
			if (key != null) {

				if (key == KeyEvent.VK_Y) {
					done = true;
				}

				if (key == KeyEvent.VK_N) {
					done = false;
				}
			}
		}

		return done;
	}

	@Override
	public void read(final String message) {

		print(message);

		while (keyQueue.poll() == null) {

			try { Thread.sleep(100); } catch (Throwable t) {};
		}

		println();
	}

	// ----- protected methods -----
	protected void handleLineInternal(final String line) {

		if ("exit".equals(line)) {
			running = false;
		}

		handler.handleLine(line);
		commandBufferIndex = 0;
	}

	// ----- private methods -----
	private void handleKeyInEventDispatcherThread(final KeyEvent e, final boolean released) {

		final int key = e.getKeyCode();
		switch (key) {

			case KeyEvent.VK_ENTER:
				if (!released) {
					keyQueue.add(key);
				}
				e.consume();
				break;

			case KeyEvent.VK_TAB:
				if (!released) {
					keyQueue.add(key);
				}
				e.consume();
				break;

			case KeyEvent.VK_HOME:
				moveHome();
				e.consume();
				break;

			case KeyEvent.VK_UP:
				if (!released) {
					keyQueue.add(key);
				}
				e.consume();
				break;

			case KeyEvent.VK_DOWN:
				if (!released) {
					keyQueue.add(key);
				}
				e.consume();
				break;

			case KeyEvent.VK_LEFT:
				if (!cursorMovementAllowed(-1)) {
					e.consume();
				}
				break;

			case KeyEvent.VK_RIGHT:
				if (!cursorMovementAllowed(0)) {
					e.consume();
				}
				break;

			case KeyEvent.VK_BACK_SPACE:
				if (!cursorMovementAllowed(-1)) {
					e.consume();
				}
				break;

			default:
				if (!released) {
					keyQueue.add(key);
				}
		}

		// reset style
		out.setCharacterAttributes(defaultStyle, true);
	}

	private void handleKeyInTerminalEmulatorThread(final int keyCode) {

		switch (keyCode) {

			case KeyEvent.VK_ENTER:
				moveEnd();
				handleNewline();
				break;

			case KeyEvent.VK_TAB:
				handleTab(++tabCount);
				break;

			case KeyEvent.VK_HOME:
				moveHome();
				break;

			case KeyEvent.VK_UP:
				handleCursorUp();
				break;

			case KeyEvent.VK_DOWN:
				handleCursorDown();
				break;

			case KeyEvent.VK_PERIOD:
				handlePeriod();
				break;
		}

		if (keyCode != KeyEvent.VK_TAB) {
			tabCount = 0;
		}
	}

	private String collect(final Object... text) {

		final StringBuilder buf = new StringBuilder();

		for (final Object o : text) {
			buf.append(o);
		}

		return buf.toString();
	}

	private void print(final Style style, final String str, final boolean newline) {

		SwingUtilities.invokeLater(() -> {

			try {

				final Document doc = out.getDocument();

				doc.insertString(out.getCaretPosition(), str, style);

				if (newline) {
					doc.insertString(out.getCaretPosition(), "\n", style);
				}

				out.setCharacterAttributes(defaultStyle, true);

			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		});
	}

	private void displaySelectedCommand(final String selectedCommand) {

		SwingUtilities.invokeLater(() -> {

			try {

				// remove current text
				final Document doc = out.getDocument();
				final int home     = getStartOfCurrentLine();
				final int end      = getEndOfCurrentLine();

				doc.remove(home, end - home);
				doc.insertString(out.getCaretPosition(), selectedCommand, defaultStyle);

			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}

		});
	}

	private boolean cursorMovementAllowed(final int offset) {

		if (getCurrentLine(1).length() == 0) {
			return false;
		}

		if (getCursorColumn() + offset < handler.getPrompt().length()) {
			return false;
		}

		return true;
	}

	private int getLineCount() {
		return out.getDocument().getDefaultRootElement().getElementCount();
	}

	private int getLineStartOffset(final int line) {
		return out.getDocument().getDefaultRootElement().getElement(line).getStartOffset();
	}

	private int getLineEndOffset(final int line) {
		return out.getDocument().getDefaultRootElement().getElement(line).getEndOffset();
	}

	private String getCurrentLine(final int offset) {

		try {


			final int currentLine = Math.max(0, getLineCount() - offset);
			final int prompt      = handler.getPrompt().length();
			final int start       = getLineStartOffset(currentLine) + prompt;
			final int end         = getLineEndOffset(currentLine) - 1;

			if (end >= start) {

				return out.getText(start, end - start);
			}

		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}

		return "";
	}

	private int getCursorColumn() {

		final int currentLine = Math.max(0, getLineCount() - 1);
		final int start       = getLineStartOffset(currentLine);

		return out.getCaretPosition() - start;
	}

	private int getStartOfCurrentLine() {

		final int currentLine = Math.max(0, getLineCount() - 1);
		final int start       = getLineStartOffset(currentLine);
		final int prompt      = handler.getPrompt().length();

		return start + prompt;
	}

	private int getEndOfCurrentLine() {

		final int currentLine = Math.max(0, getLineCount() - 1);

		return getLineEndOffset(currentLine) - 1;
	}

	private void moveEnd() {
		out.setCaretPosition(getEndOfCurrentLine());
	}

	private void moveHome() {
		out.setCaretPosition(getStartOfCurrentLine());
	}

	private void handlePeriod() {

		final String replacement = handler.getPeriodReplacement(getCurrentLine());
		if (StringUtils.isNotBlank(replacement)) {

			SwingUtilities.invokeLater(() -> {

				try {

					// remove period
					final Document doc = out.getDocument();
					final int pos      = out.getCaretPosition() - 1;

					doc.remove(pos, 1);
					doc.insertString(pos, replacement, defaultStyle);

				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}

			});

		}

	}
}
