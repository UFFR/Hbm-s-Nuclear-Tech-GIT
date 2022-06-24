package com.hbm.util;

import java.util.function.Function;

public class ObjectWrapper<T>
{
	public final T storedObj;
	private final Function<T, String> stringFunction;
	private final Function<T, Integer> hashFunction;
	private final Function<T, Boolean> equalFunction;
	public ObjectWrapper(
			T storedObj, Function<T, String> stringFunction, Function<T, Integer> hashFunction,
			Function<T, Boolean> equalFunction
	)
	{
		this.storedObj = storedObj;
		this.stringFunction = stringFunction;
		this.hashFunction = hashFunction;
		this.equalFunction = equalFunction;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(obj.getClass().equals(storedObj.getClass())))
			return false;
		return equalFunction.apply((T) obj);
	}
	
	@Override
	public int hashCode()
	{
		return hashFunction.apply(storedObj);
	}
	
	@Override
	public String toString()
	{
		return stringFunction.apply(storedObj);
	}
}
