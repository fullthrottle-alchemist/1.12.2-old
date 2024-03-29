package com.mrphd.fta.init;

import java.util.ArrayList;
import java.util.List;

import com.mrphd.fta.item.Flask;
import com.mrphd.fta.item.food.ItemBaconStrip;
import com.mrphd.fta.item.food.ItemCookedBaconStrip;
import com.mrphd.fta.item.vellum.VellumBase;

import net.minecraft.item.Item;

public class ItemInit {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item BACON_STRIP = new ItemBaconStrip();
	public static final Item COOKED_BACON_STRIP = new ItemCookedBaconStrip();
	
	public static final VellumBase VELLUM = new VellumBase("vellum");
	
	public static final Item FLASK = new Flask();
	
}
