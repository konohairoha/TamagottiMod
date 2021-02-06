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
import net.minecraftforge.common.BiomeDictionary;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseWorldGen;

public class BambooGen extends BaseWorldGen {

    public BambooGen() {
		this.maxChance = 100;
		this.minChance = 0;
		this.minY = 60;
		this.maxY = 75;
		this.range = 4;
    }

    // 乱数の取得
    @Override
	public Random getRand (int chunkX, int chunkZ, World world) {
    	return new Random(world.getSeed() + chunkX + chunkZ * 32);
    }

    // 生成不可能なバイオーム
    @Override
	public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome != Biomes.PLAINS && !BiomeDictionary.hasType(world.getBiomeForCoordsBody(pos), BiomeDictionary.Type.MOUNTAIN);
    }

    // 生成できる座標であるか
    @Override
	public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

	// 竹の生成
	@Override
	public void generate(World world, BlockPos pos) {

    	for (int x = -2; x < 2; x++) {
    		for (int z = -2; z < 2; z++) {
    			Block block = world.getBlockState(pos.add(x, 1, z)).getBlock();
        		if (block != Blocks.AIR && !(block instanceof BlockBush)) {
        			return;
        		}
        	}
    	}

    	IBlockState air = Blocks.AIR.getDefaultState();
    	IBlockState grass = Blocks.GRASS.getDefaultState();
    	IBlockState dirt = Blocks.DIRT.getDefaultState();
    	IBlockState bamb = BlockInit.bamboo.getDefaultState();
    	IBlockState bambs = BlockInit.bamboo_s.getDefaultState();

		// 周りをリセット
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 1; y <= 8; y++) {
					world.setBlockState(pos.add(x, y, z), air, 2);
				}
				world.setBlockState(pos.add(x, 0, z), grass, 2);
			}
		}

		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				world.setBlockState(pos.add(x, -2, z), dirt, 2);
				world.setBlockState(pos.add(x, -1, z), dirt, 2);
			}
		}

		// 生成させる
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {

				int chance = this.rand.nextInt(9);
				int random = this.rand.nextInt(8);
				BlockPos pos2 = new BlockPos(x, 0, z);

				if (chance >= 5) {

					// 設置個所が空気じゃないと生成しない
					if (world.getBlockState(pos2.add(x, 1, z)).getBlock() == Blocks.AIR) {
						for (int y = 1; y <= 6 + random; y++) {
							world.setBlockState(pos.add(x, y, z), bamb, 2);
						}

					// 上が空気なら生成
					} else if (world.getBlockState(pos.add(x, 2, z)).getBlock() == Blocks.AIR) {
						for (int y = 1; y <= 6 + random; y++) {
							world.setBlockState(pos.add(x, y, z), bamb, 2);
						}
					}
				} else if (chance <= 1) {
					world.setBlockState(pos.add(x, 1, z), bambs, 2);
				}
			}
		}
	}

	// 竹の生成
	public void genBambooS (World world, BlockPos pos) {
		world.setBlockState(pos, BlockInit.bamboo_s.getDefaultState(), 2);
	}
}
