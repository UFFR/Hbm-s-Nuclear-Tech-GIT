package com.hbm.interfaces;

import com.google.common.annotations.Beta;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Add to recipes to be researched by supercomputers</br>
 * Not finished yet
 * @author UFFR
 *
 */
@Beta
public interface IResearchableRecipe
{
//	public default NBTTagCompound convertToNBT()
//	{
//		NBTTagCompound nbt = new NBTTagCompound();
//		writeToNBT(nbt);
//		return nbt;
//	}
	String getKey();
//	void writeToNBT(NBTTagCompound nbt);
//	IResearchableRecipe readFromNBT(NBTTagCompound nbt);
}
