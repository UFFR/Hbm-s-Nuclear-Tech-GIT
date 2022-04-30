package api.hbm.computer.filesystem;

import java.io.Serializable;

import com.hbm.main.DeserializationException;

import api.hbm.serialization.ISerializable;

public abstract class FileBase implements Serializable, Cloneable, ISerializable<FileBase>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5309270065067336455L;
	public FileBase()
	{
	}
	
//	public abstract FileMetadata generateMetadata();
	@Override
	public abstract FileBase deserialize(byte[] bytes) throws DeserializationException;
	@Override
	public abstract int hashCode();
	@Override
	public abstract boolean equals(Object obj);
	@Override
	public abstract FileBase clone();
}
