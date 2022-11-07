package com.hbm.util.wrappers;

@FunctionalInterface
public interface EquivalencePredicate<T>
{
	public boolean test(T obj1, T obj2);
}
