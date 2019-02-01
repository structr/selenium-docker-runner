/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.command;

import org.structr.selenium.dsl.token.KeywordToken;
import org.structr.selenium.dsl.token.StringToken;
import org.structr.selenium.dsl.token.NumberToken;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.action.WaitCommand;
import org.structr.selenium.dsl.action.ClickCommand;
import org.structr.selenium.dsl.action.OpenCommand;
import org.structr.selenium.dsl.selector.IdCommand;
import org.structr.selenium.dsl.selector.TitleCommand;
import org.structr.selenium.dsl.selector.SelectorCommand;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.action.ClearCommand;
import org.structr.selenium.dsl.action.DefineCommand;
import org.structr.selenium.dsl.action.HelpCommand;
import org.structr.selenium.dsl.action.ListCommand;
import org.structr.selenium.dsl.action.LoadCommand;
import org.structr.selenium.dsl.action.MouseDownAtCommand;
import org.structr.selenium.dsl.action.MouseOverCommand;
import org.structr.selenium.dsl.action.MouseUpAtCommand;
import org.structr.selenium.dsl.action.NewCommand;
import org.structr.selenium.dsl.action.PrintCommand;
import org.structr.selenium.dsl.action.RecordCommand;
import org.structr.selenium.dsl.action.RemoveCommand;
import org.structr.selenium.dsl.action.RunCommand;
import org.structr.selenium.dsl.action.RunScriptCommand;
import org.structr.selenium.dsl.action.SaveCommand;
import org.structr.selenium.dsl.action.SelectCommand;
import org.structr.selenium.dsl.action.SetWindowSizeCommand;
import org.structr.selenium.dsl.action.StepCommand;
import org.structr.selenium.dsl.action.StopCommand;
import org.structr.selenium.dsl.action.TypeCommand;
import org.structr.selenium.dsl.action.AssertCommand;
import org.structr.selenium.dsl.action.DeleteCommand;
import org.structr.selenium.dsl.action.DoubleClickCommand;
import org.structr.selenium.dsl.action.ExistsCommand;
import org.structr.selenium.dsl.action.FindCommand;
import org.structr.selenium.dsl.action.FromCommand;
import org.structr.selenium.dsl.action.IfCommand;
import org.structr.selenium.dsl.action.NotCommand;
import org.structr.selenium.dsl.action.PatchCommand;
import org.structr.selenium.dsl.action.PostCommand;
import org.structr.selenium.dsl.action.PutCommand;
import org.structr.selenium.dsl.action.SendKeysCommand;
import org.structr.selenium.dsl.selector.ClassCommand;
import org.structr.selenium.dsl.selector.GetCommand;
import org.structr.selenium.dsl.selector.NameCommand;
import org.structr.selenium.dsl.selector.StringCommand;
import org.structr.selenium.dsl.selector.TagCommand;
import org.structr.selenium.dsl.selector.TextCommand;
import org.structr.selenium.dsl.selector.XPathCommand;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.selector.ChildCommand;
import org.structr.selenium.dsl.selector.ChildrenCommand;
import org.structr.selenium.dsl.selector.ParentCommand;

/**
 */
public class CommandFactory {

	private final Map<String, Class<? extends Command>> commands = new LinkedHashMap<>();
	private static final Pattern TOKEN_PATTERN                   = Pattern.compile("\'([^\']*)\'|\"([^\"]*)\"|(\\S+)");
	private static final Pattern NUMBER_PATTERN                  = Pattern.compile("\\-*[0-9]+[\\.]*[0-9]*");

