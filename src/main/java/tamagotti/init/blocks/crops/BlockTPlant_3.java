package tamagotti.init.blocks.crops;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseCropsBlock;

public class BlockTPlant_3 extends BaseCropsBlock {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5425D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6250D, 1.0D)};

	public BlockTPlant_3(String name, int meta) {
		super(name, meta);
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[state.getValue(this.getAgeProperty()).intValue()];
    }

    /**
     * 0 = j_radish
     * 1 = e_plant
     * 2 = lettuce
     * 3 = negi
     * 4 = cabbage
     */

    //たまごっちメモ：ドロップする種
	@Override
	protected Item getSeed() {
		switch (this.data) {
		case 0:
			return ItemInit.j_radishseed;
		case 1:
			return ItemInit.e_plantseed;
		case 2:
			return ItemInit.lettuceseed;
		case 3:
			return ItemInit.negiseed;
		case 4:
			return ItemInit.cabbageseed;
		}
		return null;
	}

	//たまごっちメモ：ドロップする作物
	@Override
	protected Item getCrop() {
		switch (this.data) {
		case 0:
			return ItemInit.j_radish;
		case 1:
			return ItemInit.e_plant;
		case 2:
			return ItemInit.lettuce;
		case 3:
			return ItemInit.negi;
		case 4:
			return ItemInit.cabbage;
		}
		return null;
	}

	//たまごっちメモ：成長の最大段階数
	@Override
	public int getMaxAge() {
        return 3;
    }

	//たまごっちメモ：骨粉による上がるAGE数(最低、最大)
	@Override
	protected int getBonemealAgeIncrease(World worldIn) {
        return MathHelper.getInt(worldIn.rand, 1,2);
    }

	//ドロップ数を変更
  	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int age = getAge(state);
        boolean p1 = rand.nextBoolean();
        if(age >= getMaxAge()) {
        	//作物回収量変更
			if (this.data == 1 || this.data == 2 || this.data == 4) {
				if (p1) {
					for (int i = 0; i < 2; i++) {
						dAdd(drops);
					}
				} else {
					dAdd(drops);
				}
			} else { //その他作物
				dAdd(drops);
			}
			for (int i = rand.nextInt(3) + 1; i > 0; i--) {
				drops.add(new ItemStack(this.getSeed(), 1, 0));
			}
		} else {
        	drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
    }

  	public void dAdd(NonNullList<ItemStack> drops) {
  		drops.add(new ItemStack(this.getCrop(), 1, 0));
  	}
}
