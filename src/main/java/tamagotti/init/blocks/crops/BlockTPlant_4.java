package tamagotti.init.blocks.crops;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseCropsBlock;

public class BlockTPlant_4 extends BaseCropsBlock {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 4);

    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
    		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.0625D, 0.9D),
    		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.3125D, 0.9D),
    		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.4375D, 0.9D),
    		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.5625D, 0.9D),
    		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.6250D, 0.9D)};

	public BlockTPlant_4(String name, int meta) {
		super(name, meta);
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[state.getValue(this.getAgeProperty()).intValue()];
    }

    /**
     * 0 = onion
     * 1 = soba
     * 2 = soy
     * 3 = tomato
     * 4 = strawberry
     */

    //たまごっちメモ：ドロップする種
	@Override
	protected Item getSeed() {
		switch (this.data) {
		case 0:
			return ItemInit.onion;
		case 1:
			return ItemInit.sobaseed;
		case 2:
			return ItemInit.soyseed;
		case 3:
			return ItemInit.tomato;
		case 4:
			return ItemInit.strawberry;
		}
		return null;
	}

	//たまごっちメモ：ドロップする作物
	@Override
	protected Item getCrop() {
		switch (this.data) {
		case 0:
			return ItemInit.onion;
		case 1:
			return ItemInit.sobaseed;
		case 2:
			return ItemInit.edamame;
		case 3:
			return ItemInit.tomato;
		case 4:
			return ItemInit.strawberry;
		}
		return null;
	}

	//たまごっちメモ：成長の最大段階数
	@Override
	public int getMaxAge() {
        return 4;
    }

	//ドロップ数を変更
  	@Override
  	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int age = getAge(state);
        boolean p1 = rand.nextBoolean();
        if(age >= getMaxAge()) {
			//枝豆の作物回収(4分の1で3つ枝豆ができる)
			if (this.data == 2) {
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
}
