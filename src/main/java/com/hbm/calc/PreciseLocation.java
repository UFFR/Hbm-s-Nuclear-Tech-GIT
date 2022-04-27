package com.hbm.calc;

import java.math.BigDecimal;
import java.math.BigInteger;
import static java.math.MathContext.DECIMAL128;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.CheckForNull;

import com.google.common.annotations.Beta;

import api.hbm.ISerializable;
import ch.obermuhlner.math.big.BigDecimalMath;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
@Beta
public class PreciseLocation implements Cloneable, Comparable<PreciseLocation>, Serializable, ISerializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -525812461028759983L;
	public final BigDecimal[] coord = new BigDecimal[3];
	public int dimID;
	public Optional<World> world = Optional.empty();
	static
	{
		ISerializable.register(PreciseLocation.class, new PreciseLocation(0, 0, 0));
	}
	public PreciseLocation(BigDecimal x, BigDecimal y, BigDecimal z)
	{
		coord[0] = x;
		coord[1] = y;
		coord[2] = z;
	}
	
	public PreciseLocation(BigDecimal[] coord)
	{
		this(coord[0], coord[1], coord[2]);
	}
	
	public PreciseLocation(double x, double y, double z)
	{
		coord[0] = BigDecimal.valueOf(x);
		coord[1] = BigDecimal.valueOf(y);
		coord[2] = BigDecimal.valueOf(z);
	}

	public PreciseLocation(long x, long y, long z)
	{
		coord[0] = BigDecimal.valueOf(x);
		coord[1] = BigDecimal.valueOf(y);
		coord[2] = BigDecimal.valueOf(z);
	}
	
	public PreciseLocation(double[] coord)
	{
		if (coord.length < 3)
			throw new IllegalArgumentException("Coordinate array length less than 3!");
		
		this.coord[0] = BigDecimal.valueOf(coord[0]);
		this.coord[1] = BigDecimal.valueOf(coord[1]);
		this.coord[2] = BigDecimal.valueOf(coord[2]);
	}
	
	public PreciseLocation(long[] coord)
	{
		if (coord.length < 3)
			throw new IllegalArgumentException("Coordinate array length less than 3!");
		
		this.coord[0] = BigDecimal.valueOf(coord[0]);
		this.coord[1] = BigDecimal.valueOf(coord[1]);
		this.coord[2] = BigDecimal.valueOf(coord[2]);
	}
	
	public PreciseLocation(Entity e)
	{
		coord[0] = BigDecimal.valueOf(e.posX);
		coord[1] = BigDecimal.valueOf(e.posY);
		coord[2] = BigDecimal.valueOf(e.posZ);
		world = Optional.of(e.worldObj);
		dimID = e.worldObj.provider.dimensionId;
	}
	
	public PreciseLocation(TileEntity te)
	{
		coord[0] = BigDecimal.valueOf(te.xCoord);
		coord[1] = BigDecimal.valueOf(te.yCoord);
		coord[2] = BigDecimal.valueOf(te.zCoord);
		world = Optional.of(te.getWorldObj());
		dimID = te.getWorldObj().provider.dimensionId;
	}
	
	public PreciseLocation(EasyLocation loc)
	{
		this(loc.getCoordDouble());
		if (loc.world.isPresent())
			setWorld(loc.world.get());
	}
	
	public PreciseLocation(Vec3 vector)
	{
		this(vector.xCoord, vector.yCoord, vector.zCoord);
	}
	
	public PreciseLocation(byte[] bytes)
	{
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		final byte[] xCoordBytes = new byte[buf.readInt()];
		buf.readBytes(xCoordBytes);
		final byte[] yCoordBytes = new byte[buf.readInt()];
		buf.readBytes(yCoordBytes);
		final byte[] zCoordBytes = new byte[buf.readInt()];
		buf.readBytes(zCoordBytes);
		dimID = buf.readInt();
		coord[0] = new BigDecimal(new String(xCoordBytes));
		coord[1] = new BigDecimal(new String(yCoordBytes));
		coord[2] = new BigDecimal(new String(zCoordBytes));
	}
	
	@Override
	public byte[] serialize()
	{
		ByteBuf buf = Unpooled.buffer();
		final byte[] xCoordBytes = coord[0].toString().getBytes();
		final byte[] yCoordBytes = coord[1].toString().getBytes();
		final byte[] zCoordBytes = coord[2].toString().getBytes();
		buf.writeInt(xCoordBytes.length);
		buf.writeBytes(xCoordBytes);
		buf.writeInt(yCoordBytes.length);
		buf.writeBytes(yCoordBytes);
		buf.writeInt(zCoordBytes.length);
		buf.writeBytes(zCoordBytes);
		buf.writeInt(dimID);
		return buf.array();
	}
	
	public static BigDecimal distance(PreciseLocation loc1, PreciseLocation loc2)
	{
		return BigDecimalMath.sqrt(BigDecimalMath.pow(loc2.coord[0].subtract(loc1.coord[0]), 2, DECIMAL128).add(BigDecimalMath.pow(loc2.coord[1].subtract(loc1.coord[1]), 2, DECIMAL128), DECIMAL128).add(BigDecimalMath.pow(loc2.coord[2].subtract(loc1.coord[2]), 2, DECIMAL128), DECIMAL128), DECIMAL128);
	}
	
	public BigDecimal distance(PreciseLocation loc)
	{
		return distance(this, loc);
	}
	
	public static BigDecimal yaw(PreciseLocation loc1, PreciseLocation loc2)
	{
		return BigDecimalMath.atan2(loc1.coord[2].subtract(loc2.coord[2]), loc1.coord[0].subtract(loc2.coord[0]), DECIMAL128);
	}
	
	public BigDecimal yaw(PreciseLocation loc)
	{
		return yaw(this, loc);
	}
	
	public static BigDecimal pitch(PreciseLocation loc1, PreciseLocation loc2)
	{
		BigDecimal[] vec = makeVector(loc1, loc2);
		return BigDecimalMath.atan2(BigDecimalMath.sqrt(vec[2].multiply(vec[2]).add(vec[0].multiply(vec[0])), DECIMAL128), vec[1], DECIMAL128).add(BigDecimalMath.pi(DECIMAL128));
	}
	
	public BigDecimal pitch(PreciseLocation loc)
	{
		return pitch(this, loc);
	}
	
	public static BigDecimal[] makeVector(PreciseLocation loc1, PreciseLocation loc2)
	{
		BigDecimal[] vec = new BigDecimal[3];
		vec[0] = loc2.coord[0].subtract(loc1.coord[0], DECIMAL128);
		vec[1] = loc2.coord[1].subtract(loc1.coord[1], DECIMAL128);
		vec[2] = loc2.coord[2].subtract(loc1.coord[2], DECIMAL128);
		return vec;
	}
	
	public PreciseLocation modifyCoord(double x, double y, double z)
	{
		coord[0].add(BigDecimal.valueOf(x));
		coord[1].add(BigDecimal.valueOf(y));
		coord[2].add(BigDecimal.valueOf(z));
		return this;
	}
	
	public PreciseLocation modifyCoord(BigDecimal x, BigDecimal y, BigDecimal z)
	{
		coord[0].add(x);
		coord[1].add(y);
		coord[2].add(z);
		return this;
	}
	/**Preserves both original PreciseLocation instances**/
	public static PreciseLocation addCoord(PreciseLocation loc1, PreciseLocation loc2)
	{
		return loc1.clone().addCoord(loc2);
	}
	
	public PreciseLocation addCoord(PreciseLocation loc)
	{
		coord[0].add(loc.coord[0]);
		coord[1].add(loc.coord[1]);
		coord[2].add(loc.coord[2]);
		return this;
	}
	
	/**Preserves both original PreciseLocation instances**/
	public static PreciseLocation subtractCoord(PreciseLocation loc1, PreciseLocation loc2)
	{
		return loc1.clone().subtractCoord(loc2);
	}
	
	public PreciseLocation subtractCoord(PreciseLocation loc)
	{
		coord[0].subtract(loc.coord[0]);
		coord[1].subtract(loc.coord[1]);
		coord[2].subtract(loc.coord[2]);
		return this;
	}
	
	public PreciseLocation setWorld(World world)
	{
		this.world = Optional.of(world);
		return this;
	}
	
	public BigDecimal[] getCoordBigDecimal()
	{
		return coord.clone();
	}
	
	public BigInteger[] getCoordBigInteger()
	{
		BigInteger[] coordOut = new BigInteger[3];
		coordOut[0] = coord[0].toBigInteger();
		coordOut[1] = coord[1].toBigInteger();
		coordOut[2] = coord[2].toBigInteger();
		return coordOut;
	}
	
	public double[] getCoordDouble()
	{
		double[] coordOut = new double[3];
		coordOut[0] = coord[0].doubleValue();
		coordOut[1] = coord[1].doubleValue();
		coordOut[2] = coord[2].doubleValue();
		return coordOut;
	}
	
	public long[] getCoordLong()
	{
		long[] coordOut = new long[3];
		coordOut[0] = coord[0].longValue();
		coordOut[1] = coord[1].longValue();
		coordOut[2] = coord[2].longValue();
		return coordOut;
	}
	
	@CheckForNull
	public static PreciseLocation constructGeneric(Object obj)
	{
		if (obj instanceof PreciseLocation)
			return (PreciseLocation) obj;
		else if (obj instanceof EasyLocation)
			return new PreciseLocation((EasyLocation) obj);
		else if (obj instanceof long[])
			return new PreciseLocation((long[]) obj);
		else if (obj instanceof double[])
			return new PreciseLocation((double[]) obj);
		else if (obj instanceof Vec3)
			return new PreciseLocation((Vec3) obj);
		else if (obj instanceof TileEntity)
			return new PreciseLocation((TileEntity) obj);
		else if (obj instanceof Entity)
			return new PreciseLocation((Entity) obj);
		else if (obj instanceof BigDecimal[])
			return new PreciseLocation((BigDecimal[]) obj);
		else
			return null;

	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		
		PreciseLocation test = constructGeneric(obj);
		if (test == null)
			return false;
		
		return coord.equals(test.coord) && world.equals(test.world) && dimID == test.dimID;
	}
	
	@Override
	public String toString()
	{
		return String.format("posX: %s; posY: %s; posZ: %s; dimID: %s, hasWorld: %s", coord[0], coord[1], coord[2], dimID, world.isPresent());
	}
	
	@Override
	public PreciseLocation clone()
	{
		try
		{
			return (PreciseLocation) super.clone();
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			PreciseLocation loc = new PreciseLocation(getCoordBigDecimal());
			if (world.isPresent())
				loc.setWorld(world.get());
			return loc;
		}
	}
	
	@Override
	public int hashCode()
	{
		return 7 * coord[0].hashCode() * coord[1].hashCode() * coord[2].hashCode() * 13 + world.hashCode();
	}

	@Override
	public int compareTo(PreciseLocation o)
	{
		PreciseLocation zero = new PreciseLocation(0, 0, 0);
		BigDecimal testDist = distance(o, zero);
		BigDecimal myDist = distance(this, zero);
		if (equals(o))
			return 0;
		else
			return myDist.compareTo(testDist);
	}

	@Override
	public ISerializable deserialize(byte[] bytes)
	{
		return new PreciseLocation(bytes);
	}
}
