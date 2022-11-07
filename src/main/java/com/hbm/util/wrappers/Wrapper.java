package com.hbm.util.wrappers;

import java.util.Collection;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Wraps an object to force implementation or re-implementation of the basic Object methods of: {@link #hashCode()}, {@link #equals(Object)}, and {@link #toString()}. 
 * @author UFFR
 *
 * @param <T> The type of object wrapped.
 */
public interface Wrapper<T>
{
	public T getObject();
	@Override
	public int hashCode();
	@Override
	public boolean equals(Object obj);
	@Override
	public String toString();
	
	public static boolean testItem(Item item1, Item item2)
	{
		return item1 == item2 || Objects.equals(item1.getUnlocalizedName(), item2.getUnlocalizedName())
				&& Objects.equals(Item.itemRegistry.getNameForObject(item1), Item.itemRegistry.getNameForObject(item2));
	}
	
	public static boolean testItemStack(ItemStack stack1, ItemStack stack2)
	{
		return testItem(stack1.getItem(), stack2.getItem())
				&& stack1.stackSize == stack2.stackSize && (stack1.getItemDamage() == stack2.getItemDamage()
					|| stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				&& Objects.equals(stack1.getTagCompound(), stack2.getTagCompound());
	}
	
	public static boolean testBlock(Block block1, Block block2)
	{
		return block1 == block2 || Objects.equals(Block.blockRegistry.getNameForObject(block1), Block.blockRegistry.getNameForObject(block2));
	}
	
	public static int hashItem(Item item)
	{
		return Item.itemRegistry.getNameForObject(item).hashCode();
	}
	
	public static int hashItemStack(ItemStack stack)
	{
		return Objects.hash(hashItem(stack.getItem()), stack.stackSize, stack.stackTagCompound);
	}
	
	public static int hashBlock(Block block)
	{
		return Block.blockRegistry.getNameForObject(block).hashCode();
	}
	
	public static boolean addWrappedItem(Item item, Collection<Wrapper<Item>> wrappers)
	{
		return wrappers.add(new HashWrapper<Item>(item, Wrapper::hashItem, Wrapper::testItem));
	}
	
	public static boolean addWrappedItemStack(ItemStack stack, Collection<Wrapper<ItemStack>> wrappers)
	{
		return wrappers.add(new HashWrapper<ItemStack>(stack, Wrapper::hashItemStack, Wrapper::testItemStack));
	}
	
	public static boolean addWrappedBlock(Block block, Collection<Wrapper<Block>> wrappers)
	{
		return wrappers.add(new HashWrapper<Block>(block, Wrapper::hashBlock, Wrapper::testBlock));
	}
	
	public static boolean doesContain(Item item, Collection<Wrapper<Item>> wrappers)
	{
		return wrappers.contains(new HashWrapper<Item>(item, Wrapper::hashItem, Wrapper::testItem));
	}
	
	public static boolean doesContain(ItemStack stack, Collection<Wrapper<ItemStack>> wrappers)
	{
		return wrappers.contains(new HashWrapper<ItemStack>(stack, Wrapper::hashItemStack, Wrapper::testItemStack));
	}
	
	public static boolean doesContain(Block block, Collection<Wrapper<Block>> wrappers)
	{
		return wrappers.contains(new HashWrapper<Block>(block, Wrapper::hashBlock, Wrapper::testBlock));
	}
}
