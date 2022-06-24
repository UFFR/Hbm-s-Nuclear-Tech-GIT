package com.hbm.items.special;

import java.util.List;
import java.util.function.Consumer;

import com.hbm.calc.EasyLocation;
import com.hbm.entity.effect.EntityTesseract;
import com.hbm.items.ModItems;
import com.hbm.lib.HbmCollection;
import com.hbm.main.MainRegistry;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
public class ItemDrop extends ItemCustomLore {

	private Consumer<EntityItem> behavior;
	public ItemDrop(Consumer<EntityItem> behavior)
	{
		this.behavior = behavior;
	}
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		if (entityItem != null && entityItem.getEntityItem() != null && !entityItem.worldObj.isRemote) {
			
			behavior.accept(entityItem);

			if (entityItem.onGround) {

				entityItem.setDead();
				return true;
			}
		}
		return false;
    }
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(itemstack, player, list, bool);
		if (this == ModItems.detonator_deadman)
		{
			if(itemstack.getTagCompound() == null)
				list.add(I18nUtil.resolveKey(HbmCollection.noPos));
			else
				list.add(I18nUtil.resolveKey(HbmCollection.pos, itemstack.stackTagCompound.getInteger("x"), itemstack.stackTagCompound.getInteger("y"), itemstack.stackTagCompound.getInteger("z")));

		}
		list.add(EnumChatFormatting.RED + "[" + I18nUtil.resolveKey(HbmCollection.drop) + "]");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int p_77663_4_,
			boolean p_77663_5_)
	{
		if (stack.getItem() == ModItems.singularity_counter_resonant && worldIn.rand.nextInt(750) == 0)
		{
			MainRegistry.logger.info("Tesseract preparing to spawn");
			EasyLocation loc = new EasyLocation(entityIn);
			EasyLocation newLoc;
			do
			{
				newLoc = loc.clone();
				newLoc.modifyCoord(worldIn.rand.nextInt(10), worldIn.rand.nextInt(10), worldIn.rand.nextInt(10));
			}
			while (newLoc.getBlock() != Blocks.air);
			EntityTesseract tesseract = new EntityTesseract(worldIn);
			tesseract.posX = newLoc.posX;
			tesseract.posY = newLoc.posY;
			tesseract.posZ = newLoc.posZ;
			worldIn.spawnEntityInWorld(tesseract);
		}
	}
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
		if(this != ModItems.detonator_deadman) {
			return super.onItemUse(stack, player, world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
		}
		
		if(stack.stackTagCompound == null)
		{
			stack.stackTagCompound = new NBTTagCompound();
		}
		
		if(player.isSneaking())
		{
			stack.stackTagCompound.setInteger("x", x);
			stack.stackTagCompound.setInteger("y", y);
			stack.stackTagCompound.setInteger("z", z);
			
			if(world.isRemote)
			{
				player.addChatMessage(new ChatComponentText("Position set!"));
			}
			
	        world.playSoundAtEntity(player, "hbm:item.techBoop", 2.0F, 1.0F);
        	
			return true;
		}
		
		return false;
    }

}