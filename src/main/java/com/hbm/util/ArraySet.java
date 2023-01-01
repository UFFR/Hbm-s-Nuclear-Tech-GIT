package com.hbm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ArraySet<E> extends ArrayList<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7973377936218273517L;
	private final Set<E> set;
	public ArraySet()
	{
		set = new HashSet<E>();
	}

	public ArraySet(int capacity)
	{
		super(capacity);
		set = new HashSet<E>(capacity);
	}

	public ArraySet(Collection<? extends E> c)
	{
		super(c);
		set = new HashSet<E>(c);
		retainAll(set);
	}
	
	@Override
	public boolean add(E e)
	{
		if (set.contains(e))
			return false;
		else
			return super.add(e);
	}
	
	@Override
	public void add(int index, E element)
	{
		if (!set.contains(element))
			super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if (set.addAll(c))
		{
			super.addAll(c);
			retainAll(set);
			return true;
		} else
			return false;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if (set.addAll(c))
		{
			super.addAll(index, c);
			retainAll(set);
			return true;
		} else
			return false;
	}

	@Override
	public E remove(int index)
	{
		final E item = super.remove(index);
		set.remove(item);
		return item;
	}
	
	@Override
	public boolean remove(Object o)
	{
		set.remove(o);
		return super.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		set.removeAll(c);
		return super.removeAll(c);
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter)
	{
		set.removeIf(filter);
		return super.removeIf(filter);
	}
	
	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		super.replaceAll(operator);
		set.clear();
		set.addAll(this);
		retainAll(set);
	}
	
	@Override
	public E set(int index, E element)
	{
		if (set.add(element))
			return super.set(index, element);
		else
			return element;
	}
}
