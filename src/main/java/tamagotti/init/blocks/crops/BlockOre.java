package tamagotti.init.blocks.crops;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseCropsBlock;

public class BlockOre extends BaseCropsBlock {


    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
    		new AxisAlignedBB(0D, 0D, 0D, 1D, 0.625D, 1D),
    		new AxisAlignedBB(0D, 0D, 0D, 1D, 0.1250D, 1D),
    		new AxisAlignedBB(0D, 0D, 0D, 1D, 0.3125D, 1D),
    		new AxisAlignedBB(0D, 0D, 0D, 1D, 0.4375D, 1D),
    		new AxisAlignedBB(0D, 0D, 0D, 1D, 0.5625D, 1D),
    		new AxisAlignedBB(0D, 0D, 0D, 1D, 0.6250D, 1D)};

	public BlockOre(String name, int meta) {
		super(name, meta);
	}

	/**
	 * 0 = 鉄
	 * 1 = 金
	 * 2 = ラピ
	 * 3 = 赤石
	 * 4 = ネザクオ
	 */

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[state.getValue(this.getAgeProperty()).intValue()];
    }

    //たまごっちメモ：ドロップする種
	@Override
	protected Item getSeed() {
		switch (this.data) {
		case 0:
			return ItemInit.ironseed;
		case 1:
			return ItemInit.goldseed;
		case 2:
			return ItemInit.lapisseed;
		case 3:
			return ItemInit.redseed;
		case 4:
			return ItemInit.quartzseed;
		}
		return null;
	}

	//たまごっちメモ：ドロップする作物
	@Override
	protected Item getCrop() {
		switch (this.data) {
		case 0:
			return Items.IRON_INGOT;
		case 1:
			return Items.GOLD_INGOT;
		case 2:
			return ItemInit.lapisseed;
		case 3:
			return Items.REDSTONE;
		case 4:
			return Items.QUARTZ;
		}
		return null;
	}

	//たまごっちメモ：成長の最大段階数
	@Override
	public int getMaxAge() {
		return 5;
	}

	//たまごっちメモ：骨粉による上がるAGE数(最低、最大)
	@Override
	protected int getBonemealAgeIncrease(World world) {
		return world.rand.nextInt(5) == 0 ? 1 : 0;
	}

	//たまごっちメモ：成長する乱数を変更
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		super.updateTick(world, pos, state, rand);
		if (!world.isAreaLoaded(pos, 1)) { return; }

		if (world.getLightFromNeighbors(pos.up()) >= 9) {

			int i = this.getAge(state);
			if (i < this.getMaxAge()) {
				float f = getGrowthChance(this, world, pos);
				//kazuの数字をいじれば成長する乱数を変更（デフォルトは25）
				float kazu = 220;
				if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (kazu / f) + 1) == 0)) {
					world.setBlockState(pos, this.withAge(i + 1), 2);
					ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
				}
			}
		}
	}

	//ドロップ数を変更
  	@Override
  	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
  		int age = getAge(state);
        if(age >= getMaxAge()) {
        	drops.add(new ItemStack(this.getCrop(), 1, 0));
        	for(int i = this.rand.nextInt(5)+1; i > 0; i--) {
        		drops.add(new ItemStack(this.getSeed(), 1, 0));
        	}
        	if(state.getBlock() == BlockInit.lapisblock) {
        		drops.add(new ItemStack(Items.DYE, 1, 4));
        	}
        } else {
        	drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
    }
}
