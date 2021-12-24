package com.hbm.inventory.gui.warpcore;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.machine.TileEntityReactorWarp;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GUIReactorWarpBase extends GuiInfoContainer
{
	protected static ResourceLocation texture;
	protected final TileEntityReactorWarp warpCore;
	public GUIReactorWarpBase(TileEntityReactorWarp te, Class<? extends Container> containerClass, String tex) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		super(containerClass.getConstructor(TileEntityReactorWarp.class).newInstance(te));
		texture = new ResourceLocation(RefStrings.MODID, tex);
		warpCore = te;
	}

	@Override
	protected abstract void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_);
	
	@Override
	public abstract void drawScreen(int x, int y, float f);
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
