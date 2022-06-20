package com.hbm.inventory.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.hbm.inventory.container.ContainerBishamonten;
import com.hbm.lib.RefStrings;
import com.hbm.packet.AuxButtonPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.turret.TileEntityBishamonten;
import com.hbm.util.I18nUtil;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("deprecation")
public class GUIBishamonten extends GuiInfoContainer
{
	private static final ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/gui/weapon/gui_turret_bishamonten.png");
	private final TileEntityBishamonten luna;
	private final NumberDisplay[] displays = new NumberDisplay[3];
	private GuiTextField tField;
	private byte timer = 0;
	public GUIBishamonten(InventoryPlayer invPlayer, TileEntityBishamonten te)
	{
		super(new ContainerBishamonten(invPlayer, te));
		luna = te;
		xSize = 191;
		ySize = 215;
		displays[0] = new NumberDisplay(69, 26).setMaxMin(360, 0);
		displays[1] = new NumberDisplay(69, 51).setMaxMin(50, -50);
		displays[2] = new NumberDisplay(98, 26).setDigitLength((byte) 4).setMaxMin(100, 0);
		componentList.add(new GUIComponent(20, 18, 35, 13).setClickAction(gui -> PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(luna, 0, 0))));
		componentList.add(new GUIComponent(20, 45, 35, 13).setClickAction(gui -> PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(luna, 0, 1))));
		componentList.add(new GUIComponent(20, 72, 35, 13).setClickAction(gui -> PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(luna, 0, 2))));
		componentList.add(new GUIComponent(20, 99, 35, 13).setClickAction(gui -> PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(luna, 0, 3))));
		componentList.add(new GUIComponent(109, 59, 35, 13).setClickAction(gui -> {PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(luna, 2, validateTextInput(tField.getText(), 0, 100).byteValue()));tField.setText("");timer = 20;}));
		componentList.add(new GUIComponent(63, 92, 35, 13).setClickAction(gui -> PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(luna, 1, 0))));
	}

	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		tField = new GuiTextField(fontRendererObj, guiLeft + 112, guiTop + 45, 29, 12);
		tField.setTextColor(-1);
		tField.setDisabledTextColour(-1);
		tField.setEnableBackgroundDrawing(false);
		tField.setMaxStringLength(3);
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		super.drawScreen(mouseX, mouseY, f);
		drawElectricityInfo(this, mouseX, mouseY, guiLeft + 166, guiTop + 19, 16, 95, luna.getPower(), luna.getMaxPower());
		drawElectricityInfo(this, mouseX, mouseY, guiLeft + 148, guiTop + 19, 16, 95, luna.getBuffer(), TileEntityBishamonten.maxBuffer);
		String dir = String.valueOf(luna.getDirection()), ele = String.valueOf(luna.getElevation()), pow = String.valueOf(displays[2].getNumber().byteValue()) + '%', purify = I18nUtil.resolveKey("twr.gui.purify");
		drawCustomInfoStat(mouseX, mouseY, guiLeft + 67, guiTop + 24, 28, 16, mouseX, mouseY, dir);
		drawCustomInfoStat(mouseX, mouseY, guiLeft + 67, guiTop + 49, 28, 16, mouseX, mouseY, ele);
		drawCustomInfoStat(mouseX, mouseY, guiLeft + 111, guiTop + 24, 32, 16, mouseX, mouseY, pow);
		drawCustomInfoStat(mouseX, mouseY, guiLeft + 62, guiTop + 92, 35, 16, guiLeft + 85, guiTop + 107, purify);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int i)
	{
		super.mouseClicked(mouseX, mouseY, i);
		tField.setFocused(getTextBool(mouseX, mouseY, tField));
		defaultClickHandling(mouseX, mouseY);
	}
	static final float scale = 2F;
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		final String name = I18nUtil.resolveKey(luna.getInventoryName());
		fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, color0);
		fontRendererObj.drawString(I18nUtil.resolveKey("container.inventory"), 14, ySize - 96 + 2, color0);
		
		String currText = tField.getText();
		char cursor = getCursor();
		if (tField.isFocused())
			currText = currText.substring(0, tField.getCursorPosition()) + cursor + currText.substring(tField.getCursorPosition(), currText.length());
		fontRendererObj.drawString(currText, 116, 47, color1);

		GL11.glScalef(1F / scale, 1F / scale, 1F);
		final String[] modes = I18nUtil.resolveKeyArray("desc.gui.bishamonten.mode");
		for (int i = 0; i < 4; i++)
			fontRendererObj.drawString(modes[i], 37 * 2 - fontRendererObj.getStringWidth(modes[i]) / 2, ((luna.getModeData()[2] == i ? 23 : 21) + (i * 27)) * 2, 0);
		String dir = I18nUtil.resolveKey("desc.gui.bishamonten.direction"),	ele = I18nUtil.resolveKey("desc.gui.bishamonten.elevation"), per = I18nUtil.resolveKey("desc.gui.bishamonten.percentage"), buf = I18nUtil.resolveKey("desc.gui.bishamonten.buffer"), pow = I18nUtil.resolveKey("desc.gui.bishamonten.power"), cor = I18nUtil.resolveKey("desc.gui.bishamonten.coordinates");
		fontRendererObj.drawString(dir, 80 * 2 - fontRendererObj.getStringWidth(dir) / 2, 20 * 2, 0);
		fontRendererObj.drawString(ele, 80 * 2 - fontRendererObj.getStringWidth(ele) / 2, 45 * 2, 0);
		fontRendererObj.drawString(per, 127 * 2 - fontRendererObj.getStringWidth(per) / 2, 14 * 2, 0);
		fontRendererObj.drawString(buf, 156 * 2 - fontRendererObj.getStringWidth(buf) / 2, 14 * 2, 0);
		fontRendererObj.drawString(pow, 174 * 2 - fontRendererObj.getStringWidth(pow) / 2, 14 * 2, 0);
		
		fontRendererObj.drawString(I18nUtil.resolveKey("twr.target", ""), 108 * 2, 87 * 2, color1);
		fontRendererObj.drawString(luna.getEntityName(), 108 * 2, 92 * 2, color1);
		fontRendererObj.drawString(cor, 108 * 2, 102 * 2, color1);
		fontRendererObj.drawString(luna.getEntityCoordinates(), 108 * 2, 107 * 2, color1);
		GL11.glScalef(scale, scale, 1F);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		displays[0].setNumber(luna.getDirection() > 0 ? luna.getDirection() : 360f + luna.getDirection());
