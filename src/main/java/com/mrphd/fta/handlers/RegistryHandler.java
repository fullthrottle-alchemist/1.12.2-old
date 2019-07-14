package com.mrphd.fta.handlers;

import com.mrphd.fta.init.EntityInit;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void registerItems(final Register<Item> event) {
		
	}
	
	@SubscribeEvent
	public static void registerBlocks(final Register<Block> event) {
		
	}
	
	public static void preInitRegistries() {
		EntityInit.registerEntities();
	}
	
}
