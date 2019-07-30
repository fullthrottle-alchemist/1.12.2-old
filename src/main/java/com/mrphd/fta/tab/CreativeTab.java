package com.mrphd.fta.tab;

import com.mrphd.fta.init.ItemInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {

	public static final CreativeTabs FTA = new CreativeTabs("FTA") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemInit.ITEMS.get(0));
		}
	};
	
}
