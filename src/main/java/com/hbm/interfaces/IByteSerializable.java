package com.hbm.interfaces;

import com.hbm.main.DeserializationException;

import bytes.ByteSequence;

public interface IByteSerializable
{
	public void writeToBytes(ByteSequence buf);
	public void readFromBytes(ByteSequence buf) throws DeserializationException;
	
	public default byte[] writeToBytes()
	{
		final ByteSequence buf = ByteSequence.allocate(true);
		writeToBytes(buf);
		return buf.array();
	}
	
	public default void readFromBytes(byte[] bytes) throws DeserializationException
	{
		readFromBytes(ByteSequence.wrapReadOnly(bytes));
	}
}
