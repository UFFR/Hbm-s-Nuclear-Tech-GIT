package com.hbm.blocks.machine;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.hbm.blocks.ModBlocks;
import com.hbm.handler.FluidTypeHandler.FluidType;
import com.hbm.interfaces.Spaghetti;
import com.hbm.lib.HbmCollection;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.machine.TileEntityBarrel;
import com.hbm.util.I18nUtil;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFluidBarrel extends BlockContainer {
	
	int capacity;
	public List<String> tooltip = new ArrayList<String>();
	private BarrelHold maxTier = BarrelHold.NONE;
	private boolean isLeaky = false;

	public BlockFluidBarrel(Material p_i45386_1_, int capacity, BarrelHold maxTier, boolean leaky) {
		super(p_i45386_1_);
		this.capacity = capacity;
		this.maxTier = maxTier;
		isLeaky = leaky;
		makeTooltip();
	}
	
	public static enum BarrelHold
	{
		NONE(""),
		HOT_FLUID("desc.block.barrel.hotFluid"),
		ACID("desc.block.barrel.acid"),
		ACID_STRONG("desc.block.barrel.acidStrong"),
		ACID_ALT("desc.block.barrel.acidAlt"),
		ANTIMATTER("desc.block.barrel.antimatter");
		private String key;
		private BarrelHold(String string)
		{
			key = string;
		}
	}
	
	private String getLoc(BarrelHold type)
	{
		return I18nUtil.resolveKey(type.key);
	}
	
	@Spaghetti("make it end")
	private void makeTooltip()
	{
		final String store = "desc.block.barrel.store";
		final String cannot = I18nUtil.resolveKey("desc.block.barrel.cannot");
		final String can = I18nUtil.resolveKey("desc.block.barrel.can");
		
		boolean[] boolList = new boolean[5];
		Arrays.fill(boolList, false);
		
		tooltip.add(I18nUtil.resolveKey(HbmCollection.capacity, NumberFormat.getIntegerInstance().format(capacity)));
		
		switch(maxTier)
		{
		case HOT_FLUID:
			boolList[0] = true;// Hot fluids
			boolList[1] = false;// Acids, but poorly
			boolList[2] = false;// "Acids"
			boolList[3] = false;// Strong acids
			boolList[4] = false;// Antimatter
			break;
		case ACID_ALT:
			boolList[0] = true;
			boolList[1] = true;
			boolList[2] = false;
			boolList[3] = false;
			boolList[4] = false;
			break;
		case ACID:
			boolList[0] = true;
			boolList[1] = false;
			boolList[2] = true;
			boolList[3] = false;
			boolList[4] = false;
			break;
		case ACID_STRONG:
			boolList[0] = true;
			boolList[1] = false;
			boolList[2] = true;
			boolList[3] = true;
			boolList[4] = false;
			break;
		case ANTIMATTER:
			boolList[0] = true;
			boolList[1] = false;
			boolList[2] = true;
			boolList[3] = true;
			boolList[4] = true;
			break;
		default:
			break;
		}
		
		tooltip.add(I18nUtil.resolveKey(store, boolList[0] ? can : cannot, getLoc(BarrelHold.HOT_FLUID)));
		if (boolList[3])
			tooltip.add(I18nUtil.resolveKey(store, can, getLoc(BarrelHold.ACID_STRONG)));
		else if (boolList[2])
		{
			tooltip.add(I18nUtil.resolveKey(store, can, getLoc(BarrelHold.ACID)));
			tooltip.add(I18nUtil.resolveKey(store, isLeaky || boolList[4] ? can : cannot, getLoc(BarrelHold.ACID_STRONG)));
		}
		else if (boolList[1])
			tooltip.add(I18nUtil.resolveKey(store, cannot, getLoc(BarrelHold.ACID_ALT)));
		else
			tooltip.add(I18nUtil.resolveKey(store, cannot, getLoc(BarrelHold.ACID)));
		tooltip.add(I18nUtil.resolveKey(store, boolList[4] ? can : cannot, getLoc(BarrelHold.ANTIMATTER)));
		if (isLeaky)
			tooltip.add(I18nUtil.resolveKey("desc.block.barrel.leaky"));
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityBarrel(capacity);
	}
    
    public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	
	@Override
	public int getRenderType(){
		return renderID;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
			
		} else if(!player.isSneaking()) {
			FMLNetworkHandler.openGui(player, MainRegistry.instance, ModBlocks.guiID_barrel, world, x, y, z);
			return true;
			
		} else {
			return false;
		}
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        float f = 0.0625F;
        this.setBlockBounds(2*f, 0.0F, 2*f, 14*f, 1.0F, 14*f);
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        float f = 0.0625F;
        this.setBlockBounds(2*f, 0.0F, 2*f, 14*f, 1.0F, 14*f);
		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

    private final Random field_149933_a = new Random();
	public static boolean keepInventory;
	
	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        if (!keepInventory)
        {
        	ISidedInventory tileentityfurnace = (ISidedInventory)p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

            if (tileentityfurnace != null)
            {
                for (int i1 = 0; i1 < tileentityfurnace.getSizeInventory(); ++i1)
                {
                    ItemStack itemstack = tileentityfurnace.getStackInSlot(i1);

                    if (itemstack != null)
                    {
                        float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0)
                        {
                            int j1 = this.field_149933_a.nextInt(21) + 10;

                            if (j1 > itemstack.stackSize)
                            {
                                j1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= j1;
                            EntityItem entityitem = new EntityItem(p_149749_1_, p_149749_2_ + f, p_149749_3_ + f1, p_149749_4_ + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound())
                            {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (float)this.field_149933_a.nextGaussian() * f3;
                            entityitem.motionY = (float)this.field_149933_a.nextGaussian() * f3 + 0.2F;
                            entityitem.motionZ = (float)this.field_149933_a.nextGaussian() * f3;
                            p_149749_1_.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
            }
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

}
