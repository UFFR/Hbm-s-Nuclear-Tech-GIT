package api.hbm.computer.filesystem;

import java.util.Set;

import javax.annotation.CheckReturnValue;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IDataStorageItem
{
	public static final String KEY_USED = "USED_CAPACITY", PREFIX = "STORED_FILE_", H_SUFFIX = "_HASH";
	
	public default long getUsedCapacity(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setLong(KEY_USED, getMaxCapacity(stack));
		}
		
		return stack.stackTagCompound.getLong(KEY_USED);
	}
	
	public long getMaxCapacity(ItemStack stack);
	public long getReadSpeed(ItemStack stack);
	public long getWriteSpeed(ItemStack stack);
	public boolean writeFile(FileBase file, ItemStack stack);
	public boolean deleteFile(String filename, ItemStack stack);
	public boolean overwriteFile(FileBase file, ItemStack stack);
	@CheckReturnValue
	public FileBase readFile(String filename, ItemStack stack);
	@CheckReturnValue
	public String getFileHash(String filename, ItemStack stack);
	@CheckReturnValue
	public FileMetadata getFileMetadata(String filename, ItemStack stack);
	public Set<String> getFileList(ItemStack stack);
}
