/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.common.AbstractTestRunner;

/**
 *
 */
public class ScriptFileRunner extends AbstractTestRunner {

	public ScriptFileRunner(final Context context) {
		super(context);
	}

	public void runScriptFile(final Path root, final Path sourcePath) {

		final String name  = root.relativize(sourcePath).toString();
		final String start = "START " + name + " ";
		final String end   = "END " + name + " ";
		final int width    = context.getWidth();

		context.setWorkDirectory(sourcePath.getParent().toFile());

		terminal.println(start, pad("", width - start.length() - 2, "#"));

		// count # of tests
		context.countTest();

		try {

			final List<String> lines = Files.readAllLines(sourcePath);
			int lineNumber           = 0;

			for (final String line : lines) {

				if (!context.runLine(terminal, line, ++lineNumber)) {

					context.takeScreenshot();
					break;
				}
			}

		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		terminal.println(end + pad("", width - end.length() - 1, "#"));
	}
}
