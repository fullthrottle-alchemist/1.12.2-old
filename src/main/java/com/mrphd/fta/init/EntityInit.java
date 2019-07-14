package com.mrphd.fta.init;

import java.util.Random;

import com.mrphd.fta.Main;
import com.mrphd.fta.entity.EntityColdChicken;
import com.mrphd.fta.entity.EntityColdCow;
import com.mrphd.fta.entity.EntityColdPig;
import com.mrphd.fta.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {

	public static final int COLD_CHICKEN_ID = 0;
	public static final int COLD_COW_ID = 1;
	public static final int COLD_PIG_ID = 2;

	private static Random random = new Random();
	
	public static void registerEntities() {
		EntityInit.registerEntity("cold_chicken", COLD_CHICKEN_ID, 50, EntityColdChicken.class, false);
		EntityInit.registerEntity("cold_cow", COLD_COW_ID, 50, EntityColdCow.class, false);
		EntityInit.registerEntity("cold_pig", COLD_PIG_ID, 50, EntityColdPig.class, false);
	}

	private static void registerEntity(final String name, final int id, final int range, final Class<? extends Entity> clazz, final boolean hasEgg) {
		if(hasEgg) EntityRegistry.registerModEntity(
			new ResourceLocation(Reference.MOD_ID + ":" + name), 
			clazz, 
			name, 
			id, 
			Main.instance, 
			range, 
			1, 
			true, 
			random.nextInt()&0xf0f0f0, 
			random.nextInt()^0xf0f0f0
		);
		else EntityRegistry.registerModEntity(
			new ResourceLocation(Reference.MOD_ID + ":" + name), 
			clazz, 
			name, 
			id, 
			Main.instance, 
			range, 
			1, 
			true
		);
	}

}
