package com.mrphd.fta.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SmeltingInit {

	public static final Map<Item, ItemStack> RECIPES = new HashMap<Item, ItemStack>();

	private static void registerSmeltingRecipe(final Item input, final Item output, final int count) {
		RECIPES.put(input, new ItemStack(output, count));
	}

	static {
		registerSmeltingRecipe(ItemInit.BACON_STRIP, ItemInit.COOKED_BACON_STRIP, 1);
	}

}
