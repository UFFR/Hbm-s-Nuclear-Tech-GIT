package com.hbm.inventory.gui;

import static com.hbm.inventory.gui.GuiInfoContainer.*;

import com.google.common.annotations.Beta;
import com.hbm.lib.RefStrings;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
@Beta
public class GUIScreenCalculator extends GuiScreen
{
	public static final ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/gui/gui_calculator");
	private int guiLeft, guiTop;
	private StringBuffer inputBuffer = new StringBuffer();
	
	private static final char[][] keys = 
		{
				{'^', '(', ')', '/'},
				{'7', '8', '9', 'x'},
				{'4', '5', '6', '-'},
				{'1', '2', '3', '+'},
				{'0', '.', '–', '='}
		};
	
	public GUIScreenCalculator()
	{
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initGui()
	{
		// TODO Auto-generated method stub
	}
}
