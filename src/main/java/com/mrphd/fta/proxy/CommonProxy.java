package com.mrphd.fta.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void onPreInit(final FMLPreInitializationEvent event) {}
	public void onInit(final FMLInitializationEvent event) {}
	public void onPostInit(final FMLPostInitializationEvent event) {}
	
	public void registerModelRenderer(Item item, int meta, String id) {}
	
}
