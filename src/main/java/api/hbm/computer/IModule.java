package api.hbm.computer;

import javax.annotation.Nullable;

import com.hbm.util.Tuple.Pair;

import api.hbm.internet.IDataReceiver;

public interface IModule extends IDataReceiver
{
	public enum ModuleType
	{
		MISC,
		STORAGE,
		RADIO,
		REDSTONE,
		REACTOR_CONTROL,
		SENSOR,
		GRAPHER;
	}
	public default Pair<ReturnCode, Object> makeReturn(ReturnCode code, Object data)
	{
		return new Pair<ReturnCode, Object>(code, data);
	}
	public default Pair<ReturnCode, Object> request(String cmd)
	{
		return request(cmd, null);
	}
	
	public Pair<ReturnCode, Object> request(String cmd, @Nullable Object data);
	public String[] cmdList();
	public ModuleType getType();
}
