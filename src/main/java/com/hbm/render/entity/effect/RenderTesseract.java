package com.hbm.render.entity.effect;

import org.lwjgl.opengl.GL11;

import com.hbm.entity.effect.EntityTesseract;
import com.hbm.main.ResourceManager;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTesseract extends Render 
{

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float interp)
	{
		// TODO Auto-generated method stub
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		ResourceManager.tesseract.renderAll();
		EntityTesseract tesseract = (EntityTesseract) entity;
		
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		// TODO Auto-generated method stub
		return null;
	}

}
