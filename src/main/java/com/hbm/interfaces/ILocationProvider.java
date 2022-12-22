package com.hbm.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ILocationProvider
{
	public double posX();
	public double posY();
	public double posZ();
	public boolean hasWorld();
	public World getWorld();
	
	@Override
	public boolean equals(Object obj);
	@Override
	public int hashCode();
	@Override
	public String toString();
	
	public default Vec3 toVec3()
	{
		return Vec3.createVectorHelper(posX(), posY(), posZ());
	}
	public default double[] getCoordDouble()
	{
		return new double[] {posX(), posY(), posZ()};
	}
	public default int[] getCoordInt()
	{
		return new int[] {(int) posX(), (int) posY(), (int) posZ()};
	}
	/**
	 * Checks if the coordinates are invalid.
	 * @return If any of the coordinates are {@code NaN}.
	 */
	public default boolean isInvalid()
	{
		return Double.isNaN(posX()) || Double.isNaN(posY()) || Double.isNaN(posZ());
	}
	/** <b><i>Has prerequisite of having the world object set</b></i>
	 * @throws NoSuchElementException If the world is not set and is backed by an Optional
	 * @throws NullPointerException If the world is not set and is not backed by an Optional**/
	public default Block getBlock()
	{
		return getWorld().getBlock((int) posX(), (int) posY(), (int) posZ());
	}
	/** <b><i>Has prerequisite of having the world object set</b></i>
	 * @throws NoSuchElementException If the world is not set and is backed by an Optional
	 * @throws NullPointerException If the world is not set and is not backed by an Optional**/
	public default TileEntity getTE()
	{
		return getWorld().getTileEntity((int) posX(), (int) posY(), (int) posZ());
	}
	
	/** <b><i>Has prerequisite of having the world object set</b></i>
	 * @param b The block in question
	 * @return Whatever the {@link#World.setBlock()} method is supposed to return
	 * @throws NoSuchElementException If the world is not set and is backed by an Optional
	 * @throws NullPointerException If the world is not set and is not backed by an Optional**/
	public default boolean setBlock(Block block)
	{
		return getWorld().setBlock((int) posX(), (int) posY(), (int) posZ(), block);
	}
	
	public static Vec3 makeVector(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		return Vec3.createVectorHelper(loc2.posX() - loc1.posX(), loc2.posY() - loc1.posY(), loc2.posZ() - loc2.posZ());
	}
	
	public static Vec3 makeUnitVector(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		return makeVector(loc1, loc2).normalize();
	}
	
	public static double distance(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		final Vec3 vec = makeVector(loc1, loc2);
		return Math.sqrt((vec.xCoord * vec.xCoord) + (vec.yCoord * vec.yCoord) + (vec.zCoord + vec.zCoord));
	}
	
	public static double yaw(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		return Math.atan2(loc1.posZ() - loc2.posZ(), loc1.posX() - loc2.posX());
	}
	
	public static double pitch(@Nonnull ILocationProvider loc1, @Nonnull ILocationProvider loc2)
	{
		final Vec3 vec = makeVector(loc1, loc2);
		return Math.atan2(Math.sqrt(vec.zCoord * vec.zCoord + vec.xCoord * vec.xCoord), vec.yCoord) + Math.PI;
	}
	
	public static ILocationProvider wrap(Entity entity, boolean adjustForHeight)
	{
		return new DelegatedEntityLocation(entity, adjustForHeight);
	}
	
	public static List<ILocationProvider> wrapEntityCollection(Collection<? extends Entity> entities)
	{
		return wrapEntityCollection(entities, false);
	}
	
	public static List<ILocationProvider> wrapEntityCollection(Collection<? extends Entity> entities, boolean adjustForHeight)
	{
		final List<ILocationProvider> locations = new ArrayList<ILocationProvider>();
		for (Entity entity : entities)
			locations.add(wrap(entity, adjustForHeight));
		return locations;
	}
	
	public static ILocationProvider wrap(TileEntity tileEntity)
	{
		return new DelegatedTileEntityLocation(tileEntity);
	}
	
	public static List<ILocationProvider> wrapTECollection(Collection<? extends TileEntity> tileEntities)
	{
		final List<ILocationProvider> locations = new ArrayList<ILocationProvider>();
		for (TileEntity tileEntity : tileEntities)
			locations.add(wrap(tileEntity));
		return locations;
	}
	
	static class DelegatedEntityLocation implements ILocationProvider
	{
		private final Entity entity;
		private final boolean adjustForHeight;
		DelegatedEntityLocation(Entity entity, boolean adjustForHeight)
		{
			this.entity = entity;
			this.adjustForHeight = adjustForHeight;
		}
		
		@Override
		public double posX()
		{
			return entity.posX;
		}
		@Override
		public double posY()
		{
			return adjustForHeight ? entity.posY + entity.getEyeHeight() : entity.posY;
		}
		@Override
		public double posZ()
		{
			return entity.posZ;
		}
		@Override
		public boolean hasWorld()
		{
			return true;
		}
		@Override
		public World getWorld()
		{
			return entity.worldObj;
		}
		@Override
		public int hashCode()
		{
			return Objects.hash(adjustForHeight, entity);
		}
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!(obj instanceof DelegatedEntityLocation))
				return false;
			final DelegatedEntityLocation other = (DelegatedEntityLocation) obj;
			return adjustForHeight == other.adjustForHeight && Objects.equals(entity, other.entity);
		}
		@Override
		public String toString()
		{
			final StringBuilder builder = new StringBuilder();
			builder.append("DelegatedEntityLocation [entity=").append(entity).append(", adjustForHeight=")
					.append(adjustForHeight).append(", posX()=").append(posX()).append(", posY()=").append(posY())
					.append(", posZ()=").append(posZ()).append(", getWorld()=").append(getWorld()).append(']');
			return builder.toString();
		}
	}
	
	static class DelegatedTileEntityLocation implements ILocationProvider
	{
		private final TileEntity tileEntity;
		DelegatedTileEntityLocation(TileEntity tileEntity)
		{
			this.tileEntity = tileEntity;
		}
		
		@Override
		public double posX()
		{
			return tileEntity.xCoord;
		}
		@Override
		public double posY()
		{
			return tileEntity.yCoord;
		}
		@Override
		public double posZ()
		{
			return tileEntity.zCoord;
		}
		@Override
		public boolean hasWorld()
		{
			return tileEntity.hasWorldObj();
		}
		@Override
		public World getWorld()
		{
			return tileEntity.getWorldObj();
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(posX(), posY(), posZ());
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!(obj instanceof DelegatedTileEntityLocation))
				return false;
			final DelegatedTileEntityLocation other = (DelegatedTileEntityLocation) obj;
			return Objects.equals(tileEntity, other.tileEntity);
		}
		@Override
		public String toString()
		{
			final StringBuilder builder = new StringBuilder();
			builder.append("DelegatedTileEntityLocation [tileEntity=").append(tileEntity).append(", posX()=")
					.append(posX()).append(", posY()=").append(posY()).append(", posZ()=").append(posZ())
					.append(", getWorld()=").append(getWorld()).append(']');
			return builder.toString();
		}
		
	}
}
