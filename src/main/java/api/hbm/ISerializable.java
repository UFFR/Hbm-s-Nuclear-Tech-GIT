package api.hbm;

import java.util.HashMap;

import com.hbm.main.DeserializationException;

public interface ISerializable<T> extends Deserializer<T>
{
	static final HashMap<Class<? extends ISerializable<?>>, ISerializable<?>> instanceMap = new HashMap<>();
	static final HashMap<String, Class<? extends ISerializable<?>>> nameMap = new HashMap<>();
	
	public static void register(Class<? extends ISerializable<?>> clazz, ISerializable<?> instance)
	{
		instanceMap.put(clazz, instance);
		nameMap.put(clazz.getSimpleName(), clazz);
	}
	
	public static Object deserialize(Class<? extends ISerializable<?>> clazz, byte[] bytes) throws DeserializationException
	{
		return instanceMap.containsKey(clazz) ? instanceMap.get(clazz).deserialize(bytes) : null;
	}
	
	public static Object deserialize(String simpleName, byte[] bytes) throws DeserializationException
	{
		return nameMap.containsKey(simpleName) ? deserialize(nameMap.get(simpleName), bytes) : null;
	}
	
	public byte[] serialize();
	@Override
	public T deserialize(byte[] bytes) throws DeserializationException;
}
