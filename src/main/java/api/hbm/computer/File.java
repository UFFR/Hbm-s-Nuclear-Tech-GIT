package api.hbm.computer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;

import com.hbm.lib.Library;

import api.hbm.Date;
import api.hbm.ISerializable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

public class File implements Cloneable, Serializable, ISerializable
{
	public enum FileType
	{
		UNFINISHED,
		CORRUPT,
		MISC,
		TEXT,
		RESEARCH,
		BLUEPRINT,
		PROGRAM;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1531170485327497840L;
	public static final String KEY_FILENAME = "FILE_NAME", KEY_FILE_TYPE = "FILE_TYPE", KEY_FILESIZE = "FILE_SIZE", KEY_HASH = "FILE_EXPECTED_HASH", KEY_CREATION_DATE = "FILE_CREATION_DATE", KEY_LAST_MODIFIED = "FILE_LAST_MODIFIED_DATE", KEY_TYPE = "FILE_TYPE";
	private NBTTagCompound data = new NBTTagCompound();
	private String filename;
	private long filesize;
	private int expectedHash;
	private Date created = new Date(), modified = new Date();
	private FileType filetype = FileType.MISC;
	static
	{
		ISerializable.register(File.class, new File());
	}
	private File()
	{}
	public File(String name, long size, Date creation)
	{
		filename = name;
		filesize = size;
		created = creation;
		modified = creation;
	}
	public File(String name, NBTTagCompound data, long size, Date creation)
	{
		this(name, size, creation);
		this.data = data;
	}
	public String getName()
	{
		return filename;
	}
	public NBTTagCompound pullNBT()
	{
		return (NBTTagCompound) data.copy();
	}
	public long getSize()
	{
		return filesize;
	}
	
	public void setFilesize(long filesize)
	{
		this.filesize = filesize;
	}
	
	public File setType(FileType type)
	{
		filetype = type;
		return this;
	}
	
	public FileType getType()
	{
		return filetype;
	}
	public File addData(String name, Object obj, @Nonnegative long size)
	{
//		this.data.setTag(name, nbt);
		NBTBase toSave = Library.autoNBT(obj);
		if (toSave == null)
			filetype = FileType.CORRUPT;
		else
			data.setTag(name, toSave);
		expectedHash = hashCode();
		filesize += size;
		return this;
	}
	public File updateModifiedDate(Date date)
	{
		if (date.compareTo(created) <= 0)
			modified = date;
		else
			filetype = FileType.CORRUPT;
		
		return this;
	}
	public Date getCreated()
	{
		return created;
	}
	public Date getLastModified()
	{
		return modified;
	}
	@Override
	public byte[] serialize()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(getName().length());
		buf.writeBytes(getName().getBytes());
		buf.writeByte(getType().ordinal());
		buf.writeInt(expectedHash);
		buf.writeLong(getSize());
		byte[] createdBytes = created.getData().toByteArray();
		byte[] modifiedBytes = modified.getData().toByteArray();
		buf.writeInt(createdBytes.length);
		buf.writeBytes(createdBytes);
		buf.writeInt(modifiedBytes.length);
		buf.writeBytes(modifiedBytes);
		if (data.hasNoTags())
		{
			buf.writeShort(0);
			return buf.array();
		}
		try
		{
			byte[] nbtBytes = CompressedStreamTools.compress(data);
			buf.writeShort(nbtBytes.length);
			buf.writeBytes(nbtBytes);
			return buf.array();
		} catch (IOException e)
		{
			e.printStackTrace();
			buf.writeShort(-1);
			return buf.array();
		}
	}
	
