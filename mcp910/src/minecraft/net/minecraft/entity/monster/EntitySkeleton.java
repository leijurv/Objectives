package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
	
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
	private static final String __OBFID = "CL_00001697";
	
	public EntitySkeleton(World worldIn) {
		super(worldIn);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(2, this.field_175455_a);
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, new Predicate() {
			
			private static final String __OBFID = "CL_00002203";
			
			public boolean func_179945_a(Entity p_179945_1_) {
				return p_179945_1_ instanceof EntityWolf;
			}
			
			public boolean apply(Object p_apply_1_) {
				return this.func_179945_a((Entity) p_apply_1_);
			}
		}, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
		
		if (worldIn != null && !worldIn.isRemote) {
			this.setCombatTask();
		}
	}
	
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}
	
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(13, new Byte((byte) 0));
	}
	
	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}
	
	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}
	
	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.skeleton.death";
	}
	
	protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
	}
	
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		if (super.attackEntityAsMob(p_70652_1_)) {
			if (this.getSkeletonType() == 1 && p_70652_1_ instanceof EntityLivingBase) {
				((EntityLivingBase) p_70652_1_).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}
	
	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			float var1 = this.getBrightness(1.0F);
			BlockPos var2 = new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);
			
			if (var1 > 0.5F && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F && this.worldObj.isAgainstSky(var2)) {
				boolean var3 = true;
				ItemStack var4 = this.getEquipmentInSlot(4);
				
				if (var4 != null) {
					if (var4.isItemStackDamageable()) {
						var4.setItemDamage(var4.getItemDamage() + this.rand.nextInt(2));
						
						if (var4.getItemDamage() >= var4.getMaxDamage()) {
							this.renderBrokenItemStack(var4);
							this.setCurrentItemOrArmor(4, (ItemStack) null);
						}
					}
					
					var3 = false;
				}
				
				if (var3) {
					this.setFire(8);
				}
			}
		}
		
		if (this.worldObj.isRemote && this.getSkeletonType() == 1) {
			this.setSize(0.72F, 2.535F);
		}
		
		super.onLivingUpdate();
	}
	
	/**
	 * Handles updating while being ridden by an entity
	 */
	public void updateRidden() {
		super.updateRidden();
		
		if (this.ridingEntity instanceof EntityCreature) {
			EntityCreature var1 = (EntityCreature) this.ridingEntity;
			this.renderYawOffset = var1.renderYawOffset;
		}
	}
	
	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		
		if (cause.getSourceOfDamage() instanceof EntityArrow && cause.getEntity() instanceof EntityPlayer) {
			EntityPlayer var2 = (EntityPlayer) cause.getEntity();
			double var3 = var2.posX - this.posX;
			double var5 = var2.posZ - this.posZ;
			
			if (var3 * var3 + var5 * var5 >= 2500.0D) {
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		} else if (cause.getEntity() instanceof EntityCreeper && ((EntityCreeper) cause.getEntity()).getPowered() && ((EntityCreeper) cause.getEntity()).isAIEnabled()) {
			((EntityCreeper) cause.getEntity()).func_175493_co();
			this.entityDropItem(new ItemStack(Items.skull, 1, this.getSkeletonType() == 1 ? 1 : 0), 0.0F);
		}
	}
	
	protected Item getDropItem() {
		return Items.arrow;
	}
	
	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int var3;
		int var4;
		
		if (this.getSkeletonType() == 1) {
			var3 = this.rand.nextInt(3 + p_70628_2_) - 1;
			
			for (var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Items.coal, 1);
			}
		} else {
			var3 = this.rand.nextInt(3 + p_70628_2_);
			
			for (var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Items.arrow, 1);
			}
		}
		
		var3 = this.rand.nextInt(3 + p_70628_2_);
		
		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Items.bone, 1);
		}
	}
	
	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor() {
		if (this.getSkeletonType() == 1) {
			this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
	}
	
	protected void func_180481_a(DifficultyInstance p_180481_1_) {
		super.func_180481_a(p_180481_1_);
		this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}
	
	public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
		p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
		
		if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0) {
			this.tasks.addTask(4, this.aiAttackOnCollide);
			this.setSkeletonType(1);
			this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
		} else {
			this.tasks.addTask(4, this.aiArrowAttack);
			this.func_180481_a(p_180482_1_);
			this.func_180483_b(p_180482_1_);
		}
		
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * p_180482_1_.func_180170_c());
		
		if (this.getEquipmentInSlot(4) == null) {
			Calendar var3 = this.worldObj.getCurrentDate();
			
			if (var3.get(2) + 1 == 10 && var3.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
				this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}
		
		return p_180482_2_;
	}
	
	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask() {
		this.tasks.removeTask(this.aiAttackOnCollide);
		this.tasks.removeTask(this.aiArrowAttack);
		ItemStack var1 = this.getHeldItem();
		
		if (var1 != null && var1.getItem() == Items.bow) {
			this.tasks.addTask(4, this.aiArrowAttack);
		} else {
			this.tasks.addTask(4, this.aiAttackOnCollide);
		}
	}
	
	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		EntityArrow var3 = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, (float) (14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
		int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int var5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
		var3.setDamage((double) (p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.getDifficulty().getDifficultyId() * 0.11F));
		
		if (var4 > 0) {
			var3.setDamage(var3.getDamage() + (double) var4 * 0.5D + 0.5D);
		}
		
		if (var5 > 0) {
			var3.setKnockbackStrength(var5);
		}
		
		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1) {
			var3.setFire(100);
		}
		
		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(var3);
	}
	
	/**
	 * Return this skeleton's type.
	 */
	public int getSkeletonType() {
		return this.dataWatcher.getWatchableObjectByte(13);
	}
	
	/**
	 * Set this skeleton's type.
	 */
	public void setSkeletonType(int p_82201_1_) {
		this.dataWatcher.updateObject(13, Byte.valueOf((byte) p_82201_1_));
		this.isImmuneToFire = p_82201_1_ == 1;
		
		if (p_82201_1_ == 1) {
			this.setSize(0.72F, 2.535F);
		} else {
			this.setSize(0.6F, 1.95F);
		}
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);
		
		if (tagCompund.hasKey("SkeletonType", 99)) {
			byte var2 = tagCompund.getByte("SkeletonType");
			this.setSkeletonType(var2);
		}
		
		this.setCombatTask();
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		tagCompound.setByte("SkeletonType", (byte) this.getSkeletonType());
	}
	
	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {
		super.setCurrentItemOrArmor(slotIn, itemStackIn);
		
		if (!this.worldObj.isRemote && slotIn == 0) {
			this.setCombatTask();
		}
	}
	
	public float getEyeHeight() {
		return this.getSkeletonType() == 1 ? super.getEyeHeight() : 1.74F;
	}
	
	/**
	 * Returns the Y Offset of this entity.
	 */
	public double getYOffset() {
		return super.getYOffset() - 0.5D;
	}
}
