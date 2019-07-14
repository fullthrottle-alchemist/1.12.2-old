package com.mrphd.fta.entity;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityColdChicken extends EntityColdAnimal {

	private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);

	public float wingRotation, destPos, oFlapSpeed, oFlap, wingRotDelta = 1.0F;
	public int timeUntilNextEgg;
	public boolean chickenJockey;

	public EntityColdChicken(final World world) {
		super(world);
		this.setSize(0.4F, 0.7F);
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		this.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, this.entityAIPanic);
		this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(3, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(5, this.entityAIEatGrass);
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	public float getEyeHeight() {
		return this.height;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.oFlap = this.wingRotation;
		this.oFlapSpeed = this.destPos;
		this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
		this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

		if (!this.onGround && this.wingRotDelta < 1.0F) {
			this.wingRotDelta = 1.0F;
		}

		this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);

		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		this.wingRotation += this.wingRotDelta * 2.0F;

		if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0) {
			this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Items.EGG, 1);
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		}
	}

	@Override
	public void fall(final float distance, final float damageMultiplier) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CHICKEN_DEATH;
	}

	@Override
	protected void playStepSound(final BlockPos pos, final Block blockIn) {
		this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_CHICKEN;
	}

	@Override
	public EntityColdChicken createChild(final EntityAgeable ageable) {
		return new EntityColdChicken(this.world);
	}

	@Override
	public boolean isBreedingItem(final ItemStack stack) {
		return TEMPTATION_ITEMS.contains(stack.getItem());
	}

	@Override
	protected int getExperiencePoints(final EntityPlayer player) {
		return this.isChickenJockey() ? 10 : super.getExperiencePoints(player);
	}

	public static void registerFixesChicken(final DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityColdChicken.class);
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.chickenJockey = compound.getBoolean("IsChickenJockey");
		if (compound.hasKey("EggLayTime")) {
			this.timeUntilNextEgg = compound.getInteger("EggLayTime");
		}
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("IsChickenJockey", this.chickenJockey);
		compound.setInteger("EggLayTime", this.timeUntilNextEgg);
	}

	@Override
	protected boolean canDespawn() {
		return this.isChickenJockey() && !this.isBeingRidden();
	}

	@Override
	public void updatePassenger(final Entity passenger) {
		super.updatePassenger(passenger);
		float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
		float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
		float f2 = 0.1F;
		float f3 = 0.0F;
		passenger.setPosition(this.posX + (double) (0.1F * f), this.posY + (double) (this.height * 0.5F) + passenger.getYOffset() + 0.0D, this.posZ - (double) (0.1F * f1));

		if (passenger instanceof EntityLivingBase) {
			((EntityLivingBase) passenger).renderYawOffset = this.renderYawOffset;
		}
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		final List<ItemStack> ret =  super.onSheared(item, world, pos, fortune);
		ret.add(new ItemStack(Items.FEATHER, (int)(Math.random()*2 + 1)));
		return ret;
	}

	public boolean isChickenJockey() {
		return this.chickenJockey;
	}

	public void setChickenJockey(final boolean jockey) {
		this.chickenJockey = jockey;
	}

}
