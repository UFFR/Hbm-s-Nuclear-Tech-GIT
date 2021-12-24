package com.hbm.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.hbm.util.Tuple.Triplet;

import api.hbm.internet.IDataUser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import scala.actors.threadpool.Arrays;
/**
 * Collection for anything related to supercomputer terminals or other computers
 * @author UFFR
 *
 */
@Beta
public class TerminalUtil
{
	private TerminalUtil() throws InstantiationException
	{
		throw new InstantiationException("Instantiation not allowed!");
	}
	public static final HashSet<Class<? extends ProgramBase>> registeredPrograms = new HashSet<>();
	public static final String[] startupLogo =
		{
				"█████          █████         █████",
				"██ ██         ██   ██        ██ ██",
				"██  ██       ██     ██       ██  ██",
				"█████        ██     ██       █████",
				"██   ██      ██     ██       ██   ██",
				"██  ██        ██   ██        ██  ██",
				"██████         █████         ██████",
				"Bobsoft Computational Services Inc.",
				" BobOS v.4080 31 Edition (c) 2077"
		};
	static
	{
		registeredPrograms.add(Echo.class);
		registeredPrograms.add(Help.class);
		registeredPrograms.add(DisplayFiles.class);
	}
	public static Map<String, ProgramBase> getPrograms(Kernel kern)
	{
		HashMap<String, ProgramBase> out = new HashMap<>(registeredPrograms.size());
		try
		{
			for (Class<? extends ProgramBase> programClass : registeredPrograms)
			{
				ProgramBase program = programClass.getConstructor(Kernel.class).newInstance(kern);
				out.put(program.name(), program);
			}
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		return out;
	}
	/**
	 * Will interact with the display and text input of the computer. Basically a glorified I/O
	 * @author UFFR
	 *
	 */
	public static final class Kernel
	{
		private final ArrayList<String> textBuffer = new ArrayList<String>();
		public final ArrayList<Triplet<Integer, Integer, String>> floatingTextBuffer = new ArrayList<>();
		private static final String NBT_KEY = "NTM_COMPUTER_TERMINAL_NBT";
		public final Map<String, ProgramBase> availPrograms;
		private final int lineCount;
		private final int maxLength;
		private Optional<ProgramBase> currentProgram = Optional.empty();
		private Optional<FileManager> fileSystem = Optional.empty();
		public Kernel(int lines, int length)
		{
			lineCount = lines;
			maxLength = length;
			availPrograms = getPrograms(this);
		}
		public void runCommand(String cmd)
		{
			print('>' + cmd);
			String[] parsed = cmd.split(" ");
			String[] args = new String[parsed.length - 1];
			for (int i = 1; i < parsed.length; i++)
				args[i] = parsed[i];
			if (availPrograms.containsKey(parsed[0]))
			{
				currentProgram = Optional.of(availPrograms.get(parsed[0]));
				currentProgram.get().execute(args);
			}
			else
				print("Unknown command: ".concat(parsed[0]));
		}
		public Optional<ProgramBase> getCurrentProgram()
		{
			return currentProgram;
		}
		public Optional<FileManager> getFileSystem()
		{
			return fileSystem;
		}
		public void setCurrentProgram(@Nullable ProgramBase newProgram)
		{
			this.currentProgram = newProgram == null ? Optional.empty() : Optional.of(newProgram);
		}
		public void setFileSystem(FileManager fSys)
		{
			fileSystem = Optional.of(fSys);
		}
		public void print(String...strings)
		{
			ArrayList<String> toAdd = new ArrayList<String>(Arrays.asList(strings));
			for (int i = 0; i < toAdd.size(); i++)
			{
				if (toAdd.get(i).length() > maxLength)
				{
					String truncated = toAdd.get(i).substring(0, getMaxLength());
					String remainder = toAdd.get(i).substring(getMaxLength(), toAdd.get(i).length() - 1);
					toAdd.add(i, truncated);
					toAdd.remove(i + 1);
					toAdd.add(i + 1, remainder);
					toAdd.remove(i + 2);
				}
			}
			textBuffer.addAll(toAdd);
		}
		public void print(int x, int y, String text)
		{
			floatingTextBuffer.add(new Triplet<Integer, Integer, String>(x, y, text));
		}
		/**
		 * @return the amount of lines in the display the buffer will be used in
		 */
		public int getMaxLineCount()
		{
			return lineCount;
		}
		/**
		 * @return the maximum amount of characters per line before a wrap is required
		 */
		public int getMaxLength()
		{
			return maxLength;
		}
		public void clearScreen()
		{
			textBuffer.clear();
			floatingTextBuffer.clear();
		}
		public String[] getTextForDisp()
		{
			String[] toDisp = new String[lineCount > textBuffer.size() ? textBuffer.size() : lineCount];
			List<String> temp = textBuffer.subList(lineCount > textBuffer.size() ? 0 : textBuffer.size() - lineCount, textBuffer.size() - 1);
			toDisp = temp.toArray(toDisp);
			return toDisp;
		}
		public void writeToNBT(NBTTagCompound nbt)
		{
			NBTTagCompound subTag = new NBTTagCompound();
			subTag.setBoolean("isRunningProgram", currentProgram.isPresent());
			subTag.setString("currentProgram", currentProgram.isPresent() ? currentProgram.get().name() : "");
			for (int i = 0; i < textBuffer.size(); i++)
				subTag.setString("textBuffer_" + i, textBuffer.get(i));
			nbt.setTag(NBT_KEY, subTag);
		}
		public void readFromNBT(NBTTagCompound nbt)
		{
			NBTTagCompound subTag = nbt.getCompoundTag(NBT_KEY);
			if (subTag.getBoolean("isRunningProgram"))
				currentProgram = Optional.of(availPrograms.get(subTag.getString("currentProgram")));
			int i = 0;
			clearScreen();
			while (subTag.hasKey("textBuffer_" + i))
				textBuffer.add(subTag.getString("textBuffer_" + i));
		}
	}
	/**
	 * Simple implementation of the {@link IDataUser} interface
	 * @author UFFR
	 *
	 */
	public static final class FileManager implements IDataUser
	{
		private final HashMap<String, File> registry = new HashMap<String, TerminalUtil.File>();
		private final long maxCapacity;
		public FileManager(long capacity)
		{
			maxCapacity = capacity;
		}
		@Override
		public long getMaxCapacity()
		{
			return maxCapacity;
		}

		@Override
		public long getUsedSpace()
		{
			long space = 0;
			for (File file : registry.values())
				space += file.getSize();
			return space;
		}

		@Override
		public boolean writeFile(File file)
		{
			if (file.getSize() + getUsedSpace() > getMaxCapacity())
				return false;
			else
			{
				registry.put(file.getName(), file);
				return true;
			}
		}

		@Override
		public boolean deleteFile(String filename)
		{
			if (registry.containsKey(filename))
			{
				registry.remove(filename);
				return true;
			}
			else
				return false;
		}
		
		@Override
		public boolean overwriteFile(File file)
		{
			deleteFile(file.getName());
			return writeFile(file);
		}
		
		@Override
		public File getFile(String filename)
		{
			return registry.get(filename);
		}
		
		@Override
		public NBTTagCompound extractNBT()
		{
			NBTTagCompound data = new NBTTagCompound();
			for (File file : registry.values())
				data.setTag(file.getName(), file.pullNBT());
			return data;
		}
		
		@Override
		public Collection<File> getFileList()
		{
			return registry.values();
		}
	}
	public static final class File
	{
		public static final String KEY_FILENAME = "FILE_NAME";
		public static final String KEY_TEXTCONTENT = "TEXT_CONTENT";
		public static final String KEY_FILESIZE = "FILE_SIZE";
		private NBTTagCompound data = new NBTTagCompound();
		public File(String name)
		{
			data.setString(KEY_FILENAME, name);
		}
		public File(String name, NBTTagCompound data)
		{
			this(name);
			this.data = data;
		}
		public String getName()
		{
			return data.getString(KEY_FILENAME);
		}
		public NBTTagCompound pullNBT()
		{
			return (NBTTagCompound) data.copy();
		}
		public long getSize()
		{
			return data.getLong(KEY_FILESIZE);
		}
		public void addData(String name, NBTBase data, @Nonnegative long size)
		{
			this.data.setTag(name, data);
			this.data.setLong(name.concat("_SIZE"), size);
			this.data.setLong(KEY_FILESIZE, getSize() + size);
		}
	}
	/**
	 * Use the {@code print()} function to print lines of text</br>
	 * Use {@code execute(...)} for initialization</br>
	 * {@code execute()} for code that runs every tick</br>
	 * And {@code complete()} to end program
	 * @author UFFR
	 *
	 */
	public abstract static class ProgramBase
	{
		private final Kernel kernel;
		private boolean executionComplete = false;
		public ProgramBase(Kernel kernel)
		{
			this.kernel = kernel;
		}
		public final void print(String...strings)
		{
			kernel.print(strings);
		}
		public final Map<String, ProgramBase> getPrograms()
		{
			return kernel.availPrograms;
		}
		public final void complete()
		{
			executionComplete = true;
		}
		public final boolean isExecutionComplete()
		{
			return executionComplete;
		}
		public final Optional<FileManager> getFileManager()
		{
			return kernel.fileSystem;
		}
		public abstract String name();
		public abstract void execute();
		public abstract void execute(String...args);
		public abstract String[] helpCommand();
		public abstract void keyTypeHook(char c, int i);
	}
	public static final class Echo extends ProgramBase
	{
		public Echo(Kernel buffer)
		{
			super(buffer);
		}
		@Override
		public String name()
		{
			return "echo";
		}
		@Override
		public void execute()
		{
			print("");
		}
		@Override
		public void execute(String... args)
		{
			print(args);
			complete();
		}
		@Override
		public String[] helpCommand()
		{
			return new String[] {"Prints out the specified string"};
		}
		@Override
		public void keyTypeHook(char c, int i)
		{
			// Not needed
		}
	}
	public static final class Help extends ProgramBase
	{
		public Help(Kernel buffer)
		{
			super(buffer);
		}
		@Override
		public String name()
		{
			return "help";
		}
		@Override
		public void execute()
		{
			// No need
		}
		@Override
		public void execute(String... args)
		{
			for (String arg : args)
			{
				if (getPrograms().containsKey(arg))
					print(getPrograms().get(arg).helpCommand());
				else
					print("No help documentation found for specified argument!");
			}
			complete();
		}
		@Override
		public String[] helpCommand()
		{
			return new String[] {"Prints out the help text of the program", "May be similar to using the -h flag"};
		}
		@Override
		public void keyTypeHook(char c, int i)
		{
			// No need
		}
	}
	public static final class DisplayFiles extends ProgramBase
	{
		boolean refined;
		String parameter = "";
		int index = 0;
		List<File> list;
		public DisplayFiles(Kernel kernel)
		{
			super(kernel);
		}
		@Override
		public String name()
		{
			return "cd";
		}

		@Override
		public void execute()
		{
			if (index == list.size())
				complete();
			else
			{
				File file = list.get(index);
				print(file.getName() + " (" + file.getSize() + " b)");
				index++;
			}
		}

		@Override
		public void execute(String... args)
		{
			if (!getFileManager().isPresent())
			{
				print("No file system present!");
				complete();
				return;
			}
			switch (args.length)
			{
			case 0:
				refined = false;
				break;
			case 1:
				refined = true;
				parameter = args[0];
				break;
			default:
				print("Invalid parameters: ".concat(Arrays.toString(args)));
				complete();
				return;
			}
			ArrayList<File> tmpList = new ArrayList<>(getFileManager().get().getFileList());
			if (refined)
			{
				Iterator<File> iterator = tmpList.iterator();
				while (iterator.hasNext())
				{
					File toCheck = iterator.next();
					if (!toCheck.getName().contains(parameter))
						tmpList.remove(toCheck);
				}
			}
			list = tmpList;
		}

		@Override
		public String[] helpCommand()
		{
			return new String[] {"Displays all relevant files", "	If used with a string parameter, only shows files", "	that match said parameter"};
		}

		@Override
		public void keyTypeHook(char c, int i)
		{
			// Not needed
		}
		
	}
}
