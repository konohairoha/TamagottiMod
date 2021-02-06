package tamagotti.init.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import tamagotti.init.BlockInit;
import tamagotti.init.entity.monster.EntityLCEvoker;

public class TileLCESpaner extends TileTBase {

	@Override
	public void update() {

		// RS入力がなくてかつ難易度がピースフル以外のとき
		if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {

			super.update();
			for (int i = 0; i <= (this.tickTime / 10); i++) {
				this.spawnParticles(this.world, this.pos);
			}

			if(this.tickTime % (22 - this.tickTime / 5) == 0 && this.tickTime <= 80) {
				this.world.playSound((EntityPlayer) null, this.pos,SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1F );
			}

			if (this.tickTime >= 100) {
				this.tickTime = 0;
				this.spawnLCE();
			}

			return;
		}

		// tileがRSで止まっているとき
		this.notActive();
	}

	public boolean anyPlayerInRange() {
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		return this.world.isAnyPlayerWithinRangeAt(x + 0.5D, y + 0.5D, z + 0.5D, 15);
	}

	public void spawnLCE() {

		Block block = this.world.getBlockState(this.pos).getBlock();

		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		if (block == BlockInit.tspawner_lce && this.anyPlayerInRange()) {
			this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState(), 2);
			EntityLCEvoker entity = new EntityLCEvoker(this.world);
			entity.setLocationAndAngles(x + 0.5D, y + 2D, z + 0.5D, 0, 0.0F);
			if (!this.world.isRemote) {
				this.world.createExplosion(null, x + 0.5D, y + 0.5D, z + 0.5D, 2.5F, false);
				this.world.spawnEntity(entity);
			}
		}
	}

	//停止時の赤石パーティクル
	public void spawnParticles(World world, BlockPos pos) {
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		double d1 = x + this.rand.nextFloat();
		double d2 = (double) (y + this.rand.nextFloat()) + 1;
		double d3 = z + this.rand.nextFloat();
		world.spawnParticle(EnumParticleTypes.SPELL_WITCH, d1, d2, d3, 0.0D, 0.0D, 0.0D);
	}
}
