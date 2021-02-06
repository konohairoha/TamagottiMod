package tamagotti.gen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.config.TConfig;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseWorldGen;
import tamagotti.init.entity.monster.EntityZombieMaster;

public class SabakuDunGen extends BaseWorldGen {

	public static List<ItemStack> loot = new ArrayList<>();

    public SabakuDunGen() {
		this.minChance = TConfig.spawnchance_desert;
		this.maxChance = 200;
		this.minChance = 0;
    }

    //生成物の内容
    @Override
    public void generate(World world, BlockPos pos) {

    	for (int x = -6; x < 6; x++) {
    		for (int z = -6; z < 6; z++) {
        		if (!world.isAirBlock(pos.add(x, 1, z))) {
        			return;
        		}
        	}
    	}

    	IBlockState sand = Blocks.SAND.getDefaultState();
    	IBlockState stone = Blocks.STONEBRICK.getDefaultState();
    	IBlockState air = Blocks.AIR.getDefaultState();
    	IBlockState slab = Blocks.STONE_SLAB.getDefaultState();
    	IBlockState logger = Blocks.LADDER.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-8, -10, -4), pos.add(9, 0, 17))) {
			world.setBlockState(p, sand);
		}

    	//土台
		for (int x = -1; x <= 2; x++) {
			for (int z = -4; z <= 5; z++) {
				world.setBlockState(pos.add(x, 0, z), sand);
			}
		}
		for (int x = -4; x <= 5; x++) {
			for (int z = -1; z <= 2; z++) {
				world.setBlockState(pos.add(x, 0, z), sand);
			}
		}
		for (int x = -2; x <= 3; x++) {
			for (int z = -3; z <= 4; z++) {
				world.setBlockState(pos.add(x, 0, z), sand);
			}
		}
		for (int x = -3; x <= 4; x++) {
			for (int z = -2; z <= 3; z++) {
				world.setBlockState(pos.add(x, 0, z), sand);
			}
		}

		for (int a = 0; a <= 1; a++) {
			for (int b = 0; b <= 1; b++) {
				int xa = a * 5;
				int zb = b * 5;
				for (int y = 1; y <= 4; y++) {
					world.setBlockState(pos.add(-2 + xa, y, -2 + zb), stone);

				}
			}
		}

		// 屋根
		for (int x = -2; x <= 3; x++) {
			for (int z = -3; z <= 4; z++) {
				world.setBlockState(pos.add(x, 5, z), slab);
			}
		}
		for (int x = -3; x <= 4; x++) {
			for (int z = -2; z <= 3; z++) {
				world.setBlockState(pos.add(x, 5, z), slab);
			}
		}

		for (int x = -1; x <= 2; x++) {
			for (int z = -2; z <= 3; z++) {
				world.setBlockState(pos.add(x, 5, z), stone);
			}
		}
		for (int x = -2; x <= 3; x++) {
			for (int z = -1; z <= 2; z++) {
				world.setBlockState(pos.add(x, 5, z), stone);
			}
		}

		for (int x = 0; x <= 1; x++) {
			for (int z = -1; z <= 2; z++) {
				world.setBlockState(pos.add(x, 6, z), slab);
			}
		}
		for (int x = -1; x <= 2; x++) {
			for (int z = 0; z <= 1; z++) {
				world.setBlockState(pos.add(x, 6, z), slab);
			}
		}

		for (int x = 0; x <= 1; x++) {
			for (int z = -1; z <= 2; z++) {
				world.setBlockState(pos.add(x, 5, z), air);
			}
		}
		for (int x = -1; x <= 2; x++) {
			for (int z = 0; z <= 1; z++) {
				world.setBlockState(pos.add(x, 5, z), air);
			}
		}

		//入口
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -16, -1), pos.add(2, 0, 2))) {
			world.setBlockState(p, stone);
		}

		for (BlockPos p : BlockPos.getAllInBox(pos.add(0, -15, 0), pos.add(1, 2, 1))) {
			world.setBlockState(p, air);
		}

		for (int y = 1; y <= 2; y++) {
			world.setBlockState(pos.add(0, y, -1), air);
			world.setBlockState(pos.add(1, y, -1), air);
		}

		//はしご
		for (int y = -15; y <= 0; y++) {
			world.setBlockState(pos.add(0, y, 0), logger);
			world.setBlockState(pos.add(1, y, 0), logger);
		}

		//地下一階入り口
		for (int x = -1; x <= 2; x++) {
			for (int y = -16; y <= -5; y++) {
				world.setBlockState(pos.add(x, y, 2), stone);
				world.setBlockState(pos.add(x, y, 3), stone);
			}
		}

		//地下一階メインフロア
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-15, -17, 3), pos.add(16, -7, 36))) {
			world.setBlockState(p, stone);
		}

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-14, -15, 4), pos.add(15, -8, 34))) {
			world.setBlockState(p, air);
		}

		for (int x = 0; x <= 1; x++) {
			for (int z = 2; z <= 3; z++) {
				world.setBlockState(pos.add(x, -14, z), air);
				world.setBlockState(pos.add(x, -15, z), air);
			}
		}

		// 柱
		for (int a = 0; a <= 1; a++) {
			for (int b = 0; b <= 1; b++) {
				int xa = a * 20;
				int zb = b * 20;
				for (int x = -10 + xa; x <= -9 + xa; x++) {
					for (int z = 8 + zb; z <= 9 + zb; z++) {
						for (int y = -16; y <= -8; y++) {
							world.setBlockState(pos.add(x, y, z), stone);
						}
					}
				}

			}
		}

		//チェスト
		for (int x = 0; x <= 1; x++) {
			world.setBlockState(pos.add(x, -15, 35), Blocks.CHEST.getDefaultState());
			//宝箱の生成
			TileEntity chest = world.getTileEntity(pos.add(x, -15, 35));
			if (chest != null && chest instanceof TileEntityChest) {
				for (int l = 0; l < 27; l++) {
					int r = this.rand.nextInt(32);
					if (r < loot.size()) {
						ItemStack ret = loot.get(r);
						((TileEntityChest) chest).setInventorySlotContents(l, ret);
					}
				}
			}
		}

		//スポナー
		for (int a = 0; a <= 1; a++) {
			for (int b = 0; b <= 3; b++) {
				int xa = a * 11;
				int zb = b * 5;
				world.setBlockState(pos.add(-5 + xa, -16, 11 + zb), Blocks.MOB_SPAWNER.getDefaultState(), 2);
				TileEntity tile = world.getTileEntity(pos.add(-5 + xa, -16, 11 + zb));
				int rnd = this.rand.nextInt(8);
				if (tile instanceof TileEntityMobSpawner) {

					MobSpawnerBaseLogic sp = ((TileEntityMobSpawner) tile).getSpawnerBaseLogic();

					switch (rnd) {
					case 0:
						sp.setEntityId(EntityList.getKey(EntityWitherSkeleton.class));
						break;
					case 1:
						sp.setEntityId(EntityList.getKey(EntityZombie.class));
						break;
					case 2:
						sp.setEntityId(EntityList.getKey(EntitySkeleton.class));
						break;
					case 3:
						sp.setEntityId(EntityList.getKey(EntityVindicator.class));
						break;
					case 4:
						sp.setEntityId(EntityList.getKey(EntityEvoker.class));
						break;
					case 5:
						sp.setEntityId(EntityList.getKey(EntityZombieMaster.class));
						break;
					case 6:
						sp.setEntityId(EntityList.getKey(EntityBlaze.class));
						break;
					case 7:
						sp.setEntityId(EntityList.getKey(EntityWitch.class));
						break;
					}
				}
			}
		}
    }

    //ルートテーブルの内容設定
    public static void initLoot() {
    	//init.アイテム名, 個数, データ値
		loot.add(new ItemStack(ItemInit.machinesword, 1, 0));
		loot.add(new ItemStack(ItemInit.m_boot, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagotticustom, 3, 0));
		loot.add(new ItemStack(ItemInit.tamagotticollaboration, 1, 0));
		loot.add(new ItemStack(ItemInit.b_tamagotti_book, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottishelter, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottiblaster, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottispinner, 1, 0));
		loot.add(new ItemStack(ItemInit.yukarishotter, 1, 0));
		loot.add(new ItemStack(ItemInit.yukarrittascope, 1, 0));
		loot.add(new ItemStack(ItemInit.yukarisweeper, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottiaxeneo, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottishovelneo, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottiswordneo, 1, 0));
		loot.add(new ItemStack(ItemInit.tamagottipickaxeneo, 1, 0));
    }
}
