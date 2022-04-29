package api.hbm;

import com.hbm.main.DeserializationException;

@FunctionalInterface
public interface Deserializer<T>
{
	public T deserialize(byte[] bytes) throws DeserializationException;
}
