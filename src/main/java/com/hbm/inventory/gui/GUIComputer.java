package com.hbm.inventory.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.hbm.inventory.container.ContainerComputer;
import com.hbm.lib.RefStrings;
import com.hbm.packet.AuxButtonPacket;
import com.hbm.packet.NBTControlPacket;
import com.hbm.packet.PacketDispatcher;
import com.hbm.tileentity.machine.computer.TileEntityComputer;
import com.hbm.util.I18nUtil;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("deprecation")
public class GUIComputer extends GuiInfoContainer
{
	private TileEntityComputer computer;
	private InventoryPlayer playerInv;
	private GuiTextField textInput;
	public static final byte maxRows = 36, maxColumns = 64;
	private static final ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/gui/machine/gui_computer.png");
	private int prevCommandIndex = 0;
//	private final FontRenderer consoleRenderer;
	public GUIComputer(InventoryPlayer inv, TileEntityComputer te)
	{
		super(new ContainerComputer(inv, te));
		playerInv = inv;
		computer = te;
		xSize = 216;
		ySize = 236;
//		consoleRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation(RefStrings.MODID, "textures/font/ascii.png"), mc.getTextureManager(), false);
		componentList.add(new GUIComponent(7, 218, 14, 14).setClickAction(gui -> PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(computer))));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		textInput = new GuiTextField(fontRendererObj, guiLeft + 9, guiTop + 206, 198, 10);
		textInput.setTextColor(-1);
		textInput.setDisabledTextColour(-1);
		textInput.setEnableBackgroundDrawing(false);
		textInput.setMaxStringLength(128);
		textInput.setFocused(true);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		super.drawScreen(mouseX, mouseY, f);
//		GL11.glScalef(2, 2, 2);
//		
//		GL11.glScalef(0.5f, 0.5f, 0.5f);
		String[] labels = I18nUtil.resolveKeyArray("desc.gui.computer");
		for (byte i = 0; i < labels.length; i++)
			drawCustomInfoStat(mouseX, mouseY, guiLeft + 29 + (i * 15), guiTop + 221, 14, 14, mouseX, mouseY, labels[i]);
//		drawCustomInfoStat(mouseX, mouseY, guiLeft + 29, guiTop + 218, 14, 14, mouseX, mouseY, "Computer power");
//		drawCustomInfoStat(mouseX, mouseY, guiLeft + 29, guiTop + 226, 14, 14, mouseX, mouseY, "Program running");
	}
	
	@Override
	protected void mouseClicked(int mX, int mY, int i)
	{
		super.mouseClicked(mX, mY, i);
		defaultClickHandling(mX, mY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		
		String currText = textInput.getText();
		char cursor = getCursor(blockCursor);
		if (textInput.isFocused())
			currText = currText.substring(0, textInput.getCursorPosition()) + cursor + currText.substring(textInput.getCursorPosition(), currText.length());
		List<String> consoleText = computer.getConsoleText(maxRows, maxColumns);
		for (int i = 0; i < consoleText.size(); i++)
			fontRendererObj.drawString(consoleText.get(i), 12 * 2, (20 + (i * 5)) * 2, color1);
		
		GL11.glScalef(2, 2, 2);
		
		fontRendererObj.drawString('>' + currText, 12, 206, color1);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		boolean[] statuses = computer.getStatuses();
		for (byte i = 0; i < statuses.length; i++)
			if (statuses[i])
				drawTexturedModalRect(guiLeft + 29 + (i * 15), guiTop + 221, 7, 236, 14, 14);
	}
	
	@Override
	protected void keyTyped(char c, int i)
	{
		mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(keyboard, 1.0F));
		if (computer.getCurrentProgram().isPresent() && !computer.getCurrentProgram().get().requestingConsoleInput())
			PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(computer, c, i));
		else
		{
			List<String> prevCommands = computer.getPreviousCommands();
			switch (i)
			{
			case 1:
				super.keyTyped(c, i);
				return;
			case Keyboard.KEY_UP:
				if (prevCommandIndex > 0)
					prevCommandIndex--;
				if (!prevCommands.isEmpty())
					textInput.setText(prevCommands.get(prevCommandIndex));
				break;
			case Keyboard.KEY_DOWN:
				if (!(prevCommandIndex >= prevCommands.size() - 1))
					prevCommandIndex++;
				else
					prevCommandIndex = prevCommands.size();
				if (!prevCommands.isEmpty() && !(prevCommandIndex >= prevCommands.size()))
					textInput.setText(prevCommands.get(prevCommandIndex));
				else
					textInput.setText("");
				break;
			case Keyboard.KEY_NUMPADENTER:
			case Keyboard.KEY_RETURN:
				NBTTagCompound data = new NBTTagCompound();
				data.setString("TYPE", "COMMAND");
				data.setString("command", textInput.getText());
				PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, computer));
				textInput.setText("");
				prevCommandIndex = prevCommands.size();
				return;
			default:
				textInput.textboxKeyTyped(c, i);
				break;
			}
		}
	}
}
