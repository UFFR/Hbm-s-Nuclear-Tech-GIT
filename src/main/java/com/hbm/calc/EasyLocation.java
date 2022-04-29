package com.hbm.calc;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.CheckForNull;

import api.hbm.ISerializable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EasyLocation implements Cloneable, Comparable<EasyLocation>, Serializable, ISerializable<EasyLocation>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1791762616348589379L;
	public double posX, posY, posZ;
	public int dimID;
	public Optional<World> world = Optional.empty();
	static
	{
		ISerializable.register(EasyLocation.class, new EasyLocation(0, 0, 0));
	}
	public EasyLocation(double x, double y, double z)
	{
		posX = x;
		posY = y;
		posZ = z;
	}
	
	public EasyLocation(double[] coord)
	{
		if (coord.length < 3)
			throw new IllegalArgumentException("Coordinate array length less than 3!");
		posX = coord[0];
		posY = coord[1];
		posZ = coord[2];
	}
	
	public EasyLocation(int[] coord)
	{
		if (coord.length < 3)
			throw new IllegalArgumentException("Coordinate array length less than 3!");
		posX = coord[0];
		posY = coord[1];
		posZ = coord[2];
	}
	
	public EasyLocation(Entity e)
	{
		posX = e.posX;
		posY = e.posY;
		posZ = e.posZ;
		world = Optional.of(e.worldObj);
		dimID = e.worldObj.provider.dimensionId;
	}
	
	public EasyLocation(TileEntity te)
	{
		posX = te.xCoord;
		posY = te.yCoord;
		posZ = te.zCoord;
		world = Optional.of(te.getWorldObj());
		dimID = te.getWorldObj().provider.dimensionId;
	}
	
	public EasyLocation(Vec3 vector)
	{
		this(vector.xCoord, vector.yCoord, vector.zCoord);
	}
	
	public EasyLocation(byte[] bytes)
	{
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		dimID = buf.readInt();
	}
	
	@Override
	public byte[] serialize()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeInt(dimID);
		return buf.array();
	}
	
	public static double distance(EasyLocation loc1, EasyLocation loc2)
	{
		return Math.sqrt(Math.pow(loc2.posX - loc1.posX, 2) + Math.pow(loc2.posY - loc1.posY, 2) + Math.pow(loc2.posZ - loc1.posZ, 2));
	}
	
	public double distance(EasyLocation loc)
	{
		return distance(this, loc);
	}
	
	public static double yaw(EasyLocation loc1, EasyLocation loc2)
	{
		return Math.atan2(loc1.posZ - loc2.posZ, loc1.posX - loc2.posX);
	}
	
	public double yaw(EasyLocation loc)
	{
		return yaw(this, loc);
	}
	
	public static double pitch(EasyLocation loc1, EasyLocation loc2)
	{
		Vec3 vec = makeVector(loc1, loc2);
		return Math.atan2(Math.sqrt(vec.zCoord * vec.zCoord + vec.xCoord * vec.xCoord), vec.yCoord) + Math.PI;
	}
	
	public double pitch(EasyLocation loc)
	{
		return pitch(this, loc);
	}
	
	public static Vec3 makeVector(EasyLocation loc1, EasyLocation loc2)
	{
//		final double calcX = Math.sin(pitch(loc1, loc2)) * Math.cos(yaw(loc1, loc2)), calcY = Math.sin(pitch(loc1, loc2)) * Math.sin(yaw(loc1, loc2)), calcZ = Math.cos(yaw(loc1, loc2));
//		return Vec3.createVectorHelper(calcX, calcY, calcZ);
		return Vec3.createVectorHelper(loc2.posX - loc1.posX, loc2.posY - loc1.posY, loc2.posZ - loc1.posZ);
	}
	
	public Vec3 makeVector(EasyLocation loc)
	{
		return makeVector(this, loc);
	}
	
	public EasyLocation normalize()
	{
		posX = Math.round(posX);
		posY = Math.round(posY);
		posZ = Math.round(posZ);
		return this;
	}
	
	public boolean isInvalid()
	{
		return Double.isNaN(posX) || Double.isNaN(posY) || Double.isNaN(posZ);
	}
	@CheckForNull
	public static EasyLocation constructGeneric(Object obj)
	{
		if (obj instanceof EasyLocation)
			return (EasyLocation) obj;
		else if (obj instanceof int[])
			return new EasyLocation((int[]) obj);
		else if (obj instanceof double[])
			return new EasyLocation((double[]) obj);
		else if (obj instanceof Vec3)
			return new EasyLocation((Vec3) obj);
		else if (obj instanceof TileEntity)
			return new EasyLocation((TileEntity) obj);
		else if (obj instanceof Entity)
			return new EasyLocation((Entity) obj);
		else
			return null;

	}
	
	/**
	 * Also accepts the same objects the constructor can take
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		
		EasyLocation test = constructGeneric(obj);
		if (test == null)
			return false;
		
		return (posX == test.posX && posY == test.posY && posZ == test.posZ && dimID == test.dimID && world.equals(test.world));
	}
	/** Also sets the dimID **/
	public EasyLocation setWorld(World worldIn)
	{
		world = Optional.of(worldIn);
		dimID = worldIn.provider.dimensionId;
		return this;
	}
	
	public EasyLocation modifyCoord(int nX, int nY, int nZ)
	{
		posX += nX;
		posY += nY;
		posZ += nZ;
		return this;
	}
	
	public EasyLocation modifyCoord(double nX, double nY, double nZ)
	{
		posX += nX;
		posY += nY;
		posZ += nZ;
		return this;
	}
	/**Preserves both original EasyLocation instances**/
	public static EasyLocation addCoord(EasyLocation loc1, EasyLocation loc2)
	{
		return loc1.clone().addCoord(loc2);
	}
	
	public EasyLocation addCoord(EasyLocation loc)
	{
		posX += loc.posX;
		posY += loc.posY;
		posZ += loc.posZ;
		return this;
	}
	/**Preserves both original EasyLocation instances**/
	public static EasyLocation subtractCoord(EasyLocation loc1, EasyLocation loc2)
	{
		return loc1.clone().subtractCoord(loc2);
	}
	
	public EasyLocation subtractCoord(EasyLocation loc)
	{
		posX -= loc.posX;
		posY -= loc.posY;
		posZ -= loc.posZ;
		return this;
	}
	
	public double[] getCoordDouble()
	{
		double[] coord = new double[3];
		coord[0] = posX;
		coord[1] = posY;
		coord[2] = posZ;
		return coord;
	}
	
	public int[] getCoordInt()
	{
		int[] coord = new int[3];
		coord[0] = (int) posX;
		coord[1] = (int) posY;
		coord[2] = (int) posZ;
		return coord;
	}
	/** <b><i>Must</i></b> have world set!
	 * @param e The entity to spawn
	 * @return Whatever spawnEntityInWorld() returns**/
	public boolean spawnEntityAtCoord(Entity e)
	{
		return world.get().spawnEntityInWorld(e);
	}
	
	/** <b><i>Has prerequisite of having the world object set</b></i> **/
	public TileEntity getTEAtCoord()
	{
		return world.get().getTileEntity((int) posX, (int) posY, (int) posZ);
	}
	/** <b><i>Has prerequisite of having the world object set</b></i> **/
	public Block getBlockAtCoord()
	{
		return world.get().getBlock((int) posX, (int) posY, (int) posZ);
	}
	/** <b><i>Has prerequisite of having the world object set</b></i>
	 * @param b The block in question
	 * @return Whatever the {@link#World.setBlock()} method is supposed to return**/
	public boolean setBlockAtCoord(Block b)
	{
		return world.get().setBlock((int) posX, (int) posY, (int) posZ, b);
	}
	
	@Override
	public EasyLocation clone()
	{
		try
		{
			return ((EasyLocation) super.clone());
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			EasyLocation cloned = new EasyLocation(getCoordDouble());
			if (world.isPresent())
				cloned.setWorld(world.get());
			return cloned;
		}
	}
	
	@Override
	public String toString()
	{
		double[] coord = getCoordDouble();
		return String.format("posX: %s; posY: %s; posZ: %s; dimID: %s, hasWorld: %s", coord[0], coord[1], coord[2], dimID, world.isPresent());
	}
	
	@Override
	public int hashCode()
	{
		return (int) (dimID + posX * posY * posZ) * 7 * 31 + world.hashCode();
	}
	
	public Vec3 toVec3()
	{
		return Vec3.createVectorHelper(posX, posY, posZ);
	}

	@Override
	public int compareTo(EasyLocation o)
	{
		EasyLocation zero = new EasyLocation(0, 0, 0);
		double testDist = distance(o, zero);
		double myDist = distance(this, zero);
		if (equals(o) || testDist == myDist)
			return 0;
		return myDist < testDist ? 1 : -1;
	}

	@Override
	public EasyLocation deserialize(byte[] bytes)
	{
		return new EasyLocation(bytes);
	}
}
