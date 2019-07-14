package com.mrphd.fta.item.food;

import com.mrphd.fta.Main;
import com.mrphd.fta.init.ItemInit;
import com.mrphd.fta.ui.CreativeTab;
import com.mrphd.fta.util.IHasModel;

import net.minecraft.item.ItemFood;

public class ItemFoodBase extends ItemFood implements IHasModel {
	
	public ItemFoodBase(final String name, final int amount, final int saturation, final boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTab.FTA);
		ItemInit.ITEMS.add(this);
	}
	
	public ItemFoodBase(final String name, final int amount, final int saturation) {
		this(name, amount, saturation, false);
	}

	@Override
	public void registerModels() {
		Main.proxy.registerModelRenderer(this, 0, getUnlocalizedName());
	}
	
}
