package com.hbm.util;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.hbm.main.DeserializationException;

import bytes.ByteSequence;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Beta
public class ItemStackSerialization
{

	public static byte[] serializeStack(@Nullable ItemStack stack)
	{
		final ByteSequence buf = ByteSequence.allocate(true);
		if (stack != null && stack.getItem() != null)
		{
			final byte[] registry = Item.itemRegistry.getNameForObject(stack.getItem()).getBytes();
			buf.writeInt(registry.length);
			buf.writeBytes(registry);
			buf.writeInt(stack.stackSize);
			buf.writeShort(stack.getItemDamage());
			if (stack.hasTagCompound())
			{
				final byte[] nbtBytes = NBTSerialization.serializeCompound(stack.getTagCompound());
				buf.writeByte(nbtBytes.length);
				buf.writeBytes(nbtBytes);
			} else
				buf.writeByte(0);
		}
		return buf.array();
	}
	
	public static byte[] serializeArray(ItemStack... stacks)
	{
		final ByteSequence buf = ByteSequence.allocate(true);
		buf.writeInt(stacks.length);
		
		for (ItemStack stack : stacks)
		{
			final byte[] stackBytes = serializeStack(stack);
			buf.writeInt(stackBytes.length);
			buf.writeBytes(stackBytes);
		}
		
		return buf.array();
	}
	
	public static ItemStack deserializeStack(byte[] bytes) throws DeserializationException
	{
		try
		{
			final ByteSequence buf = ByteSequence.wrapReadOnly(bytes);
			if (buf.getRemainingSize() == 0 || bytes.length == 0)
				return null;
			final byte[] nameBytes, nbtBytes;
			final String name;
			final Item item;
			final int stackSize, nbtSize;
			final short meta;
			final NBTTagCompound nbt;
			final ItemStack stack;
			
			nameBytes = new byte[buf.readInt()];
			buf.readBytes(nameBytes);
			name = new String(nameBytes);
			item = (Item) Item.itemRegistry.getObject(name);
			if (item == null)
				throw new DeserializationException("Item from name [" + name + "] is null!");
			
			stackSize = buf.readInt();
			meta = buf.readShort();
			nbtSize = buf.readInt();
			if (nbtSize > 0)
			{
				nbtBytes = new byte[nbtSize];
				buf.readBytes(nbtBytes);
				nbt = NBTSerialization.deserializeTagCompound(nbtBytes);
			} else
				nbt = null;
			stack = new ItemStack(item, stackSize, meta);
			stack.stackTagCompound = nbt;
			return stack;
		} catch (Exception e)
		{
			throw new DeserializationException(e);
		}
	}

	public static ItemStack[] deserializeItemStacks(byte[] bytes) throws DeserializationException
	{
		try
		{
			final ArrayList<ItemStack> stacks;
			final ByteSequence buf = ByteSequence.wrapReadOnly(bytes);
			final int count;
			
			count = buf.readInt();
			stacks = new ArrayList<>(count);
			
			for (int i = 0; i < count; i++)
			{
				final byte[] stackBytes = new byte[buf.readInt()];
				buf.readBytes(stackBytes);
				stacks.add(deserializeStack(stackBytes));
			}
			
			return stacks.toArray(new ItemStack[stacks.size()]);
		} catch (Exception e)
		{
			throw new DeserializationException(e);
		}
	}
	
}
