package com.hbm.packet;

import com.hbm.calc.EasyLocation;

import api.hbm.internet.MessagePacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
// TODO
public class NBTInternetDataPacket implements IMessage
{
	private EasyLocation loc;
	private MessagePacket msg;
	public NBTInternetDataPacket(MessagePacket msg, EasyLocation loc)
	{
		this.loc = loc;
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		int[] coord = new int[3];
		coord[0] = buf.readInt();
		coord[1] = buf.readInt();
		coord[2] = buf.readInt();
		byte[] msgBytes = new byte[buf.readInt()];
		buf.readBytes(msgBytes);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		int[] coord = loc.getCoordInt();
		buf.writeInt(coord[0]);
		buf.writeInt(coord[1]);
		buf.writeInt(coord[2]);
	}

	
	public class Handler implements IMessageHandler<NBTInternetDataPacket, IMessage>
	{

		@Override
		public IMessage onMessage(NBTInternetDataPacket message, MessageContext ctx)
		{
//			loc.setWorld(Minecraft.getMinecraft().theWorld);
//			TileEntity te = loc.getTEAtCoord();
			
//			if (te != null && te instanceof IDataConnector)
//				((IDataConnector) te).recieveData(message.msg);
			
			return null;
		}

	}

}
