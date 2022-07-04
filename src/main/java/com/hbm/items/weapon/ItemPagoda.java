package com.hbm.items.weapon;

import java.util.HashSet;
import java.util.List;

import com.hbm.calc.EasyLocation;
import com.hbm.extprop.HbmLivingProps;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.util.BobMathUtil;

import api.hbm.item.IClickReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemPagoda extends Item implements IClickReceiver
{
	private static final String KEY_CHARGE = "PAGODA_CHARGE", KEY_CHARGING = "PAGODA_CHARGING";
	private static final short MAX_CHARGE = 1200;
	private static final byte MAX_RADIUS = 20;
	public ItemPagoda()
	{
		setMaxStackSize(1);
		setFull3D();
		setUnlocalizedName("pagoda");
		setCreativeTab(MainRegistry.weaponTab);
	}

	@Override
	public boolean handleMouseInput(ItemStack stack, EntityPlayer player, int button, boolean state)
	{
		if (!state || button < 0)
			return false;
		System.out.println(button);
		// FIXME Charges up but doesn't do anything.
		switch (button)
		{
		case 0://Left
			setCharge(stack, 0);
			final short strength = getCharge(stack);
			final float radius = strength / 100;
//			final EasyLocation playerLoc = new EasyLocation(Library.getPosition(1, player)).modifyCoord(0, player.getEyeHeight(), 0);
			final ILocationProvider playerLoc = ILocationProvider.wrap(player, true);
			final EasyLocation checkCoord = new EasyLocation(playerLoc).setWorld(player.getEntityWorld());
			final HashSet<Entity> entities = new HashSet<>();
			BobMathUtil.offsetForHand(player, checkCoord);
			final Vec3 step = BobMathUtil.entityUnitVector(player);
			System.out.println(playerLoc);
			System.out.println(step);
			do
			{
				checkCoord.addCoord(step);
				entities.addAll(player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(checkCoord.posX - radius, checkCoord.posY - radius, checkCoord.posZ - radius, checkCoord.posX + radius, checkCoord.posY + radius, checkCoord.posZ + radius)));
				System.out.println(checkCoord);
			} while (!checkCoord.getBlock().isBlockNormalCube() && ILocationProvider.distance(checkCoord, playerLoc) < 50);
			System.out.println(entities);
			System.out.println(strength);
			for (Entity entity : entities)
			{
				if (entity instanceof EntityLivingBase)
				{
					System.out.println("Radz!");
					entity.attackEntityFrom(new DamageSource("radiation"), strength);
					HbmLivingProps.incrementRadiation((EntityLivingBase) entity, strength);
				}
			}
			break;
		case 2: stack.stackTagCompound.setBoolean(KEY_CHARGING, !isCharging(stack)); break;//Middle
		case 1://Right
			setCharge(stack, 0);
			final List<Entity> entityList = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(player.posX - MAX_RADIUS, player.posY - MAX_RADIUS, player.posZ - MAX_RADIUS, player.posX + MAX_RADIUS, player.posY + MAX_RADIUS, player.posZ + MAX_RADIUS));
			for (Entity entity : entityList)
				if (!Library.isObstructed(player.getEntityWorld(), ILocationProvider.wrap(player, true), ILocationProvider.wrap(entity, false)) && entity instanceof EntityLivingBase)
					HbmLivingProps.incrementRadiation((EntityLivingBase) entity, getCharge(stack) / 100);
			break;
		default: return false;
		}
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean b)
	{
		if (b)
		{
			tooltip.add("Current charge: " + getCharge(stack) + '/' + MAX_CHARGE);
			tooltip.add(Library.toPercentage(getCharge(stack), MAX_CHARGE));
			tooltip.add("Is charging? " + isCharging(stack));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int i, boolean b)
	{
		if (!stack.hasTagCompound())
		{
			stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setShort(KEY_CHARGE, (short) 0);
			stack.stackTagCompound.setBoolean(KEY_CHARGING, false);
		}
		
		if (isCharging(stack) && getCharge(stack) < MAX_CHARGE)
			incrementCharge(stack);
		else if (!isCharging(stack))
			decrementCharge(stack);
	}
	
	private static boolean isCharging(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound.getBoolean(KEY_CHARGING);
	}
	
	private static short getCharge(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound.getShort(KEY_CHARGE);
	}
	
	private static void incrementCharge(ItemStack stack)
	{
		if (getCharge(stack) < MAX_CHARGE)
			setCharge(stack, getCharge(stack) + 5);
		if (getCharge(stack) > MAX_CHARGE)
			setCharge(stack, MAX_CHARGE);
	}
	
	private static void decrementCharge(ItemStack stack)
	{
		if (getCharge(stack) > 0)
			setCharge(stack, getCharge(stack) - 1);
		if (getCharge(stack) < 0)
			setCharge(stack, 0);
	}
	
	private static void setCharge(ItemStack stack, int charge)
	{
		stack.stackTagCompound.setShort(KEY_CHARGE, (short) charge);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return BobMathUtil.durabilityBarDisplay(getCharge(stack), MAX_CHARGE);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
}
