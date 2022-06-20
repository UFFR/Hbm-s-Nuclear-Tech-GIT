package com.hbm.interfaces;

import java.util.List;

import com.hbm.render.util.RenderScreenOverlay.Crosshair;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMonocular
{
	public static final String KEY_ZOOM = "MONOCULAR_ZOOM", KEY_ENTITY = "TRACKED_ENTITY";
	public Crosshair getCrosshairType();
	public int maxZoom();
	public int getZoomLevel(ItemStack stack);
	public boolean isEnabled();
	public boolean hasNightVision();
	public boolean hasHUD();
	public boolean canTrack();
	public void setTargetedEntity(Entity e, ItemStack stack);
	public Entity getTargetedEntity(World world, ItemStack stack);
	public List<String> getAdditionalText();
}
