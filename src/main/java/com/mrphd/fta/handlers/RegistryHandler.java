package com.mrphd.fta.handlers;

import com.mrphd.fta.Main;
import com.mrphd.fta.init.BlockInit;
import com.mrphd.fta.init.EntityInit;
import com.mrphd.fta.init.ItemInit;
import com.mrphd.fta.init.SmeltingInit;
import com.mrphd.fta.tileentity.TileEntityAtelier;
import com.mrphd.fta.util.IHasModel;
import com.mrphd.fta.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		BlockInit.BLOCKS.forEach(block -> {
			event.getRegistry().register(block);
		});
	}

	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		ItemInit.ITEMS.forEach(item -> {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		});
		BlockInit.BLOCKS.forEach(block -> {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
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

		registerGUIs();
	}

	private static void registerGUI(final Class<? extends TileEntity> clazz, final String name) {
		GameRegistry.registerTileEntity(clazz, new ResourceLocation(Reference.MOD_ID + ":" + name));
	}

	public static void registerGUIs() {
		registerGUI(TileEntityAtelier.class, "ftalchemist:gui_atelier");

		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
	}

}
