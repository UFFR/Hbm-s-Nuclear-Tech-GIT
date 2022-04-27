package com.hbm.items.weapon;

import java.util.List;

import com.google.common.annotations.Beta;
import com.hbm.entity.grenade.EntityGrenadeBouncyBaseNT;
import com.hbm.items.special.ItemCustomLore;
import com.hbm.items.weapon.ItemGrenadeEnum.GrenadeLogic;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
@Beta
public class ItemGrenadeNT extends ItemCustomLore
{
	protected final int fuse;
	protected final GrenadeLogic logic;
	public ItemGrenadeNT(int fuse, GrenadeLogic logic)
	{
		this.fuse = fuse;
		this.logic = logic;
	}

	private String translateFuse()
	{
		switch (fuse)
		{
		case -1:
			return I18nUtil.resolveKey("desc.item.grenade.fuseImpact");
		case 0:
			return I18nUtil.resolveKey("desc.item.grenade.fuseInstant");
		default:
			return fuse + "s";
		}
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool) {

		list.add(I18nUtil.resolveKey("desc.item.grenade.fuse", translateFuse()));
		if (ItemCustomLore.getHasLore(itemstack.getItem()))
			list.add("");
		super.addInformation(itemstack, player, list, bool);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
			stack.stackSize--;
		
		world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote)
			world.spawnEntityInWorld(new EntityGrenadeBouncyBaseNT(world, player, fuse * 20, logic));
		
		return stack;
	}
}
