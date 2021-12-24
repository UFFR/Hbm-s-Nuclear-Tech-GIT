package api.hbm.internet;

import java.util.Collection;

import javax.annotation.CheckForNull;

import com.hbm.util.TerminalUtil.File;

import net.minecraft.nbt.NBTTagCompound;
/**
 * Interface for data storage
 * @author UFFR
 *
 */
public interface IDataUser
{
	public static final int KB = 1000;
	public static final int MB = 1000000;
	public static final int GB = 1000000000;
	public static final long TB = 100000000000L;
	public static final long PB = 100000000000000L;
	public static final long ZB = 100000000000000000L;
	public static final String KEY_NBT = "FILE_SYSTEM";

	public long getMaxCapacity();
	public long getUsedSpace();
	/**
	 * Add files to the file manager
	 * @param file The file to add
	 * @return If it was successful
	 */
	public boolean writeFile(File file);
	/**
	 * Delete a file from the file manager
	 * @param filename Name of the file to delete
	 * @return If it was successful
	 */
	public boolean deleteFile(String filename);
	public boolean overwriteFile(File file);
	@CheckForNull
	public File getFile(String filename);
	public NBTTagCompound extractNBT();
	public Collection<File> getFileList();
	
	public default void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag(KEY_NBT, extractNBT());
	}

}
