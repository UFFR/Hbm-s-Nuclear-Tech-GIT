package api.hbm.computer.filesystem;

import java.util.Set;

public interface IDataStorageSystem extends IDataStorageBase
{
	public long getUsedCapacity();
	public boolean writeFile(FileBase file);
	public boolean deleteFile(String filename);
	public boolean overwriteFile(FileBase file);
	public Set<String> getFileList(); 
}
