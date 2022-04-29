package api.hbm.computer.filesystem;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.hbm.main.DeserializationException;

import api.hbm.Date;
import api.hbm.ISerializable;

public abstract class FileBase implements Serializable, Cloneable, ISerializable<FileBase>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5309270065067336455L;
	protected String filename;
	protected long filesize;
	protected int expectedHash;
	@Nonnull
	protected Date dateCreated = new Date(), dateModified = new Date();
	private boolean isCorrupted = false;
	public FileBase()
	{
	}
	
	public static byte[] serializeMetadata(FileBase file)
	{
		return new FileMetadata(file).serialize();
	}
	
	public static FileMetadata deserializeMetadata(byte[] bytes) throws DeserializationException
	{
		return FileMetadata.deserializer.deserialize(bytes);
	}
	
	public final String getFilename()
	{
		return filename;
	}
	
	public final int getExpectedHash()
	{
		return expectedHash;
	}
	
	public final long getFileSize()
	{
		return filesize;
	}
	
	public final Date getCreationDate()
	{
		return dateCreated.clone();
	}
	
	public final Date getLastModifiedDate()
	{
		return dateModified.clone();
	}

	public final boolean getIsCorrupted()
	{
		return isCorrupted;
	}
	
	public final FileBase setCorrupted()
	{
		isCorrupted = true;
		return this;
	}
	
	@Override
	public abstract FileBase deserialize(byte[] bytes) throws DeserializationException;
	@Override
	public abstract int hashCode();
	@Override
	public abstract boolean equals(Object obj);
}
