package com.hbm.commands;

import static com.hbm.main.MainRegistry.logger;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandSpawnSquad extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "summonsquad";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/summonsquad";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
	{
		// TODO Auto-generated method stub
		logger.debug("Code not implemented!");
		sender.addChatMessage(new ChatComponentText("Code not implemented!"));
	}

}
