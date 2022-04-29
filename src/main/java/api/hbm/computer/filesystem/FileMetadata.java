package api.hbm.computer.filesystem;

import java.io.Serializable;
import java.math.BigInteger;

import javax.annotation.concurrent.Immutable;

import com.hbm.main.DeserializationException;

import api.hbm.Deserializer;
import api.hbm.ISerializable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@Immutable
@org.apache.http.annotation.Immutable
public final class FileMetadata implements Serializable, ISerializable<FileMetadata>
{
	public static final Deserializer<FileMetadata> deserializer = (bytes) ->
	{
		try
		{
			ByteBuf buf = Unpooled.copiedBuffer(bytes);
			final byte[] filenameBytes = new byte[buf.readByte()];
			buf.readBytes(filenameBytes);
			final String filename = new String(filenameBytes);
			final byte[] fileTypeBytes = new byte[buf.readByte()];
			buf.readBytes(fileTypeBytes);
			final String fileType = new String(fileTypeBytes);
			final long fileSize = buf.readLong();
			final int expectedHash = buf.readInt();
			final byte[] createdBytes = new byte[buf.readInt()];
			buf.readBytes(createdBytes);
			final BigInteger created = new BigInteger(createdBytes);
			final byte[] modifiedBytes = new byte[buf.readInt()];
			buf.readBytes(modifiedBytes);
			final BigInteger modified = new BigInteger(modifiedBytes);
			final boolean corrupt = buf.readBoolean();
			return new FileMetadata(filename, fileType, fileSize, expectedHash, created, modified, corrupt);
		} catch (Exception e)
		{
			throw new DeserializationException(e);
		}
	};
	/**
	 * 
	 */
	private static final long serialVersionUID = -7556665102029511004L;
	public final String filename, fileType;
	public final long fileSize;
	public final int expectedHash;
	public final BigInteger dateCreated, dateModified;
	public final boolean corrupted;
	public FileMetadata(FileBase file)
	{
		this(file.filename, file.getClass(), file.filesize, file.expectedHash, file.dateCreated.getData(), file.dateModified.getData(), file.getIsCorrupted());
	}
	
	public FileMetadata(String filename, Class<? extends FileBase> clazz, long fileSize, int expectedHash, BigInteger dateCreated, BigInteger dateModified, boolean corrupted)
	{
		this(filename, clazz.getSimpleName(), fileSize, expectedHash, dateCreated, dateModified, corrupted);
	}
	
	public FileMetadata(String filename, String fileType, long fileSize, int expectedHash, BigInteger dateCreated, BigInteger dateModified, boolean corrupted)
	{
		this.filename = filename;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.expectedHash = expectedHash;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.corrupted = corrupted;
	}
	
	@Override
	public byte[] serialize()
	{
		ByteBuf buf = Unpooled.buffer();
		final byte[] filenameBytes = filename.getBytes();
		final byte[] fileTypeBytes = fileType.getBytes();
		buf.writeByte(filenameBytes.length);
		buf.writeBytes(filenameBytes);
		buf.writeByte(fileTypeBytes.length);
		buf.writeBytes(fileTypeBytes);
		buf.writeLong(fileSize);
		buf.writeInt(hashCode());
		final byte[] createdBytes = dateCreated.toByteArray();
		final byte[] modifiedBytes = dateModified.toByteArray();
		buf.writeInt(createdBytes.length);
		buf.writeBytes(createdBytes);
		buf.writeInt(modifiedBytes.length);
		buf.writeBytes(modifiedBytes);
		buf.writeBoolean(corrupted);
		return buf.array();
	}
	@Override
	public FileMetadata deserialize(byte[] bytes) throws DeserializationException
	{
		return deserializer.deserialize(bytes);
	}

}
