package com.mrphd.fta.entity.render;

import com.mrphd.fta.entity.passive.EntityColdPig;
import com.mrphd.fta.util.Reference;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderColdPig extends RenderLiving<EntityColdPig> {

	private static final ResourceLocation COLD = new ResourceLocation(Reference.MOD_ID + ":textures/entity/pig/cold_pig.png");
	private static final ResourceLocation NORMAL = new ResourceLocation(Reference.MOD_ID + ":textures/entity/pig/pig.png");

	public RenderColdPig(final RenderManager manager) {
		super(manager, new ModelPig(), 0.7F);
		this.addLayer(new LayerSaddle(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityColdPig entity) {
		return entity.getSheared() ? COLD : NORMAL;
	}

	@SideOnly(Side.CLIENT)
	private static final class LayerSaddle implements LayerRenderer<EntityColdPig> {

		private static final ResourceLocation SADDLE = new ResourceLocation(Reference.MOD_ID + ":textures/entity/pig/pig_saddle.png");
		private final RenderColdPig pigRenderer;
		private final ModelPig pigModel = new ModelPig(0.5F);

		public LayerSaddle(final RenderColdPig pigRendererIn) {
			this.pigRenderer = pigRendererIn;
		}

		@Override
		public void doRenderLayer(final EntityColdPig entity, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
			if (entity.getSaddled()) {
				this.pigRenderer.bindTexture(SADDLE);
				this.pigModel.setModelAttributes(this.pigRenderer.getMainModel());
				this.pigModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}

	}

}
