package com.hbm.util.lists;

import java.util.ArrayList;
import java.util.Collection;

public class CacheList<E> extends ArrayList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778154396642186275L;
	private final int maxSize;
	public CacheList(int capacity)
	{
		super(capacity);
		maxSize = capacity;
	}

	public CacheList(Collection<? extends E> elements)
	{
		super(elements);
		maxSize = elements.size();
	}

	public int getMaxSize()
	{
		return maxSize;
	}
	
	@Override
	public boolean add(E e)
	{
		super.add(e);
		if (size() > maxSize)
			remove(0);
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		super.addAll(c);
		if (size() > maxSize)
			removeRange(0, size() - maxSize);
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		super.addAll(index, c);
		if (size() > maxSize)
			removeRange(0, size() - maxSize);
		return true;
	}
}
