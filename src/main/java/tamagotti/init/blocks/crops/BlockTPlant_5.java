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

public class BlockTPlant_5 extends BaseCropsBlock {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 5);

    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1250D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6250D, 1.0D)};

	public BlockTPlant_5(String name, int meta) {
		super(name, meta);
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[state.getValue(this.getAgeProperty()).intValue()];
    }

    /**
     * 0 = azuki
     * 1 = canola
     */

    //たまごっちメモ：ドロップする種
	@Override
	protected Item getSeed() {
		switch (this.data) {
		case 0:
			return ItemInit.azukiseed;
		case 1:
			return ItemInit.canolaseed;
		}
		return null;
	}

	//たまごっちメモ：ドロップする作物
	@Override
	protected Item getCrop() {
		switch (this.data) {
		case 0:
			return ItemInit.azukiseed;
		case 1:
			return ItemInit.canolaseed;
		}
		return null;
	}

	//たまごっちメモ：成長の最大段階数
	@Override
	public int getMaxAge() {
		return 5;
	}

	//ドロップ数を変更
  	@Override
  	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int age = getAge(state);
        boolean p1 = rand.nextBoolean();
        if(age >= getMaxAge()) {
			//菜種の作物側回収(4分の1で3つ菜種ができる)
			if (this.data == 1) {
				if (p1) {
					boolean p2 = rand.nextBoolean();
					if (p2) {
						for (int i = 0; i < 3; i++) {
							dAdd(drops);
						}
					} else {
						for (int i = 0; i < 2; i++) {
							dAdd(drops);
						}
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

	//たまごっちメモ：骨粉による上がるAGE数(最低、最大)
	@Override
	protected int getBonemealAgeIncrease(World worldIn) {
        return MathHelper.getInt(worldIn.rand, 1, 3);
    }
}
