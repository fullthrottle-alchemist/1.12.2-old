package com.mrphd.fta.entity.render;

import com.mrphd.fta.entity.EntityColdChicken;
import com.mrphd.fta.util.Reference;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderColdChicken extends RenderLiving<EntityColdChicken> {

	private static final ResourceLocation COLD = new ResourceLocation(Reference.MOD_ID + ":textures/entity/chicken/cold_chicken.png");
	private static final ResourceLocation NORMAL = new ResourceLocation(Reference.MOD_ID + ":textures/entity/chicken/chicken.png");

	public RenderColdChicken(final RenderManager manager) {
		super(manager, new ModelChicken(), 0.3F);
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityColdChicken entity) {
		return entity.getSheared() ? COLD : NORMAL;
	}

	@Override
	protected float handleRotationFloat(final EntityColdChicken livingBase, final float partialTicks) {
		float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
		float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
		return (MathHelper.sin(f) + 1.0F) * f1;
	}

}
