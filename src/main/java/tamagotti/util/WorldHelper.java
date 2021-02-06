package tamagotti.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class WorldHelper {

	// 範囲内のプレイヤーにポーション効果
	public static void PlayerPoint(World world, AxisAlignedBB aabb){
		List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		for (EntityPlayer ent : list) {
			ent.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 30, 0));
			ent.addPotionEffect(new PotionEffect(MobEffects.HASTE, 300, 3));
			ent.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300, 1));
		}
	}

	// アイテム引き寄せ
	public static void gravitateEntityTowards(Entity ent, double x, double y, double z) {
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
		double vel = 1.0 - dist / 15.0;
		if (vel > 0.0D) {
			vel *= vel;
			ent.motionX += dX / dist * vel * 0.175;
			ent.motionY += dY / dist * vel * 0.25;
			ent.motionZ += dZ / dist * vel * 0.175;
		}
	}

	// 敵モブを上に飛ばす
	public static void upEntity(World world, AxisAlignedBB aabb, BlockPos pos) {

		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, aabb);
		if (list.isEmpty()) { return; }

		for (Entity ent : list) {
			if ((ent instanceof EntityLiving) || (ent instanceof IProjectile)) {
				Vec3d p = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
				Vec3d t = new Vec3d(ent.posX, ent.posY, ent.posZ);
				double distance = p.distanceTo(t) + 0.1D;
				Vec3d r = new Vec3d(t.x + p.x, t.y + p.y, t.z + p.z);
				//r.x / 速度D / distance 速度を小さくするほど移動速度が速くなる（小さすぎると動かない）
				ent.motionY += r.y * 0.125D / distance;
				ent.motionX = 0;
				ent.motionZ = 0;
			}
		}
	}

	// 範囲内のプレイヤーにポーション効果
	public static void SuperSensor(World world, AxisAlignedBB aabb) {
		List<EntityLiving> list = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
		for (EntityLiving ent : list) {
			ent.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 1200, 0));
		}
	}

	// 範囲内のプレイヤーにポーション効果
	public static void SShiled(World world, AxisAlignedBB aabb) {
		List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		for (EntityPlayer ent : list) {
			ent.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 10, 0));
		}
	}

	// プレイヤー以外を吹っ飛ばす
	public static void Mob_Slide(World world, AxisAlignedBB aabb, BlockPos pos) {

		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, aabb);
		if (list.isEmpty()) { return; }

		for (Entity ent : list) {
			if (ent instanceof EntityLiving || ent instanceof IProjectile) {
				Vec3d p = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
				Vec3d t = new Vec3d(ent.posX, ent.posY, ent.posZ);
				double distance = p.distanceTo(t) + 0.067D;
				Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);
				ent.motionX += r.x * 0.66D / distance;
				ent.motionY += r.y * 0.66D / distance;
				ent.motionZ += r.z * 0.66D / distance;
			}
		}
	}

	public static void createLootDrop(List<ItemStack> drops, World world, BlockPos pos){
		createLootDrop(drops, world, pos.getX(), pos.getY(), pos.getZ());
	}

	public static void createLootDrop(List<ItemStack> drops, World world, double x, double y, double z) {

		ItemHelper.compactItemListNoStacksize(drops);
		if (drops.isEmpty()) { return; }

		for (ItemStack drop : drops) {
			EntityItem ent = new EntityItem(world, x, y, z);
			ent.setItem(drop);
			world.spawnEntity(ent);
		}
	}

	public static List<ItemStack> getBlockDrops(World world, EntityPlayer player, IBlockState state, Block block, BlockPos pos, boolean canSilk, int fortune){
		if (canSilk && block.canSilkHarvest(world, pos, state, player)){
			return Lists.newArrayList(new ItemStack(block, 1, block.getMetaFromState(state)));
		}
		return block.getDrops(world, pos, state, fortune);
	}

	// 範囲内のプレイヤーにポーション効果
	public static void ExAxe(World world, AxisAlignedBB aabb) {
		List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		for (EntityPlayer ent : list) {
			ent.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 0, true, false));
			ent.addPotionEffect(new PotionEffect(MobEffects.HASTE, 200, 1, true, false));
		}
	}

	// 弾を消すばりあーーーーーーーーーーーーーーーー
	public static void ShieldBarrier(World world, AxisAlignedBB aabb, EntityPlayer player) {

		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, aabb);
		if (list.isEmpty()) { return; }

		for (Entity entity : list) {

			if (entity != null) {

				boolean flag = false;
				Entity shooter = null;

				if (entity instanceof EntityArrow) {
					EntityArrow arrow = (EntityArrow) entity;
					if (arrow.shootingEntity != null) {
						shooter = arrow.shootingEntity;
					}
					flag = true;
				} else if (entity instanceof EntityThrowable) {
					EntityThrowable arrow = (EntityThrowable) entity;
					if (arrow.getThrower() != null) {
						shooter = arrow.getThrower();
					}
					flag = true;
				} else if (entity instanceof EntityFireball) {
					EntityFireball arrow = (EntityFireball) entity;
					if (arrow.shootingEntity != null) {
						shooter = arrow.shootingEntity;
					}
					flag = true;
				} else if (entity instanceof IProjectile) {
					flag = true;
				}

				if (flag) {
					if (shooter instanceof IMob || shooter == null) {
						world.playSound(player, new BlockPos(player), SoundEvents.ITEM_SHIELD_BREAK,
								SoundCategory.AMBIENT, 0.5F, 1.0F);
						entity.setDead();
					}
				}
			}
		}
	}

	public static List<TileEntity> getTileEntitiesWithinAABB(World world, AxisAlignedBB bBox) {

		List<TileEntity> list = new ArrayList<>();
		for (BlockPos pos : getPositionsFromBox(bBox)) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null) {
				list.add(tile);
			}
		}
		return list;
	}

	public static Iterable<BlockPos> getPositionsFromBox(AxisAlignedBB box) {
		return BlockPos.getAllInBox(new BlockPos(box.minX, box.minY, box.minZ), new BlockPos(box.maxX, box.maxY, box.maxZ));
	}
}
