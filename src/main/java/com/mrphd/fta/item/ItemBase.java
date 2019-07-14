package com.mrphd.fta.item;

import com.mrphd.fta.Main;
import com.mrphd.fta.init.ItemInit;
import com.mrphd.fta.ui.CreativeTab;
import com.mrphd.fta.util.IHasModel;

import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(final String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTab.FTA);
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerModelRenderer(this, 0, getUnlocalizedName());
	}
	
}
