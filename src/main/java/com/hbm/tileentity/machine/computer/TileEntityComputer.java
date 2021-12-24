package com.hbm.tileentity.machine.computer;

import org.lwjgl.input.Keyboard;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.TerminalUtil;
import com.hbm.util.TerminalUtil.Kernel;

import api.hbm.energy.IEnergyUser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComputer extends TileEntityMachineBase implements IEnergyUser, IControlReceiver
{
	public static final short maxPower = 25000;
	private short power = 0;
	private boolean isOn = false;
	public Kernel kernel;
	public TileEntityComputer()
	{
		super(0);
		kernel = new Kernel(10, 10);
	}
	
	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (!worldObj.isRemote)
		{
			if (isOn)
			{
				if (power >= 10)
					power -= 10;
				else
					isOn = false;
				
				// Standard program exit
				if ((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && Keyboard.isKeyDown(Keyboard.KEY_C))
				{
					kernel.print("^C");
					kernel.setCurrentProgram(null);
				}
				if (kernel.getCurrentProgram().isPresent())
				{
					kernel.getCurrentProgram().get().execute();
					if (kernel.getCurrentProgram().get().isExecutionComplete())
						kernel.setCurrentProgram(null);
				}
			}
			else
				kernel.setCurrentProgram(null);
		}

		NBTTagCompound data = new NBTTagCompound();
		data.setShort("power", power);
		data.setBoolean("isOn", isOn);
		kernel.writeToNBT(data);
		networkPack(data, 50);
	}
	
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		isOn = !isOn;
		if (isOn)
			kernel.print(TerminalUtil.startupLogo);
	}
	
	@Override
	public boolean hasPermission(EntityPlayer player)
	{
		return isUseableByPlayer(player);
	}
	
	@Override
	public void receiveControl(NBTTagCompound data)
	{
		// TODO Auto-generated method stub
		if (data.hasKey("command"))
			kernel.runCommand(data.getString("command"));
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getShort("power");
		isOn = nbt.getBoolean("isOn");
		kernel.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("power", power);
		nbt.setBoolean("isOn", isOn);
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

	public boolean isOn()
	{
		return isOn;
	}

	public void setOn(boolean isOn)
	{
		this.isOn = isOn;
	}

}
