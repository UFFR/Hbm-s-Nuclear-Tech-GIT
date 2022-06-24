package com.hbm.tileentity.turret;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.hbm.blocks.ModBlocks;
import com.hbm.calc.EasyLocation;
import com.hbm.config.BombConfig;
import com.hbm.entity.effect.EntityCloudSolinium;
import com.hbm.entity.effect.EntityCloudTom;
import com.hbm.entity.effect.EntityNukeCloudSmall;
import com.hbm.entity.logic.EntityNukeExplosionMK3;
import com.hbm.entity.logic.EntityNukeExplosionMK4;
import com.hbm.extprop.HbmLivingProps;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.lib.ModDamageSource;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.I18nUtil;

import api.hbm.block.ILaserable;
import api.hbm.energy.IEnergyUser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBishamonten extends TileEntityMachineBase implements ILaserable, IControlReceiver, IEnergyUser
{
	private long power = 0, buffer = 0, energyIn = 0, yield = 0;
	public static final long powerRate = 1000000000L, maxPower = 1000000000000L, maxBuffer = 100000000000L;
	/**Aka pitch**/
	private double elevation = 0.0F;
	/**Aka yaw**/
	private double direction = 0.0F;
	private int range = 50;
	private byte powPercentage = 0, timer = 0;
	private Mode currMode = Mode.DISABLED;
	public Optional<EntityLivingBase> currEntity = Optional.empty();
	private Optional<EasyLocation> targLoc = Optional.empty();
	private EasyLocation myLoc = null;
	private boolean bufferQueued = false, laserOn = false;
	private static final byte pitchRestriction = 50;
	private enum Mode
	{
		/** Off **/
		DISABLED,
		/** Target entities **/
		TARG_ENTITY,
		/** Target blocks or anything **/
		TARG_MISC,
		/** cover yourself in moonlight **/
		FLOOD;
	}
	public TileEntityBishamonten()
	{
		super(0);
	}

	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
	}
	
	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (myLoc == null)
			myLoc = new EasyLocation(this).modifyCoord(0, 2, 0);
		
		if (timer > 2)
			energyIn = 0;
		else
			timer++;
		
		targLoc = Optional.of(new EasyLocation(424, 100, 330));
		
		if (!worldObj.isRemote)
		{
			if (energyIn > 0)
				buffer = (buffer + energyIn > maxBuffer ? maxBuffer : buffer + energyIn);
			else if (!bufferQueued)
				buffer = 0;
			yield = maxBuffer * powPercentage / 100;
			if (currMode == Mode.TARG_ENTITY && currEntity.isPresent())
				targLoc = Optional.of(new EasyLocation(currEntity.get()));
			
			if (targLoc.isPresent())
			{
				elevation = Math.toDegrees(ILocationProvider.pitch(myLoc, targLoc.get())) - 90;
				direction = Math.toDegrees(ILocationProvider.yaw(myLoc, targLoc.get()));
			}
			
			if (bufferQueued && fillBuffer())
			{
				bufferQueued = false;
				purify();
			}
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setLong("buffer", buffer);
		data.setLong("yield", yield);
		data.setDouble("elevation", elevation);
		data.setDouble("direction", direction);
		data.setByte("mode", modeToByte(currMode));
		data.setByte("percentage", powPercentage);
		data.setBoolean("charging", bufferQueued);
		data.setBoolean("laserOn", laserOn);
		if (currEntity.isPresent())
			data.setInteger("entityID", currEntity.get().getEntityId());
		if (targLoc.isPresent())
			data.setIntArray("custCoord", targLoc.get().getCoordInt());
		networkPack(data, 100);
	}
	
	private void laser(EasyLocation target)
	{
		if (!target.isInvalid() && !myLoc.equals(target))
		{
			final Set<EntityLivingBase> entityBuffer = new HashSet<>();
			final Set<EasyLocation> blockBuffer = new LinkedHashSet<>();
			final double distanceToTarg = ILocationProvider.distance(myLoc, target);
			final Vec3 vector = ILocationProvider.makeVector(myLoc, target);
			vector.xCoord /= distanceToTarg;
			vector.yCoord /= distanceToTarg;
			vector.zCoord /= distanceToTarg;
			final EasyLocation checkCoord = myLoc.clone();
			double distance = 0;
			do
			{
				checkCoord.modifyCoord(vector.xCoord, vector.yCoord, vector.zCoord);
				entityBuffer.addAll(worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(checkCoord.posX - 5, checkCoord.posY - 5, checkCoord.posZ - 5, checkCoord.posX + 5, checkCoord.posY + 5, checkCoord.posZ + 5)));
				if (checkCoord.getBlock() == Blocks.air)
					blockBuffer.add(checkCoord.clone().round());
				distance = ILocationProvider.distance(checkCoord, myLoc);
			} while (distance <= range && checkCoord.posY < 255 && checkCoord.posY > 0);
			for (EntityLivingBase entity : entityBuffer)
			{
				HbmLivingProps.setDigamma(entity, 9.9f);
				HbmLivingProps.setRadiation(entity, 999.9f);
				if (!entity.attackEntityFrom(ModDamageSource.light, 1000))
					entity.setHealth(0);
				entity.onDeath(ModDamageSource.light);
			}
			for (EasyLocation blockPos : blockBuffer)
				blockPos.setBlock(ModBlocks.block_australium);
		}
	}
	
	private void purify()
	{
		// TODO
		switch (currMode)
		{
		case TARG_ENTITY:
		case TARG_MISC:
			laser(targLoc.get());
			break;
		case FLOOD:
			int fYield = (int) (yield / 1000000000) * 5;
			EntityNukeExplosionMK3 flood = new EntityNukeExplosionMK3(getWorldObj());
			flood.posX = xCoord;
			flood.posY = yCoord + 2;
			flood.posZ = zCoord;
			flood.destructionRange = fYield;//Would be a wee bit large without this clamped
			flood.speed = BombConfig.blastSpeed;
			flood.coefficient = 100;
			flood.coefficient2 = 200;
			flood.waste = false;
			flood.extType = 2;
			worldObj.spawnEntityInWorld(flood);
			
			EntityCloudTom effect = new EntityCloudTom(getWorldObj(), fYield);
			effect.posX = xCoord;
			effect.posY = yCoord + 2;
			effect.posZ = zCoord;
			worldObj.spawnEntityInWorld(effect);
			break;
		default:
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
			break;
		}
		buffer = 0;
	}
	
	public String getEntityName()
	{
		return currEntity.isPresent() ? currEntity.get().getCommandSenderName() : I18nUtil.resolveKey("twr.target.none");
	}
	
	public String getEntityCoordinates()
	{
		if (currEntity.isPresent() && currMode == Mode.TARG_ENTITY)
		{
			double[] coord = new double[3];
			coord[0] = currEntity.get().posX;
			coord[1] = currEntity.get().posY;
			coord[2] = currEntity.get().posZ;
			return Arrays.toString(coord);
		}
		else if (targLoc.isPresent() && currMode == Mode.TARG_MISC)
			return Arrays.toString(targLoc.get().getCoordInt());
		else
			return "N/A";
	}
	
	private static Mode byteToMode(byte in)
	{
		switch (in)
		{
		case 1:
			return Mode.TARG_ENTITY;
		case 2:
			return Mode.TARG_MISC;
		case 3:
			return Mode.FLOOD;
		default:
			return Mode.DISABLED;
		}
	}
	
	private static byte modeToByte(Mode in)
	{
		switch (in)
		{
		case DISABLED:
			return 0;
		case TARG_ENTITY:
			return 1;
		case TARG_MISC:
			return 2;
		case FLOOD:
			return 3;
		default:
			return 0;
		}
	}
	
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "gui.button.press", 1.0F, 1.0F);
		if (value == 2)
		{
			powPercentage = (byte) (meta == -1 ? powPercentage : meta);
			return;
		}
		if (value == 0 && !bufferQueued)
		{
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:alarm.defconstage", 1.0F, 1.0F);
			currMode = byteToMode((byte) meta);
		}
		else if (bufferQueued)
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
		else
		{
			if (maxBuffer * (powPercentage / 100) > power || currMode == Mode.DISABLED || powPercentage == 0 || bufferQueued)
			{
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
				return;
			}
			else
				bufferQueued = true;
		}
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getLong("power");
		buffer = nbt.getLong("buffer");
		yield = nbt.getLong("yield");
		elevation = nbt.getDouble("elevation");
		direction = nbt.getDouble("direction");
		currMode = byteToMode(nbt.getByte("mode"));
		powPercentage = nbt.getByte("percentage");
		bufferQueued = nbt.getBoolean("charging");
		laserOn = nbt.getBoolean("laserOn");
		if (nbt.hasKey("entityID"))
			currEntity = Optional.ofNullable((EntityLivingBase) worldObj.getEntityByID(nbt.getInteger("entityID")));
		if (nbt.hasKey("custCoord"))
			targLoc = Optional.of(new EasyLocation(nbt.getIntArray("custCoord")));
	}
	/**
	 * Charge up sequence
	 * @return If it has finished
	 */
	private boolean fillBuffer()
	{
		if (power >= powerRate && buffer + powerRate <= yield)
		{
			power -= powerRate;
			buffer += powerRate;
		}
		return buffer >= yield;
	}
	
	public boolean outOfBounds()
	{
//		direction = MathHelper.clamp_float(direction, 0F, 360F);
//		elevation = MathHelper.clamp_float(elevation, -50F, 50F);
		return (elevation <= pitchRestriction || elevation >= -pitchRestriction);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setDouble("elevation", elevation);
		nbt.setDouble("direction", direction);
		nbt.setByte("mode", modeToByte(currMode));
		nbt.setByte("percentage", powPercentage);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		elevation = nbt.getDouble("elevation");
		direction = nbt.getDouble("direction");
		currMode = byteToMode(nbt.getByte("mode"));
		powPercentage = nbt.getByte("percentage");
	}
	
	
	public long getBuffer()
	{
		return buffer;
	}

	@Override
	public String getName()
	{
		return "container.turretLunarOni";
	}
	
	public double getDirection()
	{
		return direction;
	}
	public double getElevation()
	{
		return elevation;
	}
	public byte getLevel()
	{
		return powPercentage;
	}
	public boolean isCharging()
	{
		return bufferQueued;
	}

	@Override
	public void addEnergy(World world, int x, int y, int z, long energy, ForgeDirection dir)
	{
		energyIn = energy;
		timer = 0;
	}

	@Override
	public boolean hasPermission(EntityPlayer player)
	{
		return isUseableByPlayer(player);
	}

	@Override
	public void receiveControl(NBTTagCompound data)
	{
		System.out.println("Got data");
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "gui.button.press", 1.0F, 1.0F);
//		if (data.hasKey("input"))
//			powPercentage = ((data.getByte("input") == -1) ? powPercentage : data.getByte("input"));
		if (data.hasKey("coord"))
			targLoc = Optional.of(new EasyLocation(data.getIntArray("coord")));
	}
	
	private void makeExplosion(EasyLocation loc, int yield)
	{
		if (yield >= 100)
		{
			final EntityNukeExplosionMK3 field = new EntityNukeExplosionMK3(getWorldObj());
			field.posX = loc.posX;
			field.posY = loc.posY;
			field.posZ = loc.posZ;
			field.speed = 25;
			field.coefficient = 1.0F;
			field.waste = false;
			worldObj.spawnEntityInWorld(field);
			final EntityCloudSolinium sphere = new EntityCloudSolinium(getWorldObj(), yield);
			sphere.posX = loc.posX;
			sphere.posY = loc.posY;
			sphere.posZ = loc.posZ;
			worldObj.spawnEntityInWorld(sphere);
		}
		else
		{
			worldObj.spawnEntityInWorld(EntityNukeExplosionMK4.statFacNoRad(getWorldObj(), yield, loc.posX, loc.posY, loc.posZ));
			
			EntityNukeCloudSmall cloud = new EntityNukeCloudSmall(getWorldObj(), yield * 4, yield * 0.005F);
			cloud.posX = loc.posX;
			cloud.posY = loc.posY;
			cloud.posZ = loc.posZ;
			worldObj.spawnEntityInWorld(cloud);
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
		this.power = power;
	}
	
	public int[] getModeData()
	{
		int lightOffset;
		int buttonOffset;
		switch(currMode)
		{
		case DISABLED:
			lightOffset = 22;
			buttonOffset = 18;
			break;
		case TARG_ENTITY:
			lightOffset = 49;
			buttonOffset = 45;
			break;
		case TARG_MISC:
			lightOffset = 76;
			buttonOffset = 72;
			break;
		case FLOOD:
			lightOffset = 103;
			buttonOffset = 99;
			break;
		default:
			lightOffset = 0;
			buttonOffset = 0;
			break;
		}
		return new int[] {lightOffset, buttonOffset, currMode.ordinal()};
	}
}
