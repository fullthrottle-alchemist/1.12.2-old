package com.mrphd.fta.entity.render;

import com.mrphd.fta.entity.EntityColdCow;
import com.mrphd.fta.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderColdCow extends RenderLiving<EntityColdCow> {

	private static final ResourceLocation COLD = new ResourceLocation(Reference.MOD_ID+":textures/entity/cow/cold_cow.png");
	private static final ResourceLocation NORMAL = new ResourceLocation(Reference.MOD_ID+":textures/entity/cow/cow.png");

	public RenderColdCow(final RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCow(), 0.7F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(final EntityColdCow entity) {	
		return entity.getSheared() ? COLD : NORMAL;
	}
	
	
	
}
