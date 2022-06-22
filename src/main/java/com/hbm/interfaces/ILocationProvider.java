package com.hbm.interfaces;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ILocationProvider
{
	public double getX();
	public double getY();
	public double getZ();
	public boolean hasWorld();
	public World getWorld();
	public default double[] getCoordDouble()
	{
		return new double[] {getX(), getY(), getZ()};
	}
	public default int[] getCoordInt()
	{
		return new int[] {(int) getX(), (int) getY(), (int) getZ()};
	}
	
	public static Vec3 makeVector(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		return Vec3.createVectorHelper(loc2.getX() - loc1.getX(), loc2.getY() - loc1.getY(), loc2.getZ() - loc2.getZ());
	}
	
	public static double distance(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		final Vec3 vec = makeVector(loc1, loc2);
		return Math.sqrt((vec.xCoord * vec.xCoord) + (vec.yCoord * vec.yCoord) + (vec.zCoord + vec.zCoord));
	}
	
	public static double yaw(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		return Math.atan2(loc1.getZ() - loc2.getZ(), loc1.getX() - loc2.getX());
	}
	
	public static double pitch(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		final Vec3 vec = makeVector(loc1, loc2);
		return Math.atan2(Math.sqrt(vec.zCoord * vec.zCoord + vec.xCoord * vec.xCoord), vec.yCoord) + Math.PI;
	}
	
	public default boolean isInvalid()
	{
		return Double.isNaN(getX()) || Double.isNaN(getY()) || Double.isNaN(getZ());
	}
	
	public static ILocationProvider wrap(Entity entity)
	{
		return new ILocationProvider()
		{
			@Override public boolean hasWorld() {return true;}
			@Override public double getZ() {return entity.posZ;}
			@Override public double getY() {return entity.posY;}
			@Override public double getX() {return entity.posX;}
			@Override public World getWorld() {return entity.worldObj;}
		};
	}
	
	public static ILocationProvider wrap(TileEntity tileEntity)
	{
		return new ILocationProvider()
		{
			@Override public boolean hasWorld() {return tileEntity.hasWorldObj();}
			@Override public double getZ() {return tileEntity.zCoord;}
			@Override public double getY() {return tileEntity.yCoord;}
			@Override public double getX() {return tileEntity.xCoord;}
			@Override public World getWorld() {return tileEntity.getWorldObj();}
		};
	}
}
