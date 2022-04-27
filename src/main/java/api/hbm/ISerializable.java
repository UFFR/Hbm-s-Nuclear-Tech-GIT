package api.hbm;

import java.util.HashMap;

public interface ISerializable
{
	static final HashMap<Class<? extends ISerializable>, ISerializable> instanceMap = new HashMap<>();
	static final HashMap<String, Class<? extends ISerializable>> nameMap = new HashMap<>();
	
	public static void register(Class<? extends ISerializable> clazz, ISerializable instance)
	{
		instanceMap.put(clazz, instance);
		nameMap.put(clazz.getSimpleName(), clazz);
	}
	
	public static ISerializable deserialize(Class<? extends ISerializable> clazz, byte[] bytes)
	{
		return instanceMap.containsKey(clazz) ? instanceMap.get(clazz).deserialize(bytes) : null;
	}
	
	public static ISerializable deserialize(String simpleName, byte[] bytes)
	{
		return nameMap.containsKey(simpleName) ? deserialize(nameMap.get(simpleName), bytes) : null;
	}
	
	public byte[] serialize();
	public ISerializable deserialize(byte[] bytes);
}
