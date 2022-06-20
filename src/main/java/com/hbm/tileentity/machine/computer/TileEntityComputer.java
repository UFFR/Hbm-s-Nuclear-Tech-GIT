package com.hbm.tileentity.machine.computer;

import java.awt.Shape;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.items.ItemEnums.EnumCPU;
import com.hbm.lib.Library;
import com.hbm.saveddata.TimeSavedData;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BobMathUtil;

import api.hbm.computer.*;
import api.hbm.computer.File.FileType;
import api.hbm.energy.IEnergyUser;
import api.hbm.internet.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityComputer extends TileEntityMachineBase implements IEnergyUser, IControlReceiver, ITerminal, IDataReceiver, IDataStorageSystem
{
	public static final short maxPower = 30000;
	private short power = 0;
	private long uptime = 0;
	private UUID id = UUID.randomUUID();
	private String friendlyName = "";
	private final ArrayList<String> consoleText = new ArrayList<String>();
	private final ArrayList<String> prevCommands = new ArrayList<String>();
	private final ArrayList<FloatingText> floatingText = new ArrayList<FloatingText>();
	private final ArrayList<Shape> shapes = new ArrayList<Shape>();
	private final HashMap<String, Class<? extends Program>> installedPrograms = new HashMap<String, Class<? extends Program>>();
	private Optional<Program> currentProgram = Optional.empty();
	private Optional<File> activeFile = Optional.empty();
	private boolean[] statuses = new boolean[7];
	private BigInteger intClock = BigInteger.ZERO;
	private HashMap<String, File> fileManager = new HashMap<String, File>();
	public TileEntityComputer()
	{
		super(0);
	}
	
	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (!worldObj.isRemote)
		{
			statuses[1] = currentProgram.isPresent();
			if (statuses[0])
			{
				if (power >= 10)
					power -= 10;
				else
					statuses[0] = false;
				if (statuses[6])
					return;
				
				if (activeFile.isPresent())
				{
					if (writeChunk(activeFile.get().getName(), Math.min(getWriteSpeed(), activeFile.get().getSize())))
						activeFile = Optional.empty();
					else
						activeFile.get().setFilesize(-getWriteSpeed());
				}
				
				uptime++;
				try
				{
					onTick();
				}
				catch (Exception e)
				{
					handleReturn(ReturnCode.FATAL, e);
					setFault(true);
				}
			}
			else
				onShutdown();
		}

		NBTTagCompound data = new NBTTagCompound();
		data.setShort("power", power);
		data.setByteArray("statuses", Library.boolToByte(getStatuses()));
		saveTerminalToNBT(data);
		networkPack(data, 50);
	}
	
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		if (value == 0)
		{
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "gui.button.press", 1.0F, 1.0F);
			statuses[0] = !statuses[0];
			if (statuses[0])
				onBoot();
			else
				onShutdown();
		}
		else if (getCurrentProgram().isPresent() && !getCurrentProgram().get().requestingConsoleInput())
			getCurrentProgram().get().keyTypeHook((char) value, meta);
	}
	
	@Override
	public boolean hasPermission(EntityPlayer player)
	{
		return isUseableByPlayer(player);
	}
	
	@Override
	public void runCommand(String cmd)
	{
		ITerminal.super.runCommand(cmd);
		
		TerminalCommand parsed = new TerminalCommand(cmd);
		
		switch (parsed.getCommand())
		{
		case "gettime":
			print("Current time:", Arrays.toString(TimeSavedData.decompress(getInternalDate())), intClock.toString()); break;
		case "files":
			print(getFileList().toString()); break;
		case "netname":
			if (parsed.getArgs().length == 0)
				print(friendlyName);
			else
				friendlyName = parsed.getArgsUnsplit();
			break;
		default:
			ITerminal.stockCommandHandler(this, cmd); break;
		}
	}
	
	@Override
	public void receiveControl(NBTTagCompound data)
	{
		// TODO Auto-generated method stub
		String toWrite = new String();
		switch (data.getString("TYPE"))
		{
		case "COMMAND":
			toWrite = data.getString("command");
			try
			{
				if (currentProgram.isPresent() && currentProgram.get().requestingConsoleInput())
					currentProgram.get().acceptConsoleInputHook(toWrite);
				else
					runCommand(toWrite);
			}
			catch (Exception e)
			{
				handleReturn(ReturnCode.UNKNOWN, e);
			}
//			kernel.runCommand(toWrite);
//			consoleText.add(toWrite);
			break;
		case "CONSOLE_TEXT":
			consoleText.add(data.getString("text"));
			break;
		case "CLEAR":
			consoleText.clear();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getShort("power");
		statuses = Library.byteToBool(nbt.getByteArray("statuses"), false);
		loadTerminalFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("power", power);
		nbt.setString("computerID", id.toString());
		nbt.setByteArray("statuses", Library.boolToByte(getStatuses()));
		nbt.setString("name", friendlyName);
		nbt.setByteArray("clock", intClock.toByteArray());
		ByteBuf buf = Unpooled.buffer();
		for (File file : fileManager.values())
		{
			buf.writeBoolean(true);
			byte[] bytes = file.toBytes();
			buf.writeInt(bytes.length);
			buf.writeBytes(bytes);
		}
		buf.writeBoolean(false);
		nbt.setByteArray("fileStorage", buf.array());
		saveTerminalToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getShort("power");
		statuses = Library.byteToBool(nbt.getByteArray("statuses"), false);
		friendlyName = nbt.getString("name");
		intClock = new BigInteger(nbt.getByteArray("clock"));
		ByteBuf buf = Unpooled.copiedBuffer(nbt.getByteArray("fileStorage"));
		while (buf.capacity() > 0 && buf.readBoolean())
		{
			byte[] bytes = new byte[buf.readInt()];
			File file = File.fromBytes(bytes);
			fileManager.put(file.getName(), file);
		}
		
		loadTerminalFromNBT(nbt);
		try
		{
			id = UUID.fromString(nbt.getString("computerID"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			id = UUID.randomUUID();
		}
	}
	
	@Override
	public long getPower()
	{
		return power;
	}

	@Override
	public long getMaxPower()
	{
		return maxPower;
	}

	@Override
	public void setPower(long power)
	{
		this.power = (short) power;
	}

	@Override
	public String getName()
	{
		return "container.computer";
	}

	public boolean[] getStatuses()
	{
		return statuses.clone();
	}

	public void setOn(boolean isOn)
	{
		this.statuses[0] = isOn;
	}

	@Override
	public long upTime()
	{
		return uptime;
	}
	
	@Override
	public MessageResponse recieveData(MessagePacket data)
	{
		return MessageResponse.LOCKED;
	}

	@Override
	public boolean canDataConnect(ForgeDirection dir)
	{
		return dir == ForgeDirection.SOUTH;
	}

	@Override
	public long getStorage()
	{
		int used = 0;
		
		for (File file : fileManager.values())
			used += file.getSize();
		
		return used;
	}

	@Override
	public long getMaxCapacity()
	{
		return 5 *BobMathUtil.MB;
	}

	@Override
	public UUID getUUID()
	{
		return id;
	}
	
	@Override
	public String getFriendlyName()
	{
		return friendlyName;
	}

	@Override
	public List<String> getPreviousCommands()
	{
		return ImmutableList.copyOf(prevCommands);
	}

	@Override
	public void addToPreviousCommands(String cmd)
	{
		if (!prevCommands.get(prevCommands.size() - 1).equals(cmd))
			prevCommands.add(cmd);
	}
	
	@Override
	public void addAllToPrevious(Collection<String> cmds)
	{
		prevCommands.addAll(cmds);
	}

	@Override
	public List<String> getConsoleText()
	{
		return ImmutableList.copyOf(consoleText);
	}
	
	@Override
	public void print(String s)
	{
		consoleText.add(s);
	}

	@Override
	public List<FloatingText> getFloatingText()
	{
		return ImmutableList.copyOf(floatingText);
	}

	@Override
	public void print(FloatingText txt)
	{
		floatingText.add(txt);
	}
	
	@Override
	public List<Shape> getShapes()
	{
		return ImmutableList.copyOf(shapes);
	}

	@Override
	public void draw(Shape shape)
	{
		shapes.add(shape);
	}

	@Override
	public void clearScreen(int i)
	{
		switch (i)
		{
		case 0:
		default:
			consoleText.clear();
			break;
		case 1:
			floatingText.clear();
			break;
		case 2:
			shapes.clear();
			break;
		case -1:
			consoleText.clear();
			floatingText.clear();
			shapes.clear();
			break;
		}
	}

	@Override
	public Map<String, Class<? extends Program>> getPrograms()
	{
		return ImmutableMap.copyOf(installedPrograms);
	}

	@Override
	public Optional<Program> getCurrentProgram()
	{
		return currentProgram;
	}

	@Override
	public void setCurrentProgram(Program program)
	{
		currentProgram = Optional.ofNullable(program);
	}

	@Override
	public boolean hasFault()
	{
		return statuses[6];
	}

	@Override
	public void setFault(boolean fault)
	{
		statuses[6] = fault;
	}

	@Override
	public void onShutdown()
	{
		if (getCurrentProgram().isPresent())
			handleReturn(getCurrentProgram().get().onProcessKill());
		setCurrentProgram(null);
		uptime = 0;
	}

	@Override
	public int getUpgradeCap()
	{
		return 100;
	}

	@Override
	public long getCPU()
	{
		return EnumCPU.ALUMINIUM.power;
	}
	
	@Override
	public long getUsedCPU()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getRAM()
	{
		return 512000;
	}

	@Override
	public long getUsedRAM()
	{
		// TODO Auto-generated method stub
		long used = 0;
		if (currentProgram.isPresent())
			used += currentProgram.get().getRAM();
		if (activeFile.isPresent())
			used += activeFile.get().getSize();
		return used;
	}
	
	@Override
	public void incrementAge()
	{
		intClock = intClock.add(BigInteger.ONE);
	}

	@Override
	public void notifyTimeUpdated(BigInteger newTime)
	{
		intClock = newTime;
	}

	@Override
	public BigInteger getInternalDate()
	{
		return intClock;
	}

	@Override
	public long getReadSpeed()
	{
		return 100;
	}

	@Override
	public long getWriteSpeed()
	{
		return 80;
	}

	@Override
	public boolean writeChunk(String filename, long size)
	{
		if (activeFile.isPresent() && activeFile.get().getName().equals(filename))
		{
			if (fileManager.containsKey(filename))
				fileManager.get(filename).setFilesize(size);
			else
				fileManager.put(filename, new File(filename, size, getInternalDate()).setType(FileType.UNFINISHED));
			
			long writtenFile = fileManager.get(filename).getSize(), pendingFile = activeFile.get().getSize();
			setFault(writtenFile > pendingFile);
			return writtenFile >= pendingFile;
		}
		return true;
	}

	@Override
	public boolean readChunk(String filename)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eraseChunk(String filename)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean writeFile(File file)
	{
		activeFile = Optional.of(file);
		return true;
	}

	@Override
	public boolean deleteFile(String filename)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean overwriteFile(File file)
	{
		activeFile = Optional.of(file);
		return true;
	}

	@Override
	public File getFile(String filename)
	{
		return fileManager.get(filename);
	}

	@Override
	public byte[] extractFiles()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(fileManager.size());
		
		fileManager.values().forEach(f ->
		{
			byte[] bytes = f.toBytes();
			buf.writeInt(bytes.length);
			buf.writeBytes(bytes);
		});
		
		return buf.array();
	}

	@Override
	public Set<String> getFileList()
	{
		return ImmutableSet.copyOf(fileManager.keySet());
	}
}
