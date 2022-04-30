package api.hbm.computer.filesystem;

import java.util.Set;

import javax.annotation.CheckReturnValue;

public interface IDataStorageSystem extends IDataStorageBase
{
	public long getUsedCapacity();
	public boolean writeFile(FileBase file);
	public boolean deleteFile(String filename);
	public boolean overwriteFile(FileBase file);
	public FileMetadata getFileMetadata(String filename);
	@CheckReturnValue
	public FileBase readFile(String filename);
	@CheckReturnValue
	public String getFileHash(String filename);
	public Set<String> getFileList();
}
