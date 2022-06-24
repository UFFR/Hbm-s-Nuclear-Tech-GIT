package com.hbm.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTBuilder
{
	private final NBTTagCompound nbt;
	public NBTBuilder()
	{
		nbt = new NBTTagCompound();
	}
	public NBTBuilder(NBTTagCompound nbt)
	{
		this.nbt = (NBTTagCompound) nbt.copy();
	}
	public NBTBuilder addTag(String key, NBTBase value)
	{
		nbt.setTag(key, value);
		return this;
	}
	public NBTBuilder addByte(String key, byte value)
	{
		nbt.setByte(key, value);
		return this;
	}
	public NBTBuilder addShort(String key, short value)
	{
		nbt.setShort(key, value);
		return this;
	}
	public NBTBuilder addInt(String key, int value)
	{
		nbt.setInteger(key, value);
		return this;
	}
	public NBTBuilder addLong(String key, long value)
	{
		nbt.setLong(key, value);
		return this;
	}
	public NBTBuilder addFloat(String key, float value)
	{
		nbt.setFloat(key, value);
		return this;
	}
	public NBTBuilder addDouble(String key, double value)
	{
		nbt.setDouble(key, value);
		return this;
	}
	public NBTBuilder addString(String key, String value)
	{
		nbt.setString(key, value);
		return this;
	}
	public NBTBuilder addByteArray(String key, byte[] value)
	{
		nbt.setByteArray(key, value);
		return this;
	}
	public NBTBuilder addIntArray(String key, int[] value)
	{
		nbt.setIntArray(key, value);
		return this;
	}
	public NBTBuilder addBoolean(String key, boolean value)
	{
		nbt.setBoolean(key, value);
		return this;
	}
	public NBTBase getTag(String key)
	{
		return nbt.getTag(key);
	}
	public boolean hasKey(String key)
	{
		return nbt.hasKey(key);
	}
	public boolean hasKey(String key, int type)
	{
		return nbt.hasKey(key, type);
	}
	public byte getByte(String key)
	{
		return nbt.getByte(key);
	}
	public short getShort(String key)
	{
		return nbt.getShort(key);
	}
	public int getInt(String key)
	{
		return nbt.getInteger(key);
	}
	public long getLong(String key)
	{
		return nbt.getLong(key);
	}
	public float getFloat(String key)
	{
		return nbt.getFloat(key);
	}
	public double getDouble(String key)
	{
		return nbt.getDouble(key);
	}
	public String getString(String key)
	{
		return nbt.getString(key);
	}
	public byte[] getByteArray(String key)
	{
		return nbt.getByteArray(key);
	}
	public int[] getIntArray(String key)
	{
		return nbt.getIntArray(key);
	}
	public NBTTagCompound getCompoundTag(String key)
	{
		return nbt.getCompoundTag(key);
	}
	public NBTTagList getTagList(String key, int type)
	{
		return nbt.getTagList(key, type);
	}
	public boolean getBoolean(String key)
	{
		return nbt.getBoolean(key);
	}
	public void removeTag(String key)
	{
		nbt.removeTag(key);
	}
	public boolean hasNoTags()
	{
		return nbt.hasNoTags();
	}
	public NBTTagCompound construct()
	{
		return nbt;
	}
	
	public Set<String> getKeys()
	{
		return nbt.func_150296_c();
	}
	
	public NBTBase[] getTags()
	{
		Set<String> keys = new HashSet<String>(getKeys());
		NBTBase[] tags = new NBTBase[keys.size()];
		int i = 0;
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			tags[i] = nbt.getTag(iterator.next());
			i++;
			iterator.remove();
		}
		return tags;
	}
	
	public Map<String, NBTBase> decompileNBT()
	{
		HashMap<String, NBTBase> map = new HashMap<String, NBTBase>();
		Set<String> keys = new HashSet<String>(getKeys());
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			map.put(iterator.next(), nbt.getTag(iterator.next()));
			iterator.remove();
		}
		return ImmutableMap.copyOf(map);
	}
	
	@Override
	public NBTBuilder clone()
	{
		return new NBTBuilder(nbt);
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		else if (obj instanceof NBTBuilder)
		{
			final NBTBuilder b = (NBTBuilder) obj;
			return b.construct().equals(nbt);
		}
		else if (obj instanceof NBTTagCompound)
		{
			final NBTTagCompound t = (NBTTagCompound) obj;
			return t.equals(nbt);
		}
		else
			return false;
	}
	@Override
	public int hashCode()
	{
		return nbt.hashCode();
	}
	@Override
	public String toString()
	{
		return nbt.toString();
	}
}
