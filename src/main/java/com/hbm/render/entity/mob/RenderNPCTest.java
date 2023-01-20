package com.hbm.render.entity.mob;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderNPCTest extends RenderBiped
{
	private final ResourceLocation texture;
	public RenderNPCTest(ResourceLocation texture, ModelBiped model, float f1, float f2)
	{
		super(model, f1, f2);
		this.texture = texture;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityLiving e)
	{
		return texture;
	}
	
	@Override
	public void doRender(EntityLiving e, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
    	
    	modelBipedMain.aimedBow = field_82423_g.aimedBow = field_82425_h.aimedBow = true;
    	super.doRender(e, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    	modelBipedMain.aimedBow = field_82423_g.aimedBow = field_82425_h.aimedBow = true;
    }

}
