package com.hbm.util;

import java.util.function.Function;

public class HashWrapper<T>
{
	public final T storedObj;
	private final Function<T, Integer> hashFunction;
	private final Function<T, Boolean> equalFunction;
	public HashWrapper(T storedObj, Function<T, Integer> hashFunction, Function<T, Boolean> equalFunction)
	{
		this.storedObj = storedObj;
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
}
