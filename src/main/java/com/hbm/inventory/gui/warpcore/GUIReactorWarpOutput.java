package com.hbm.inventory.gui.warpcore;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.container.warpcore.ContainerReactorWarpOutput;
import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.machine.TileEntityReactorWarp;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIReactorWarpOutput extends GuiInfoContainer
{
	private static final ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/gui/reactor/warpcore/gui_outputs.png");
	public GUIReactorWarpOutput(InventoryPlayer invPlayer, TileEntityReactorWarp te)
	{
		super(new ContainerReactorWarpOutput(invPlayer, te));
		xSize = 176;
		ySize = 186;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		final String name = TileEntityReactorWarp.nameCoreOut;
		fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, color0);
		fontRendererObj.drawString(I18nUtil.resolveKey("container.inventory"), 8, this.ySize - 96 + 2, color0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(texture);
	}

}
