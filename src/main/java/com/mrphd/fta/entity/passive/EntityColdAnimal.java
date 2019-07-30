package com.mrphd.fta.entity.passive;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityColdAnimal extends EntityAnimal implements IShearable {
	
	private static final int GRASS_ANIM_TIME = 40;

	protected static final DataParameter<Boolean> COLD = EntityDataManager.<Boolean>createKey(EntityColdAnimal.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> PANICKED = EntityDataManager.<Boolean>createKey(EntityColdAnimal.class, DataSerializers.BOOLEAN);

	protected EntityAIEatGrass entityAIEatGrass;
	protected EntityAIPanic entityAIPanic;
	protected int grassTimer;

	public EntityColdAnimal(final World world) {
		super(world);
	}
	
	@Override
	protected void entityInit() {		
		super.entityInit();
		this.dataManager.register(COLD, Boolean.valueOf(false));
		this.dataManager.register(PANICKED, Boolean.valueOf(false));
	}

	public boolean getSheared() {
		return this.dataManager.get(COLD).booleanValue();
	}

	public void setSheared(final boolean sheared) {
		this.dataManager.set(COLD, Boolean.valueOf(sheared));
	}

	public boolean isPanicked() {
		return this.dataManager.get(PANICKED).booleanValue();
	}

	public void setPanicked(final boolean panicked) {
		this.dataManager.set(PANICKED, Boolean.valueOf(panicked));
	}

	@Override
	protected void initEntityAI() {
		this.entityAIEatGrass = new EntityAIEatGrass(this);
		this.entityAIPanic = new EntityAIPanic(this);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		this.entityAIEatGrass.updateTask();
		this.entityAIPanic.updateTask();
		this.grassTimer = this.entityAIEatGrass.getEatingGrassTimer();
	}

	@Override
	public void onLivingUpdate() {
		if (this.world.isRemote) {
			this.grassTimer = Math.max(0, this.grassTimer - 1);
		}
		super.onLivingUpdate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(final byte id) {
		switch (id) {
		case 10:
			this.grassTimer = GRASS_ANIM_TIME;
			break;
		default:
			super.handleStatusUpdate(id);
		}
	}

	@SideOnly(Side.CLIENT)
	public float getHeadRotationPointY(final float angle) {
		if (this.grassTimer <= 0) {
			return 0.0F;
		} else if (this.grassTimer >= 4 && this.grassTimer <= 36) {
			return 1.0F;
		} else {
			return this.grassTimer < 4 ? ((float) this.grassTimer - angle) / 4.0F : -((float) (this.grassTimer - GRASS_ANIM_TIME) - angle) / 4.0F;
		}
	}

	@SideOnly(Side.CLIENT)
	public float getHeadRotationAngleX(final float angle) {
		if (this.grassTimer > 4 && this.grassTimer <= 36) {
			float f = ((float) (this.grassTimer - 4) - angle) / 32.0F;
			return ((float) Math.PI / 5F) + ((float) Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
		} else {
			return this.grassTimer > 0 ? ((float) Math.PI / 5F) : this.rotationPitch * 0.017453292F;
		}
	}

	public void readEntityFromNBT(final NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setSheared(compound.getBoolean("Sheared"));
	}

	public void writeEntityToNBT(final NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("Sheared", this.getSheared());
	}

	@Override
	public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
		final ItemStack itemstack = player.getHeldItem(hand);

		if (!itemstack.isEmpty()) {
			if (!this.isChild() && itemstack.getItem() instanceof ItemShears) {
				itemstack.interactWithEntity(player, this, hand);
			}
		}

		return super.processInteract(player, hand);
	}

	public void eatGrassBonus() {
		this.setSheared(false);

		if (this.isChild()) {
			this.addGrowth(60);
		}
	}

	/////////////////// MUST override abstract classes ///////////////////

	protected abstract SoundEvent getAmbientSound();

	protected abstract SoundEvent getHurtSound(final DamageSource damageSourceIn);

	protected abstract SoundEvent getDeathSound();

	protected abstract void playStepSound(final BlockPos pos, final Block blockIn);

	/////////////////// EntityAnimal method override ///////////////////

	@Override
	public abstract EntityAgeable createChild(final EntityAgeable ageable);

	/////////////////// IShearable method override ///////////////////

	@Override
	public boolean isShearable(final ItemStack item, final IBlockAccess world, final BlockPos pos) {
		return !this.getSheared() && !this.isChild();
	}

	@Override
	public List<ItemStack> onSheared(final ItemStack item, final IBlockAccess world, final BlockPos pos, final int fortune) {
		this.setSheared(true);
		this.setPanicked(false);
		this.playSound(this.getHurtSound(null), 1.0F, 1.0F);
		return Lists.newArrayList();
	}

	/////////////////// Panic on shear AI class ///////////////////

	private final class EntityAIPanic extends EntityAIBase {

		protected final EntityCreature creature;
		protected double speed;
		protected double randPosX;
		protected double randPosY;
		protected double randPosZ;

		private EntityAIPanic(final EntityCreature creature, final double speedIn) {
			this.creature = creature;
			this.speed = speedIn;
			this.setMutexBits(1);
		}

		public EntityAIPanic(final EntityCreature creature) {
			this(creature, 1.8D);
		}

		@Override
		public boolean shouldExecute() {
			if (this.creature instanceof EntityColdAnimal) {
				final EntityColdAnimal coldAnimal = (EntityColdAnimal) this.creature;
				if (coldAnimal.getSheared() && !coldAnimal.isPanicked()) {
					BlockPos blockpos = this.getRandPos(this.creature.world, this.creature, 5, 4);

					if (blockpos != null) {
						this.randPosX = (double) blockpos.getX();
						this.randPosY = (double) blockpos.getY();
						this.randPosZ = (double) blockpos.getZ();
						return true;
					}

					coldAnimal.setPanicked(true);
					return this.findRandomPosition();
				}
			}
			if (this.creature.getRevengeTarget() == null && !this.creature.isBurning()) {
				return false;
			} else {
				if (this.creature.isBurning()) {
					BlockPos blockpos = this.getRandPos(this.creature.world, this.creature, 5, 4);

					if (blockpos != null) {
						this.randPosX = (double) blockpos.getX();
						this.randPosY = (double) blockpos.getY();
						this.randPosZ = (double) blockpos.getZ();
						return true;
					}
				}

				return this.findRandomPosition();
			}
		}

		protected boolean findRandomPosition() {
			Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);

			if (vec3d == null) {
				return false;
			} else {
				this.randPosX = vec3d.x;
				this.randPosY = vec3d.y;
				this.randPosZ = vec3d.z;
				return true;
			}
		}

		public void startExecuting() {
			this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
		}

		public boolean shouldContinueExecuting() {
			return !this.creature.getNavigator().noPath();
		}

		@Nullable
		private BlockPos getRandPos(final World worldIn, final Entity entityIn, final int horizontalRange, final int verticalRange) {
			BlockPos blockpos = new BlockPos(entityIn);
			int i = blockpos.getX();
			int j = blockpos.getY();
			int k = blockpos.getZ();
			float f = (float) (horizontalRange * horizontalRange * verticalRange * 2);
			BlockPos blockpos1 = null;
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int l = i - horizontalRange; l <= i + horizontalRange; ++l) {
				for (int i1 = j - verticalRange; i1 <= j + verticalRange; ++i1) {
					for (int j1 = k - horizontalRange; j1 <= k + horizontalRange; ++j1) {
						blockpos$mutableblockpos.setPos(l, i1, j1);
						IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);

						if (iblockstate.getMaterial() == Material.WATER) {
							float f1 = (float) ((l - i) * (l - i) + (i1 - j) * (i1 - j) + (j1 - k) * (j1 - k));

							if (f1 < f) {
								f = f1;
								blockpos1 = new BlockPos(blockpos$mutableblockpos);
							}
						}
					}
				}
			}

			return blockpos1;
		}

	}

}
