package com.hbm.util.programs;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import api.hbm.computer.ITerminal;
import api.hbm.computer.ITerminal.TerminalCommand;

public abstract class OSBase
{
	protected static final Supplier<CommandLineParser> PARSER = DefaultParser::new;
	protected static final Supplier<HelpFormatter> FORMATTER = HelpFormatter::new;
	
	public String helpString(String command)
	{
		return getOptionMap().containsKey(command) ? '[' + command + ']' + '\n' + getOptionMap().get(command) : "Command: " + command + " not found.";
	}
	public abstract String getName();
	public abstract void executeCommand(TerminalCommand command, ITerminal terminal);
	protected abstract Map<String, Options> getOptionMap();
	protected abstract Map<String, Function<String[], Integer>> getCommandMap();
	@Override
	public abstract String toString();
}
