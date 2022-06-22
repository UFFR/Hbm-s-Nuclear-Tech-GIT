package com.hbm.tileentity.machine.computer;

import com.hbm.main.DeserializationException;

import api.hbm.serialization.ISerializable;

public class MessagePacket implements ISerializable<MessagePacket>
{
	public MessagePacket()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public MessagePacket deserialize(byte[] bytes) throws DeserializationException
	{
		return null;
	}

	@Override
	public byte[] serialize()
	{
		return new byte[0];
	}
}
