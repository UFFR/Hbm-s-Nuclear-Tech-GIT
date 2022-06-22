package com.hbm.inventory.gui.warpcore;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.container.warpcore.ContainerReactorWarpTank;
import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.tileentity.machine.TileEntityReactorWarp;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.player.InventoryPlayer;

public class GUIReactorWarpTank extends GuiInfoContainer
{
	private final TileEntityReactorWarp core;
	private boolean above;
	public GUIReactorWarpTank(InventoryPlayer invPlayer, TileEntityReactorWarp te)
	{
		super(new ContainerReactorWarpTank(invPlayer, te));
		core = te;
		above = invPlayer.player.posY > core.yCoord;
		xSize = 176;
		ySize = 186;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		final String name = TileEntityReactorWarp.nameIntermix + (above ? 'M' : 'A');
		ArrayList<String> tankInfo = new ArrayList<>();
//		core.getTanks().get(above ? 0 : 1).getTankType().addInfo(tankInfo);
		drawCustomInfoStat(mouseX, mouseY, 83, 24, 10, 58, mouseX, mouseY, tankInfo.toArray(new String[tankInfo.size()]));
		fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, color0);
		fontRendererObj.drawString(I18nUtil.resolveKey("container.inventory"), 8, this.ySize - 96 + 2, color0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1, 1, 1, 1);
//		core.getTanks().get(above ? 0 : 1).renderTank(83, 24, zLevel, 10, 58);
	}

}
