package com.hbm.handler;

import static com.hbm.main.MainRegistry.logger;

import java.lang.reflect.InvocationTargetException;

import com.hbm.util.EnumUtil;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		EnumGUI gui = EnumUtil.grabEnumSafely(EnumGUI.class, ID);
		if (gui.hasStandardBehavior() && gui.getContainer().isPresent())
		{
			try
			{
				return gui.getContainer().get().getConstructor(InventoryPlayer.class, gui.getTE()).newInstance(player.inventory, gui.getTE().cast(entity));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
				logger.error("Unable to open Container for device: " + gui.toString() + "! This should not be possible!");
				logger.catching(e);
			}
		}
		else if (gui.getCustomBehavior().isPresent() && !gui.hasStandardBehavior())
			return gui.getCustomBehavior().get().getContainer(entity, player, world, x, y, z);

		logger.warn("getServerGuiElement() executed without opening a container for the device: " + gui.toString() + "; is this ok?");
		return null;
		
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		EnumGUI gui = EnumUtil.grabEnumSafely(EnumGUI.class, ID);
		if (gui.hasStandardBehavior())
		{
			try
			{
				return gui.getGui().getConstructor(InventoryPlayer.class, gui.getTE()).newInstance(player.inventory, gui.getTE().cast(entity));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
				logger.error("Unable to open GUI for device: " + gui.toString() + "! This should be possible!");
				logger.catching(e);
			}
		}
		else if (gui.getCustomBehavior().isPresent())
			return gui.getCustomBehavior().get().getGUI(entity, player, world, x, y, z);
		
		logger.warn("getClientGuiElement() executed without opening a GUI for the device: " + gui.toString() + "; is this ok?");
		return null;
	}
}
