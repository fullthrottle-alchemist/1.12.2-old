package com.mrphd.fta.proxy;

import com.mrphd.fta.util.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void onInit(final FMLInitializationEvent event) {
		
	}
	
	@Override
	public void registerModelRenderer(Item item, int meta, String id) {	
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
}
