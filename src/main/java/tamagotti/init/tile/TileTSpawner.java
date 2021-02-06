package tamagotti.init.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.blocks.block.TSpawner;
import tamagotti.util.TUtil;

public class TileTSpawner extends TileTBase {

	public int randTick = 0;

	@Override
	public void update() {

		// RS入力がなくてかつ難易度がピースフル以外のとき
		if (TUtil.isActive(this.world, this.pos) && this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {

			super.update();

			if (this.randTick == 0) {
				this.randTick= this.rand.nextInt(150);
			}

			// 時間が経てばモブスポーンの処理へ
			if (this.tickTime >= (150 + this.randTick)) {
				this.spanerMob();
				this.tickTime = 0;
				this.randTick= this.rand.nextInt(150);
			}

		} else {

			// tileがRSで止まっているとき
			this.notActive();
		}
	}

	public void spanerMob() {

		// 向き取得
		IBlockState state = this.world.getBlockState(this.pos);
		EnumFacing facing = state.getValue(BaseFaceBlock.FACING);
		Block block = state.getBlock();

		// 念のためスポナーじゃなかったら何もしない
		if (!(block instanceof TSpawner)) { return; }

		// 向きに合わせて座標を動かすための変数の用意
		int changeX = 0, changeZ = 0;
		switch (facing) {
		case NORTH:
			changeZ = 4;
			break;
		case SOUTH:
			changeZ = -4;
			break;
		case EAST:
			changeX = -4;
			break;
		case WEST:
			changeX = 4;
			break;
		case DOWN:
		case UP:
		default:
			break;
		}

		EntityMob entity[] = new EntityMob[6];
		for (int i = 0; i < entity.length; i++) {

			// ブロックの種類でスポーンするエンティティを判断する
			if (block == BlockInit.tspawner_s) {
				entity[i] = new EntitySkeleton(this.world);
			} else if (block == BlockInit.tspawner_z) {
				entity[i] = new EntityZombie(this.world);
			} else if (block == BlockInit.tspawner_c) {
				entity[i] = new EntityCreeper(this.world);
			} else if (block == BlockInit.tspawner_b) {
				entity[i] = new EntityBlaze(this.world);
			} else if (block == BlockInit.tspawner_wi) {
				entity[i] = new EntityWitch(this.world);
			} else if (block == BlockInit.tspawner_ws) {
				entity[i] = new EntityWitherSkeleton(this.world);
			} else {
				return;
			}

			// 座標を変えるためにランダム用意
			int xRand = this.rand.nextInt(7);
			int yRand = this.rand.nextInt(3);
			int zRand = this.rand.nextInt(7);
			double x = this.pos.getX() + 0.5F + changeX + xRand - 3;
			double y = this.pos.getY() + 0.5F + yRand;
			double z = this.pos.getZ() + 0.5F + changeZ + zRand - 3;

			if (!this.world.isRemote) {
				entity[i].setLocationAndAngles(x, y, z, 0, 0.0F);
				if (this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) {
					this.world.spawnEntity(entity[i]);
				}
			}
		}
	}
}