	public static File fromBytes(byte[] bytes)
	{
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		final short nameLength = buf.readShort();
		byte[] nameBytes = new byte[nameLength];
		buf.readBytes(nameBytes);
		final String name = new String(nameBytes); 
		final byte typeByte = buf.readByte();
		FileType type = FileType.values()[typeByte < FileType.values().length ? typeByte : FileType.CORRUPT.ordinal()];
		final int hash  = buf.readInt();
		final long size = buf.readLong();
		byte[] createdBytes = new byte[buf.readInt()];
		buf.readBytes(createdBytes);
		final BigInteger created = new BigInteger(createdBytes);
		byte[] modifiedBytes = new byte[buf.readInt()];
		buf.readBytes(modifiedBytes);
		final BigInteger modified = new BigInteger(modifiedBytes);
		final short dataLength = buf.readShort();
		if (dataLength < 0)
		{
			type = FileType.CORRUPT;
			return new File(name, size, new Date(created)).setType(type).updateModifiedDate(new Date(modified));
		}
		else if (dataLength == 0)
			return new File(name, size, new Date(created)).setType(type).updateModifiedDate(new Date(modified));
		else
		{
			try
			{
				byte[] nbtBytes = new byte[dataLength];
				NBTTagCompound internalData = CompressedStreamTools.func_152457_a(nbtBytes, new NBTSizeTracker(2097152L));
				File out = new File(name, internalData, size, new Date(created)).setType(type).updateModifiedDate(new Date(modified));
				if (out.hashCode() != hash)
					out.setType(FileType.CORRUPT);
				
				return out;
			} catch (IOException e)
			{
				e.printStackTrace();
				return new File(name, size, new Date(created)).updateModifiedDate(new Date(modified)).setType(FileType.CORRUPT);
			}
		}
	}
	
	@CheckForNull
	public static File fromNBT(NBTTagCompound nbt)
	{
		if (!(nbt.hasKey(KEY_FILENAME, 8) && nbt.hasKey(KEY_TYPE, 8) && nbt.hasKey(KEY_FILESIZE, 4) && nbt.hasKey(KEY_HASH, 3) && nbt.hasKey(KEY_CREATION_DATE, 7) && nbt.hasKey(KEY_LAST_MODIFIED, 7)))
			return null;
		else
			return new File(nbt.getString(KEY_FILENAME), nbt, nbt.getLong(KEY_FILESIZE), new Date(new BigInteger(nbt.getByteArray(KEY_CREATION_DATE)))).updateModifiedDate(new Date(new BigInteger(nbt.getByteArray(KEY_LAST_MODIFIED)))).setType(FileType.valueOf(nbt.getString(KEY_TYPE)));
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException
	{
		byte[] bytes = serialize();
		stream.write(bytes.length);
		stream.write(bytes);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException
	{
		byte[] bytes = new byte[stream.read()];
		stream.read(bytes);
		File f = fromBytes(bytes);
		filename = f.filename;
		filesize = f.filesize;
		filetype = f.expectedHash == f.hashCode() ? f.filetype : FileType.CORRUPT;
		created = f.created;
		modified = f.modified;
		data = f.data;
		expectedHash = f.expectedHash;
	}
	
	@Override
	public int hashCode()
	{
		return data.hashCode() * 13 * getType().hashCode() * 31 * getName().hashCode() * 7 * getCreated().hashCode() * 71 * getLastModified().hashCode();
	}
	
	@Override
	public File clone()
	{
		try
		{
			return (File) super.clone();
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return new File(KEY_FILENAME, data, getSize(), created).setType(getType()).updateModifiedDate(modified);
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(obj instanceof File))
			return false;
		if (obj == this)
			return true;
		File test = (File) obj;
		
		return getSize() == test.getSize() && test.data.equals(data) && getType() == test.getType();
	}
	
	@Override
	public String toString()
	{
		return String.format("[NAME=%s; TYPE=%s; SIZE=%s; CREATION_DATE=%s; LAST_MODIFIED_DATE=%s; DATA=%s; EXPECTED_HASH=%s; ACTUAL_HASH=%s]", getName(), getType(), getSize(), getCreated(), getLastModified(), data, data.getInteger(KEY_HASH), hashCode());
	}
	@Override
	public ISerializable deserialize(byte[] bytes)
	{
		return fromBytes(bytes);
	}
}
