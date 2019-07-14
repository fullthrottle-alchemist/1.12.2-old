package com.mrphd.fta;

import org.apache.logging.log4j.Logger;

import com.mrphd.fta.handlers.RegistryHandler;
import com.mrphd.fta.handlers.RenderHandler;
import com.mrphd.fta.proxy.CommonProxy;
import com.mrphd.fta.util.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Reference.MOD_ID,name=Reference.NAME,version=Reference.VERSION)
public class Main {

	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS,serverSide=Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public static Logger logger;
	
	@EventHandler
	public static void preInit(final FMLPreInitializationEvent event) {
		logger = event.getModLog();
		RegistryHandler.preInitRegistries();
		RenderHandler.registerRenders();
		proxy.onPreInit(event);
	}
	
	@EventHandler
	public static void init(final FMLInitializationEvent event) {
		proxy.onInit(event);
	}
	
	@EventHandler
	public static void postInit(final FMLPostInitializationEvent event) {
		proxy.onPostInit(event);
	}
	
}
