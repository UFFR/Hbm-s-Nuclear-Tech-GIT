package com.hbm.calc;

import java.util.Iterator;

import com.google.common.annotations.Beta;

import api.hbm.serialization.ISerializable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
@Beta
public final class EasyLocationStep extends EasyLocation implements Iterable<EasyLocationStep>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7616340419778639505L;
	@FunctionalInterface
	public interface ConcludeIteration
	{
		public boolean hasConcluded(EasyLocation start, EasyLocation current, Vec3 vector);
	}
	/**Origin coordinates**/
	private final EasyLocation origin;
	protected final Vec3 vector;
	protected ConcludeIteration conclude = (start, current, vec) -> {return current.posY > 255 || current.posY < 0 || distanceFromOrigin() >= 1000;};
	static
	{
		ISerializable.register(EasyLocationStep.class, new EasyLocationStep(0, 0, 0, Vec3.createVectorHelper(0, 0, 0)));
	}
	public EasyLocationStep(double x, double y, double z, Vec3 vector)
	{
		super(x, y, z);
		origin = clone();
		this.vector = vector;
	}

	public EasyLocationStep(double[] coord, Vec3 vector)
	{
		super(coord);
		origin = clone();
		this.vector = vector;
	}

	public EasyLocationStep(int[] coord, Vec3 vector)
	{
		super(coord);
		origin = clone();
		this.vector = vector;
	}

	public EasyLocationStep(Entity e, Vec3 vector)
	{
		super(e);
		origin = clone();
		this.vector = vector;
	}

	public EasyLocationStep(TileEntity te, Vec3 vector)
	{
		super(te);
		origin = clone();
		this.vector = vector;
	}

	public EasyLocationStep(Vec3 vector, Vec3 directionalVector)
	{
		super(vector);
		origin = clone();
		this.vector = directionalVector;
	}
	
	@Override
	public byte[] serialize()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeBytes(origin.serialize());
		buf.writeBytes(super.serialize());
		buf.writeDouble(vector.xCoord);
		buf.writeDouble(vector.yCoord);
		buf.writeDouble(vector.zCoord);
		return buf.array();
	}
	
	public static EasyLocationStep fromBytes(byte[] bytes)
	{
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		EasyLocationStep out = new EasyLocationStep(buf.readDouble(), buf.readDouble(), buf.readDouble(), Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble()));
		out.posX = buf.readDouble();
		out.posY = buf.readDouble();
		out.posZ = buf.readDouble();
		return out;
	}
	
	@Override
	public EasyLocationStep deserialize(byte[] bytes)
	{
		return fromBytes(bytes);
	}
	
	public EasyLocationStep setConclusionCondition(ConcludeIteration conclude)
	{
		this.conclude = conclude;
		return this;
	}
	
	public EasyLocationStep stepAhead()
	{
		modifyCoord(vector.xCoord, vector.yCoord, vector.zCoord);
		return this;
	}
	
	public EasyLocationStep stepBehind()
	{
		modifyCoord(-vector.xCoord, -vector.yCoord, -vector.zCoord);
		return this;
	}

	public double distanceFromOrigin()
	{
		return distance(this, origin);
	}
	
	public EasyLocation getOrigin()
	{
		return origin.clone();
	}
	
	public EasyLocation getCurrentPos()
	{
		return clone();
	}
	
	@Override
	public Iterator<EasyLocationStep> iterator()
	{
		return new EasyLocIterator();
	}
	private class EasyLocIterator implements Iterator<EasyLocationStep>
	{
		public EasyLocIterator()
		{
		}

		@Override
		public boolean hasNext()
		{
			return !conclude.hasConcluded(getOrigin(), getCurrentPos(), vector);
		}

		@Override
		public EasyLocationStep next()
		{
			return stepAhead();
		}

	}

}
