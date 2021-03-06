package com.hbm.inventory.gui;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.FluidTank;
import com.hbm.inventory.container.ContainerMachineSingGen;
import com.hbm.items.ModItems;
import com.hbm.items.special.ItemAMSCore;
import com.hbm.lib.RefStrings;
import com.hbm.packet.AuxButtonPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.machine.TileEntityMachineSingGen;
import com.hbm.util.I18nUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class GUIMachineSingGen extends GuiInfoContainer
{
	private static ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/gui/machine/gui_sing_gen.png");
	private TileEntityMachineSingGen singGen;
	
	public GUIMachineSingGen(InventoryPlayer invPlayer, TileEntityMachineSingGen tileEntity)
	{
		super(new ContainerMachineSingGen(invPlayer, tileEntity));
		singGen = tileEntity;
		this.xSize = 175;
		this.ySize = 226;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		super.drawScreen(mouseX, mouseY, f);
		
		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 132, guiTop + 111, 18, 18, mouseX, mouseY, I18nUtil.resolveKeyArray("hbmfluid." + singGen.tank.getTankType().toString().toLowerCase()));
		
		this.drawElectricityInfo(this, mouseX, mouseY, guiLeft + 8, guiTop + 22, 16, 88, singGen.power, singGen.maxPower);
		
		singGen.tank.renderTankInfo(this, mouseX, mouseY, guiLeft + 152, guiTop + 22, 16, 70);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		String name = this.singGen.hasCustomInventoryName() ? this.singGen.getInventoryName() : I18n.format(this.singGen.getInventoryName());
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		
		String state = I18n.format("singGen.isRunning." + singGen.isProcessing());
		String progress = singGen.getProgressPercent() + " %";
		this.fontRendererObj.drawString(state, ((43 + 72) / 2) - (this.fontRendererObj.getStringWidth(state) / 2) + 4, 116, 0x8080ff);
		this.fontRendererObj.drawString(progress, ((97 + 132) / 2) - (this.fontRendererObj.getStringWidth(progress) / 2) + 4, 116, 0x8080ff);
	}
	
	//@Override
	protected void mouseClicked(int x, int y, int i)
	{
		super.mouseClicked(x, y, i);
		if (guiLeft + 25 <= x && guiLeft + 25 + 18 > x && guiTop + 111 < y && guiTop + 111 + 18 >= y)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(singGen.xCoord, singGen.yCoord, singGen.zCoord, 0, 0));
		}
		if (guiLeft + 132 <= x && guiLeft + 132 + 18 > x && guiTop + 111 < y && guiTop + 111 + 18 >= y)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(singGen.xCoord, singGen.yCoord, singGen.zCoord, 0, 1));
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int pow = singGen.getPowerScaled(88);
		drawTexturedModalRect(guiLeft + 8, guiTop + 110 - pow, 176, 88 - pow, 16, pow);
		
		if (singGen.isOn)
			drawTexturedModalRect(guiLeft + 25, guiTop + 111, 192, 0, 18, 18);
		
		if (singGen.isProcessing())
			drawTexturedModalRect(guiLeft + 59, guiTop + 33, 176, 88, 58, 48);
		
		if (singGen.currentRecipe != null)
		{
			Item out = singGen.currentRecipe.getOutput().getItem();
			if (out == ModItems.singularity)
			{
				drawTexturedModalRect(guiLeft + 80, guiTop + 125, 210, 4, 16, 4);
			}
			if (out == ModItems.singularity_super_heated)
			{
				drawTexturedModalRect(guiLeft + 80, guiTop + 125, 210, 8, 16, 4);
			}
			if (out == ModItems.singularity_counter_resonant)
			{
				drawTexturedModalRect(guiLeft + 80, guiTop + 125, 210, 12, 16, 4);
			}
			if (out == ModItems.black_hole)
			{
				drawTexturedModalRect(guiLeft + 80, guiTop + 125, 210, 16, 16, 4);
			}
			if (out == ModItems.overfuse || out == ModItems.singularity_spark || out instanceof ItemAMSCore)
			{
				drawTexturedModalRect(guiLeft + 80, guiTop + 125, 210, 20, 16, 4);
			}
		}
		
		switch(singGen.tank.getTankType())
		{
		case ASCHRAB:
			drawTexturedModalRect(guiLeft + 133, guiTop + 111, 226, 0, 18, 18);
			break;
		case LAVA:
			drawTexturedModalRect(guiLeft + 133, guiTop + 111, 226, 18, 18, 18);
		default:
			break;
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(singGen.tank.getSheet());
		singGen.tank.renderTank(this, guiLeft + 152, guiTop + 92, singGen.tank.getTankType().textureX() * FluidTank.x, singGen.tank.getTankType().textureY() * FluidTank.y, 16, 70);
	}

}
