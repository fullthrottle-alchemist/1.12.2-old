package com.mrphd.fta.handlers;

import com.mrphd.fta.entity.passive.EntityColdChicken;
import com.mrphd.fta.entity.passive.EntityColdCow;
import com.mrphd.fta.entity.passive.EntityColdPig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventHandler {

	private static void OnLivingSpawnOrEnterEvent(final Event event, final World world, final Entity entity) {
		if(event == null || world == null || world.isRemote || entity == null) return;
		if(	entity instanceof EntityChicken ||
			entity instanceof EntityCow || 
			entity instanceof EntityPig) {
			final EntityAnimal animal = (EntityAnimal) entity;
			if(animal instanceof EntityChicken) {
				final EntityChicken chicken = (EntityChicken) animal;
				final EntityColdChicken newChicken = new EntityColdChicken(world);
				newChicken.setLocationAndAngles(chicken.posX, chicken.posY, chicken.posZ, chicken.rotationPitch, chicken.rotationYaw);
				world.spawnEntity(newChicken);
			}else if(animal instanceof EntityCow && !(animal instanceof EntityMooshroom)) {
				final EntityCow cow = (EntityCow) animal;
				final EntityColdCow newCow = new EntityColdCow(world);
				newCow.setLocationAndAngles(cow.posX, cow.posY, cow.posZ, cow.rotationPitch, cow.rotationYaw);
				world.spawnEntity(newCow);
			}else if(animal instanceof EntityPig) {
				final EntityPig pig = (EntityPig) animal;
				final EntityColdPig newPig = new EntityColdPig(world);
				newPig.setLocationAndAngles(pig.posX, pig.posY, pig.posZ, pig.rotationPitch, pig.rotationYaw);
				world.spawnEntity(newPig);
			}
			else return;
			if(event.isCancelable()) event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void OnEntityEnterWorld(final LivingSpawnEvent event) {
		OnLivingSpawnOrEnterEvent(event, event.getWorld(), event.getEntity());
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onEntityJoinWorld(final EntityJoinWorldEvent event) {
		OnLivingSpawnOrEnterEvent(event, event.getWorld(), event.getEntity());
	}
	
}
