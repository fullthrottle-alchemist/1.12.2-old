package com.mrphd.fta.handlers;

import com.mrphd.fta.entity.passive.EntityColdChicken;
import com.mrphd.fta.entity.passive.EntityColdCow;
import com.mrphd.fta.entity.passive.EntityColdPig;
import com.mrphd.fta.entity.render.RenderColdChicken;
import com.mrphd.fta.entity.render.RenderColdCow;
import com.mrphd.fta.entity.render.RenderColdPig;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityColdChicken.class, new IRenderFactory<EntityColdChicken>() {
			@Override
			public Render<EntityColdChicken> createRenderFor(final RenderManager manager) {
				return new RenderColdChicken(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityColdCow.class, new IRenderFactory<EntityColdCow>() {
			@Override
			public Render<EntityColdCow> createRenderFor(final RenderManager manager) {
				return new RenderColdCow(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityColdPig.class, new IRenderFactory<EntityColdPig>() {
			@Override
			public Render<EntityColdPig> createRenderFor(final RenderManager manager) {
				return new RenderColdPig(manager);
			}
		});
	}

}
