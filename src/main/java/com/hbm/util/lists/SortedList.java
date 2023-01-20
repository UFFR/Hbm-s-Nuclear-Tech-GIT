package com.hbm.util.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<E> extends ArrayList<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3739610227085564599L;
	private final Comparator<E> comparator;
	public SortedList(Comparator<E> comparator)
	{
		this.comparator = comparator;
	}

	public SortedList(int initialSize, Comparator<E> comparator)
	{
		super(initialSize);
		this.comparator = comparator;
	}

	public SortedList(Collection<? extends E> c, Comparator<E> comparator)
	{
		super(c);
		this.comparator = comparator;
		sort(comparator);
	}
	
	@Override
	public boolean add(E e)
	{
		super.add(e);
		sort(comparator);
		return true;
	}

	@Override
	public void add(int index, E element)
	{
		super.add(index, element);
		sort(comparator);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		super.addAll(c);
		sort(comparator);
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		super.addAll(index, c);
		sort(comparator);
		return true;
	}
	
	@Override
	public E set(int index, E element)
	{
		final E old = super.set(index, element);
		sort(comparator);
		return old;
	}
}
