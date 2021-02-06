package tamagotti.init.base;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import tamagotti.config.TConfig;

public class BaseWorldGen implements IWorldGenerator {

	public Random rand;
	public BlockPos pos;
	public int minChance;
	public int maxChance;
	public int minY;
	public int maxY;
	public int range;

    public BaseWorldGen() {
		this.minChance = TConfig.spawnchance_desert;
		this.maxChance = 200;
		this.minY = 63;
		this.maxY = 70;
		this.range = 4;
    }

    @Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

		//ネザー、エンドでは生成しない
		int genDim1 = world.provider.getDimension();
		if ((genDim1 == 1 || genDim1 == -1)) { return; }

		// 生成時のチェックして生成
		if(this.checkGen(rand, chunkX, chunkZ, world)) {
			this.generate(world, this.pos);
		}
	}

    //生成条件
    public boolean checkGen(Random rand, int chunkX, int chunkZ, World world) {

		//ネザー、エンドでは生成しない
		int dim = world.provider.getDimension();
		if ((dim == 1 || dim == -1)) { return false; }

		// 乱数の取得
    	this.rand = this.getRand(chunkX, chunkZ, world);

    	// チャンス少ないなら終了
		if (this.rand.nextInt(this.maxChance) > this.minChance) { return false; }

		// x軸、y軸選定
		int posX = chunkX << this.range;
		int posZ = chunkZ << this.range;
		posX += this.rand.nextInt(this.range) + this.rand.nextInt(this.range);
		posZ += this.rand.nextInt(this.range) + this.rand.nextInt(this.range);

		// バイオーム判定
		BlockPos pos = new BlockPos(posX, 60, posZ);
		Biome biome = world.getBiomeForCoordsBody(pos);

		// 砂漠以外なら終了
		if (this.checkBiome(world, pos, biome)) { return false; }

		// 村の近くなら終了
		if (world.villageCollection.getNearestVillage(pos, 6) != null) { return false; }

		// 高さ判定
		for (int y = this.minY; y < this.maxY; y++) {

			this.pos = new BlockPos(posX, y, posZ);
			IBlockState state = world.getBlockState(this.pos);

		    // 生成できる座標であるか
			if (this.checkBlock(world, state, this.pos)) {
				return true;
			}
		}
		return false;
	}

    // 乱数の取得
    public Random getRand (int chunkX, int chunkZ, World world) {
    	return new Random(world.getSeed() + chunkX + chunkZ * 31);
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome != Biomes.DESERT;
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.SAND;
    }

    public boolean isReplaceable(World world, IBlockState state, BlockPos pos) {
		return state.getBlock().isAir(state, world, pos) || state.getMaterial().isReplaceable();
	}

    //生成物の内容
    public void generate(World world, BlockPos pos) {}
}
