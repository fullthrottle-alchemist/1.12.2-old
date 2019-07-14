package com.mrphd.fta.handlers;

import com.mrphd.fta.init.EntityInit;
import com.mrphd.fta.init.ItemInit;
import com.mrphd.fta.init.SmeltingInit;
import com.mrphd.fta.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void registerItems(final Register<Item> event) {
		ItemInit.ITEMS.forEach(item -> {
			event.getRegistry().register(item);
		});
	}

	@SubscribeEvent
	public static void registerBlocks(final Register<Block> event) {

	}

	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		ItemInit.ITEMS.forEach(item -> {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		});
	}

	public static void registerSmelting() {
		SmeltingInit.RECIPES.forEach((input, output) -> {
			FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(input), output, 0);
		});
	}

	public static void preInitRegistries() {
		registerSmelting();
		EntityInit.registerEntities();
	}

}