	public CommandFactory() {

		commands.put("#",                     ChildCommand.class);
		commands.put("assert",                AssertCommand.class);
		commands.put("children",              ChildrenCommand.class);
		commands.put("class",                 ClassCommand.class);
		commands.put("clear",                 ClearCommand.class);
		commands.put("click",                 ClickCommand.class);
		commands.put("css",                   SelectorCommand.class);
		commands.put("define",                DefineCommand.class);
		commands.put("delete",                DeleteCommand.class);
		commands.put("doubleclick",           DoubleClickCommand.class);
		commands.put("exists",                ExistsCommand.class);
		commands.put("from",                  FromCommand.class);
		commands.put("find",                  FindCommand.class);
		commands.put("get",                   GetCommand.class);
		commands.put("help",                  HelpCommand.class);
		commands.put("id",                    IdCommand.class);
		commands.put("if",                    IfCommand.class);
		commands.put("list",                  ListCommand.class);
		commands.put("load",                  LoadCommand.class);
		commands.put("mouseDownAt",           MouseDownAtCommand.class);
		commands.put("mouseMoveAt",           MouseOverCommand.class);
		commands.put("mouseOver",             MouseOverCommand.class);
		commands.put("mouseUpAt",             MouseUpAtCommand.class);
		commands.put("name",                  NameCommand.class);
		commands.put("new",                   NewCommand.class);
		commands.put("not",                   NotCommand.class);
		commands.put("open",                  OpenCommand.class);
		commands.put("parent",                ParentCommand.class);
		commands.put("patch",                 PatchCommand.class);
		commands.put("pause",                 WaitCommand.class);
		commands.put("post",                  PostCommand.class);
		commands.put("print",                 PrintCommand.class);
		commands.put("put",                   PutCommand.class);
		commands.put("record",                RecordCommand.class);
		commands.put("remove",                RemoveCommand.class);
		commands.put("run",                   RunCommand.class);
		commands.put("runScript",             RunScriptCommand.class);
		commands.put("save",                  SaveCommand.class);
		commands.put("select",                SelectCommand.class);
		commands.put("sendKeys",              SendKeysCommand.class);
		commands.put("setWindowSize",         SetWindowSizeCommand.class);
		commands.put("step",                  StepCommand.class);
		commands.put("stop",                  StopCommand.class);
		commands.put("string",                StringCommand.class);
		commands.put("tag",                   TagCommand.class);
		commands.put("text",                  TextCommand.class);
		commands.put("title",                 TitleCommand.class);
		commands.put("type",                  TypeCommand.class);
		commands.put("waitForElementVisible", FindCommand.class);
		commands.put("xpath",                 XPathCommand.class);
	}

	public Command uninitializedForName(final String name) {

		final Class<? extends Command> type = commands.get(name);
		if (type != null) {

			try {

				return type.newInstance();

			} catch (InstantiationException|IllegalAccessException ex) {
				throw new IllegalArgumentException(ex.getMessage());
			}
		}

		return null;
	}

	public Command forName(final Context context, final String name, final TokenQueue args) {

		final Class<? extends Command> type = commands.get(name);
		if (type != null) {

			try {

				final Command cmd = type.newInstance();

				cmd.setContext(context);
				cmd.init(args);

				return cmd;

			} catch (InstantiationException|IllegalAccessException ex) {
				throw new IllegalArgumentException(ex.getMessage());
			}
		}

		return null;
	}

	public Command fromLine(final Context context, final String line) {

		// ignore comments
		if (!line.startsWith("#")) {

			final TokenQueue args = split(line);
			if (!args.isEmpty()) {

				return args.command(context, false);
			}
		}

		return null;
	}

	public Command fromSide(final Context context, final int lineNumber, final SideTest test) {

		final Class<? extends Command> type = commands.get(test.getCommand());
		if (type != null) {

			try {

				final Command cmd = type.newInstance();

				cmd.setContext(context);
				cmd.init(test);

				return cmd;

			} catch (InstantiationException|IllegalAccessException ex) {
				throw new IllegalArgumentException(ex.getMessage());
			}
		}

		return null;
	}

	public Map<String, Class<? extends Command>> getCommands() {
		return commands;
	}

	public Set<String> getCommandNames() {
		return commands.keySet();
	}

	public TokenQueue split(final String source) {

 		final Matcher matcher  = TOKEN_PATTERN.matcher(source);
		final TokenQueue parts = new TokenQueue();

		while (matcher.find()) {

			if (matcher.group(1) != null) {

				parts.add(new StringToken(matcher.group(1)));

        		} else if (matcher.group(2) != null) {

				parts.add(new StringToken(matcher.group(2)));

        		} else {

				final String match = matcher.group(3);

				if (NUMBER_PATTERN.matcher(match).matches()) {

					parts.add(new NumberToken(Double.valueOf(match).intValue()));

				} else {

					parts.add(new KeywordToken(matcher.group(3)));
				}
        		}
		}

		return parts;
	}
}
