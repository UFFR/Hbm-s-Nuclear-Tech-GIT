package api.hbm.computer.filesystem;

import java.util.Set;

import javax.annotation.CheckReturnValue;

import net.minecraft.item.ItemStack;

public interface IDataStorageItem
{
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
