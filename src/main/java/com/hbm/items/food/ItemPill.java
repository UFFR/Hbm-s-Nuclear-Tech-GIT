package com.hbm.items.food;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.hbm.config.VersatileConfig;
import com.hbm.interfaces.IHasLore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
public class ItemPill extends ItemFood implements IHasLore
{
	public interface PillConsumed
	{
		public void onConsume(ItemStack stack, World world, EntityPlayer player);
	}
	private Optional<PillConsumed> consumeAction = Optional.empty();
	private EnumAction action = EnumAction.none;
	public ItemPill(int hunger) {
		super(hunger, false);
		this.setAlwaysEdible();
	}

	public ItemPill(int hunger, PillConsumed action)
	{
		this(hunger);
		consumeAction = Optional.of(action);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return action;
	}
	
	public ItemPill setActionType(EnumAction action)
	{
		this.action = action;
		return this;
	}
	
	Random rand = new Random();

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			
			VersatileConfig.applyPotionSickness(player, 5);
			
			if (consumeAction.isPresent())
				consumeAction.get().onConsume(stack, world, player);
			
		}
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
	{
		standardLore(itemstack, list);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 10;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {

		if(!VersatileConfig.hasPotionSickness(p_77659_3_))
			p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));

		return p_77659_1_;
	}

}
