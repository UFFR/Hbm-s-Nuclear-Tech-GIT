package com.hbm.render.item.weapon;

import org.lwjgl.opengl.GL11;

import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ItemRenderM2 extends ItemRenderBase
{
	public ItemRenderM2()
	{
	}
	static final float scale1 = 0.35f,
					   scale2 = 0.25f,
					   scale3 = 0.25f,
					   scale4 = 0.75f;
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
//		super.renderItem(type, item, data);
		switch (type)
		{
			case ENTITY:
				GL11.glScalef(scale4, scale4, scale4); break;
			case EQUIPPED:
				GL11.glScalef(scale1, scale1, scale1);
//				GL11.glRotatef(90, 1, 0, 0);
//				GL11.glRotatef(90, 0, 1, 0);
//				GL11.glRotatef(90, 0, 0, 1);
				
				break;
			default: break;
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceManager.m2_tex);
		ResourceManager.m2.renderAll();
	}
}
