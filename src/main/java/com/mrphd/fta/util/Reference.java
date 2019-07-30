package com.mrphd.fta.util;

import net.minecraft.util.ResourceLocation;

public class Reference {

	public static final String MOD_ID = "ftalchemist";
	public static final String NAME = "FullThrottle Alchemist";
	public static final String VERSION = "1.0.0";
	public static final String CLIENT_PROXY_CLASS = "com.mrphd.fta.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.mrphd.fta.proxy.CommonProxy";
	
	public static ResourceLocation location(final String path) {
		return new ResourceLocation(MOD_ID + ":" + path);
	}
	
}
