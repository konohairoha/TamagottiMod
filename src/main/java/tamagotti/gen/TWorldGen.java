package tamagotti.gen;

import java.util.Random;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import tamagotti.init.BlockInit;

public class TWorldGen implements IWorldGenerator {

	//ワールド生成用変数
    private WorldGenerator redberylore, smoky,quartz, fluoriteore, copperore, t_ore, t_neoore, silverore, rubyore, starore, roseore, t_hore;

    public TWorldGen() {

        //たまごっちメモ：ワールド生成用変数 = new WorldGenMinable (ブロック名) , (ブロックの最大生成数) , (置換するブロック)
        redberylore = new WorldGenMinable(BlockInit.redberylore.getDefaultState(), 12, BlockMatcher.forBlock(Blocks.NETHERRACK));
        quartz = new WorldGenMinable(BlockInit.smokyquartzore.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.NETHERRACK));
        smoky = new WorldGenMinable(BlockInit.smokyquartzore.getDefaultState(), 6, BlockMatcher.forBlock(Blocks.NETHERRACK));
        fluoriteore = new WorldGenMinable(BlockInit.fluoriteore.getDefaultState(), 7,BlockMatcher.forBlock(Blocks.STONE));
		copperore = new WorldGenMinable(BlockInit.copperore.getDefaultState(), 9, BlockMatcher.forBlock(Blocks.STONE));
		t_ore = new WorldGenMinable(BlockInit.t_ore.getDefaultState(), 11, BlockMatcher.forBlock(Blocks.STONE));
        t_neoore = new WorldGenMinable(BlockInit.t_neoore.getDefaultState(), 9, BlockMatcher.forBlock(Blocks.NETHERRACK));
        silverore = new WorldGenMinable(BlockInit.silverore.getDefaultState(), 6, BlockMatcher.forBlock(Blocks.STONE));
        rubyore = new WorldGenMinable(BlockInit.rubyore.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.NETHERRACK));
        starore = new WorldGenMinable(BlockInit.starrosequartzore.getDefaultState(), 4, BlockMatcher.forBlock(Blocks.STONE));
        roseore = new WorldGenMinable(BlockInit.starrosequartzore.getDefaultState(), 2, BlockMatcher.forBlock(Blocks.STONE));
        t_hore = new WorldGenMinable(BlockInit.t_hore.getDefaultState(), 4, BlockMatcher.forBlock(Blocks.END_STONE));
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
//        if(world.provider.getDimension() == 0) {    //オーバーワールド鉱石
            this.runGenerator(fluoriteore, world, random, chunkX, chunkZ, 4, 0, 20);
            this.runGenerator(copperore, world, random, chunkX, chunkZ, 16, 0, 64);
            this.runGenerator(t_ore, world, random, chunkX, chunkZ, 4, 0, 124);
            this.runGenerator(silverore, world, random, chunkX, chunkZ, 4, 0, 48);
            this.runGenerator(starore, world, random, chunkX, chunkZ, 3, 0, 16); //クォーツ多め
            this.runGenerator(roseore, world, random, chunkX, chunkZ, 1, 0, 52); //クォーツ少なめ
//        }if(world.provider.getDimension() == -1) {    //ネザー鉱石
            this.runGenerator(rubyore, world, random, chunkX, chunkZ, 4, 0, 128);
            this.runGenerator(redberylore, world, random, chunkX, chunkZ, 4, 100, 128);
            this.runGenerator(smoky, world, random, chunkX, chunkZ, 2, 0, 128);
            this.runGenerator(quartz, world, random, chunkX, chunkZ, 2, 0, 24);
            this.runGenerator(t_neoore, world, random, chunkX, chunkZ, 3, 0, 128);
            this.runGenerator(t_hore, world, random, chunkX, chunkZ, 16, 0, 256);
//        }
            //備忘メモ：じぇねれーたの後ろの数字はチャンス、一番下の高さ、一番上の高さ
            //たまごっちメモ：0＝オーバーワールド、1＝エンド、-1＝ネザー
    }

    private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight) {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256) throw new IllegalArgumentException("Ore generated out of bounds");
        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);
            gen.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
