package com.hbm.items.special;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.hbm.util.Tuple.Pair;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemSimpleConsumable extends ItemCustomLore {
	
	//if java is giving me the power of generics and delegates then i'm going to use them, damn it!
	private Optional<BiConsumer<ItemStack, EntityPlayer>> useAction = Optional.empty();
	private Optional<BiConsumer<ItemStack, EntityPlayer>> useActionServer = Optional.empty();
	private Optional<BiConsumer<ItemStack, Pair<EntityLivingBase, EntityLivingBase>>> hitAction = Optional.empty();
	private Optional<BiConsumer<ItemStack, Pair<EntityLivingBase, EntityLivingBase>>> hitActionServer = Optional.empty();
	private EnumAction action = EnumAction.none;
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		if(this.useAction.isPresent())
			this.useAction.get().accept(stack, player);
		
		if(!world.isRemote && this.useActionServer.isPresent())
			this.useActionServer.get().accept(stack, player);
		
		return stack;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase entityPlayer) {
		
		if(this.hitAction.isPresent())
			this.hitAction.get().accept(stack, new Pair<>(entity, entityPlayer));
		
		if(!entity.worldObj.isRemote && this.hitActionServer.isPresent())
			this.hitActionServer.get().accept(stack, new Pair<>(entity, entityPlayer));
		
		return false;
	}
	
	public static void giveSoundAndDecrement(ItemStack stack, EntityLivingBase entity, String sound, ItemStack container) {
		stack.stackSize--;
		entity.worldObj.playSoundAtEntity(entity, sound, 1.0F, 1.0F);
		ItemSimpleConsumable.tryAddItem(entity, container);
	}
	
	public static void addPotionEffect(EntityLivingBase entity, Potion effect, int duration, int level) {
		
		if(!entity.isPotionActive(effect)) {
			entity.addPotionEffect(new PotionEffect(effect.id, duration, level));
		} else {
			int d = entity.getActivePotionEffect(effect).getDuration() + duration;
			entity.addPotionEffect(new PotionEffect(effect.id, d, level));
		}
	}
	
	public static void tryAddItem(EntityLivingBase entity, ItemStack stack) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if(!player.inventory.addItemStackToInventory(stack)) {
				player.dropPlayerItemWithRandomChoice(stack, false);
			}
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return action;
	}
	
	public ItemSimpleConsumable setActionType(EnumAction action)
	{
		this.action = action;
		return this;
	}
	
	//this formatting style probably already has a name but i will call it "the greg"
	public ItemSimpleConsumable setUseAction(		BiConsumer<ItemStack, EntityPlayer> delegate) {								this.useAction = Optional.of(delegate);			return this; }
	public ItemSimpleConsumable setUseActionServer(	BiConsumer<ItemStack, EntityPlayer> delegate) {								this.useActionServer = Optional.of(delegate);	return this; }
	public ItemSimpleConsumable setHitAction(		BiConsumer<ItemStack, Pair<EntityLivingBase, EntityLivingBase>> delegate) {	this.hitAction = Optional.of(delegate);			return this; }
	public ItemSimpleConsumable setHitActionServer(	BiConsumer<ItemStack, Pair<EntityLivingBase, EntityLivingBase>> delegate) {	this.hitActionServer = Optional.of(delegate);	return this; }
}