//		displays[0].setNumber(luna.getDirection());
		displays[1].setNumber(luna.getElevation());
		displays[2].setNumber(luna.getLevel());

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int[] offsets = luna.getModeData();
		drawTexturedModalRect(guiLeft + 8, guiTop + offsets[0], 213, 90, 7, 7);
		drawTexturedModalRect(guiLeft + 19, guiTop + offsets[1], 219, 127, 37, 15);
		if (luna.isCharging())
			drawTexturedModalRect(guiLeft + 62, guiTop + 92, 219, 97, 37, 15);
		
		if (timer > 0)
		{
			timer--;
			drawTexturedModalRect(guiLeft + 108, guiTop + 59, 219, 112, 37, 15);
		}
		
		for (int i = 0; i < 3; i++)
			displays[i].drawNumber();
		
		int pow = (int) getScaledBar(luna.getPower(), 95, luna.getMaxPower());
		drawTexturedModalRect(guiLeft + 165, guiTop + 114 - pow, 238, 96 - pow, 18, pow);
		int buff = (int) getScaledBar(luna.getBuffer(), 95, TileEntityBishamonten.maxBuffer);
		drawTexturedModalRect(guiLeft + 147, guiTop + 114 - buff, 220, 96 - buff, 18, buff);
	}
	@Override
	protected void keyTyped(char c, int i)
	{
		if (tField.textboxKeyTyped(c, i))
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(keyboard, 1.0F));
		else
			super.keyTyped(c, i);
	}
}
