package com.mrphd.fta.proxy;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void onPreInit(final FMLPreInitializationEvent event) {}
	public void onInit(final FMLInitializationEvent event) {}
	public void onPostInit(final FMLPostInitializationEvent event) {}
	
	public void registerModelRenderer(Item item, int meta, String id) {}
	
}
