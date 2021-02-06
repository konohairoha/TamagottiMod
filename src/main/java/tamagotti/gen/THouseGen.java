package tamagotti.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseChestBlock;
import tamagotti.init.base.BaseWorldGen;
import tamagotti.init.blocks.chest.wadansu.TileWadasu;
import tamagotti.init.items.tool.hheld.H_House_1;

public class THouseGen extends BaseWorldGen {

	public static List<ItemStack> loot = new ArrayList<>();

    public THouseGen() {
		this.maxChance = 160;
		this.minChance = 0;
		this.minY = 63;
		this.range = 4;
    }

    // 乱数の取得
    @Override
	public Random getRand (int chunkX, int chunkZ, World world) {
    	return new Random(world.getSeed() + chunkX + chunkZ * 16);
    }

    // 生成不可能なバイオーム
    @Override
	public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome == Biomes.DESERT;
    }

    // 生成できる座標であるか
    @Override
	public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    //生成物の内容
    @Override
	public void generate(World world, BlockPos pos) {

    	for (int x = -4; x < 4; x++) {
    		for (int z = -4; z < 4; z++) {
    			Block block = world.getBlockState(pos.add(x, 1, z)).getBlock();
        		if (block != Blocks.AIR && !(block instanceof BlockBush)) {
        			return;
        		}
        	}
    	}

		// たまごっちハウス生成
		H_House_1.genTHouse(world, pos);

		for (int z = -3; z <= -2; z++) {
			world.setBlockState(pos.add(3, 2, z), BlockInit.wadansu.getDefaultState().withProperty(BaseChestBlock.FACING,EnumFacing.WEST), 2);
			//宝箱の生成
			TileEntity chest = world.getTileEntity(pos.add(3, 2, z));
			if (chest != null && chest instanceof TileWadasu) {
				for (int l = 0; l < 54; l++) {
					int r = rand.nextInt(80);
					if (r < loot.size()) {
						ItemStack ret = loot.get(r);
						((TileWadasu) chest).setInventorySlotContents(l, ret);
					}
				}
			}
		}
    }

    //ルートテーブルの内容設定
    public static void initLoot() {
    	//init.アイテム名, 個数, データ値
		loot.add(new ItemStack(ItemInit.tamagotti, 2, 0));
		loot.add(new ItemStack(ItemInit.tamagottipickaxe, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottiaxe, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottishovel, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottisword, 1, 0));
    }
}
