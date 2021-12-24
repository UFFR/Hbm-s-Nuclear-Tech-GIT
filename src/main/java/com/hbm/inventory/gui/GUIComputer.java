package com.hbm.inventory.gui;

import static com.hbm.inventory.gui.GuiInfoContainer.*;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.hbm.lib.RefStrings;
import com.hbm.packet.AuxButtonPacket;
import com.hbm.packet.NBTControlPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.machine.computer.TileEntityComputer;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("deprecation")
public class GUIComputer extends GuiScreen
{
	private TileEntityComputer computer;
	private InventoryPlayer playerInv;
	private GuiTextField textIn;
	
	private static final ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/gui/machine/gui_computer.png");
	private static final short xSize = 216;
	private static final short ySize = 237;
	private static int guiLeft;
	private static int guiTop;
	public GUIComputer(InventoryPlayer inv, TileEntityComputer te)
	{
		playerInv = inv;
		computer = te;
	}
	
	@Override
	public void initGui()
	{
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		Keyboard.enableRepeatEvents(true);
		textIn = new GuiTextField(fontRendererObj, guiLeft + 10, guiTop + 205, 196, 11);
		textIn.setTextColor(color1);
		textIn.setDisabledTextColour(color1);
		textIn.setEnableBackgroundDrawing(true);
		textIn.setMaxStringLength(20);
		textIn.setEnabled(true);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		drawDefaultBackground();
		drawGUIBackground(mouseX, mouseY, f);
		GL11.glScalef(2, 2, 2);
		String inputText = textIn.getText();
		char c = getCursor();
		inputText = inputText.substring(0, textIn.getCursorPosition()) + c + inputText.substring(textIn.getCursorPosition(), inputText.length());
		fontRendererObj.drawString(inputText, 10, 205, color1);
		
		String[] toDisp = computer.kernel.getTextForDisp();
		for (byte i = 0; i < 10 && i < toDisp.length; i++)
			fontRendererObj.drawString(toDisp[i], 10, 100 + i * 5, color1);
		
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		drawCustomInfoStat(mouseX, mouseY, guiLeft + 29, guiTop + 218, 14, 14, mouseX, mouseY, "Computer power");
		drawCustomInfoStat(mouseX, mouseY, guiLeft + 29, guiTop + 226, 14, 14, mouseX, mouseY, "Program running");
	}
	
	public void drawCustomInfoStat(int mouseX, int mouseY, int x, int y, int width, int height, int tPosX, int tPosY, String... text) {
		
		if(x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY)
			this.func_146283_a(Arrays.asList(text), tPosX, tPosY);
	}

	
	@Override
	protected void mouseClicked(int mX, int mY, int i)
	{
		super.mouseClicked(mX, mY, i);
		if (getButtonBool(mY, mX, guiLeft + 7, guiTop + 218, 14, 14, guiLeft, guiTop))
			PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(computer));
	}
	
	protected void drawGUIBackground(int mX, int mY, float f)
	{
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (computer.isOn())
			drawTexturedModalRect(guiLeft + 29, guiTop + 218, 7, 236, 14, 14);
		
		if (computer.kernel.getCurrentProgram().isPresent())
			drawTexturedModalRect(guiLeft + 29, guiTop + 226, 7, 236, 14, 14);
		
		textIn.drawTextBox();
	}
	
	@Override
	protected void keyTyped(char c, int i)
	{
		mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(keyboard, 1.0F));
		if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode())
		{
			mc.thePlayer.closeScreen();
			return;
		}
		
		if (computer.kernel.getCurrentProgram().isPresent())
			computer.kernel.getCurrentProgram().get().keyTypeHook(c, i);
		else
		{
			if (i == Keyboard.KEY_NUMPADENTER || i == Keyboard.KEY_RETURN)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setString("command", textIn.getText());
				PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, computer));
			}
			textIn.textboxKeyTyped(c, i);
		}
		super.keyTyped(c, i);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
