/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.structr.selenium.dsl.runner.interactive;

import java.util.List;

/**
 */
public interface TerminalHandler {

	public List<String> getCommandHistory();
	public String getPrompt();
	public void handleLine(final String line);
	public void handleTab(final int tabCount);
	public String getPeriodReplacement(final String line);
}
