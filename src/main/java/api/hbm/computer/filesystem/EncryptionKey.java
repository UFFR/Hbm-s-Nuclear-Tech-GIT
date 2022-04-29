package api.hbm.computer.filesystem;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.hbm.main.DeserializationException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EncryptionKey extends FileBase
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5971864114448314627L;
	private static final SecureRandom RANDOM = new SecureRandom();
	private IvParameterSpec spec;
	private SecretKey secretKey;
	private boolean invalid = false;
	public EncryptionKey()
	{
		spec = genIvSpec();
		try
		{
			secretKey = genKey();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			invalid = true;
		}
	}
	
	public EncryptionKey(String password)
	{
		spec = genIvSpec();
		try
		{
			secretKey = genKey(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			e.printStackTrace();
			invalid = true;
		}
	}
	
	public EncryptionKey(SecretKey key, IvParameterSpec spec)
	{
		secretKey = key;
		this.spec = spec;
	}
	
	public byte[] encryptToBytes(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
		return cipher.doFinal(input);
	}
	
	public String encryptToString(byte[] input) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		return Base64.getEncoder().encodeToString(encryptToBytes(input));
	}
	
	public byte[] decryptToBytes(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
		return cipher.doFinal(input);
	}
	
	public byte[] decryptToString(byte[] input) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		return Base64.getDecoder().decode(decryptToBytes(input));
	}
	
	private static SecretKey genKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		final byte[] salt = new byte[256];
		RANDOM.nextBytes(salt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}
	
	private static SecretKey genKey() throws NoSuchAlgorithmException
	{
		final KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(256);
		return generator.generateKey();
	}
	
	private static IvParameterSpec genIvSpec()
	{
		final byte[] bytes = new byte[32];
		RANDOM.nextBytes(bytes);
		return new IvParameterSpec(bytes);
	}

	@Override
	public byte[] serialize()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeBoolean(invalid);
		if (!invalid)
		{
			final byte[] specBytes = spec.getIV();
			final byte[] keyBytes = secretKey.getEncoded();
			buf.writeBytes(specBytes);
			buf.writeBytes(keyBytes);
		}
		return buf.array();
	}

	@Override
	public EncryptionKey deserialize(byte[] bytes) throws DeserializationException
	{
		ByteBuf buf = Unpooled.buffer();
		if (buf.readBoolean())
			return (EncryptionKey) new EncryptionKey().setCorrupted();
		
		final byte[] specBytes = new byte[32];
		final byte[] keyBytes = new byte[256];
		buf.readBytes(specBytes);
		buf.readBytes(keyBytes);
		return new EncryptionKey(new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(specBytes));
	}

	@Override
	public int hashCode()
	{
		return 31 * spec.hashCode() * 7 * secretKey.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof EncryptionKey))
			return false;
		
		EncryptionKey test = (EncryptionKey) obj;
		return spec.equals(test.spec) && secretKey.equals(test.secretKey) && invalid == test.invalid;
	}

}
