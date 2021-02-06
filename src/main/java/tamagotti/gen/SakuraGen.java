package tamagotti.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseWorldGen;

public class SakuraGen extends BaseWorldGen {

    public SakuraGen() {
		this.maxChance = 40;
		this.minChance = 0;
		this.range = 4;
    }

    // 乱数の取得
    @Override
	public Random getRand (int chunkX, int chunkZ, World world) {
    	return new Random(world.getSeed() + chunkX + chunkZ * 24);
    }

    // 生成不可能なバイオーム
    @Override
	public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome != Biomes.PLAINS;
    }

    @Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

		//ネザー、エンドでは生成しない
		int genDim1 = world.provider.getDimension();
		if ((genDim1 == 1 || genDim1 == -1)) { return; }

		// 生成時のチェックして生成
		if(this.checkGen(rand, chunkX, chunkZ, world)) {
			this.generate(world, this.pos.up());
		}
	}

    // 生成できる座標であるか
    @Override
	public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    //生成物の内容
    @Override
	public void generate(World world, BlockPos pos) {

    	// 高さの乱数の取得
    	int random = world.rand.nextInt(2);

    	//生成範囲が空気かどうか
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-2, 3 + random, -2), pos.add(2, 4 + random, 2))) {

			Block block = world.getBlockState(p).getBlock();
			if(block != Blocks.AIR && !(block instanceof BlockBush)) {
				return;
			}
		}

		//葉っぱ一段目
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlockState(pos.add(x, 3 + random, z), BlockInit.sakuraleave.getDefaultState(), 2);
			}
		}

		//葉っぱ２段目
		for (int x = -1; x <= 1; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlockState(pos.add(x, 4 + random, z), BlockInit.sakuraleave.getDefaultState(), 2);
			}
		}
		for (int x = -2; x <= 2; x++) {
			for (int z = -1; z <= 1; z++) {
				world.setBlockState(pos.add(x, 4 + random, z), BlockInit.sakuraleave.getDefaultState(), 2);
			}
		}

		//葉っぱ3段目
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				world.setBlockState(pos.add(x, 5 + random, z), BlockInit.sakuraleave.getDefaultState(), 2);
			}
		}

		//葉っぱ最上段
		for (int z = -1; z <= 1; z++) {
			world.setBlockState(pos.add(0, 6 + random, z), BlockInit.sakuraleave.getDefaultState(), 2);
		}for (int x = -1; x <= 1; x++) {
			world.setBlockState(pos.add(x, 6 + random, 0), BlockInit.sakuraleave.getDefaultState(), 2);
		}

		//原木
		for (int y = 0; y <= 4 + random; y++) {
			world.setBlockState(pos.add(0, y, 0), BlockInit.sakuralog.getDefaultState(), 2);
		}
    }

    public void genSapling (World world, BlockPos pos) {
		world.setBlockState(pos, BlockInit.sakurasapling.getDefaultState(), 2);
    }
}
