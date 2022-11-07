package com.hbm.util.wrappers;

import java.util.function.Function;

public class HashWrapper<T> implements Wrapper<T>
{
	private final T obj;
	private final Function<T, Integer> hashFunction;
	private final EquivalencePredicate<T> equalPredicate;
	public HashWrapper(T obj, Function<T, Integer> hashFunction, EquivalencePredicate<T> equalPredicate)
	{
		this.obj = obj;
		this.hashFunction = hashFunction;
		this.equalPredicate = equalPredicate;
	}

	@Override
	public T getObject()
	{
		return obj;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this || this.obj == obj)
			return true;
		if (!(obj instanceof HashWrapper) || !(this.obj.getClass().isAssignableFrom(obj.getClass())))
			return false;
		final T toTest;
		if (obj instanceof HashWrapper)
		{
			final HashWrapper<T> other = (HashWrapper<T>) obj;
			if (!this.obj.getClass().isAssignableFrom(other.getClass()))
				return false;
			else
				toTest = other.getObject();
		} else
			toTest = (T) obj;
		return equalPredicate.test(this.obj, toTest);
	}
	
	@Override
	public int hashCode()
	{
		return hashFunction.apply(getObject());
	}
}
