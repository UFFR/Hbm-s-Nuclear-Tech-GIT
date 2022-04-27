package com.hbm.render.tileentity;

import org.lwjgl.opengl.GL11;

import com.hbm.blocks.BlockDummyable;
import com.hbm.main.ResourceManager;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderSingGen extends TileEntitySpecialRenderer
{

	public RenderSingGen()
	{
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,	float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glScalef(2, 2, 2);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		switch (te.getBlockMetadata() - BlockDummyable.offset)
		{
		case 2:
			GL11.glRotatef(90, 0F, 1F, 0F);
			break;
		case 4:
			GL11.glRotatef(180, 0F, 1F, 0F);
			break;
		case 3:
			GL11.glRotatef(270, 0F, 1F, 0F);
			break;
		case 5:
			GL11.glRotatef(0, 0F, 1F, 0F);
			break;
		}
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		bindTexture(ResourceManager.sing_gen_tex);
		ResourceManager.sing_gen.renderAll();
		GL11.glShadeModel(GL11.GL_FLAT);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
