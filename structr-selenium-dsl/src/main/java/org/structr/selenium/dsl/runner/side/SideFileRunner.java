/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.side;

import java.io.IOException;
import java.nio.file.Path;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.command.CommandFactory;
import org.structr.selenium.dsl.common.AbstractTestRunner;

/**
 *
 * @author Christian Morgner
 */
public class SideFileRunner extends AbstractTestRunner {

	public SideFileRunner(final Context context) {
		super(context);
	}

	public void runSideFile(final Path root, final Path sourcePath) {

		final String name  = root.relativize(sourcePath).toString();
		final String start = "START " + name + " ";
		final String end   = "END " + name + " ";
		final int width    = context.getWidth();

		terminal.println(start + pad("", width - start.length() - 1, "#"));

		// count # of tests
		context.countTest();

		try {

			final SideFile side = new SideFile(sourcePath);
			int lineNumber      = 0;

			for (final SideTest test : side.getTests()) {

				context.runSide(terminal, test, ++lineNumber);
			}

		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		terminal.println(end + pad("", width - end.length() - 1, "#"));
	}
}
