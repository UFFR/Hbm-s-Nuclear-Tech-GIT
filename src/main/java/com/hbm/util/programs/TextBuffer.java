package com.hbm.util.programs;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.hbm.lib.Library;

import api.hbm.computer.File;
import api.hbm.computer.ITerminal;
import api.hbm.computer.Program;
import api.hbm.computer.ReturnCode;
import api.hbm.internet.IDataStorageSystem;
import net.minecraft.nbt.NBTTagCompound;

public class TextBuffer extends Program
{
	private final ArrayList<String> strings = new ArrayList<String>();
	public TextBuffer(ITerminal terminal)
	{
		super(terminal);
	}
	
	@Override
	public void execute()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(String... args)
	{
		// TODO Auto-generated method stub
		ram = getSize();
	}

	private int getSize()
	{
		int size = 0;
		for (String s : strings)
			size += s.length() * 8;
		return size;
	}
	
	@Override
	public void keyTypeHook(char c, int i)
	{
		switch (i)
		{
		case Keyboard.KEY_F2: strings.remove(strings.size() - 1); break;
		case Keyboard.KEY_F8:
			if (terminal instanceof IDataStorageSystem)
			{
				File file = new File(strings.get(0), getSize(), terminal.getInternalDate());
				((IDataStorageSystem) terminal).writeFile(file);
			}
			else
				terminal.handleReturn(ReturnCode.NO_DISC, "System has no file system!");
		case Keyboard.KEY_F10: complete(); break;
		default:
			break;
		}
	}

	@Override
	public boolean requestingConsoleInput()
	{
		return true;
	}

	@Override
	public void acceptConsoleInputHook(String s)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnCode onProcessKill()
	{
		return ReturnCode.NO_ERROR;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByteArray("programData", Library.compressStringCollection(strings));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		strings.clear();
		strings.addAll(Library.decompressStringBytes(nbt.getByteArray("programData")));
	}

}
