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

public class BlockRice extends BaseCropsBlock {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
    		new AxisAlignedBB(0.0D, 0.05D, 0.0D, 1.0D, 0.0D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.1D, 0.0D, 1.0D, 0.0D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.25D, 0.0D, 1.0D, 0.0D, 1.0D),
    		new AxisAlignedBB(0.0D, 0.65D, 0.0D, 0.95D, 0.0D, 0.95D),
    		new AxisAlignedBB(0.0D, 0.85D, 0.0D, 0.95D, 0.0D, 0.95D),
    		new AxisAlignedBB(0.0D, 0.85D, 0.0D, 0.95D, 0.0D, 0.95D),
    		new AxisAlignedBB(0.0D, 0.85D, 0.0D, 0.95D, 0.0D, 0.95D),
    		new AxisAlignedBB(0.0D, 0.8D, 0.0D, 0.95D, 0.0D, 0.95D)};

	public BlockRice(String name) {
		super(name, 0);
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[state.getValue(this.getAgeProperty()).intValue()];
    }

    //たまごっちメモ：ドロップする種
	@Override
	protected Item getSeed() {
		return ItemInit.riceseed;
	}

	//たまごっちメモ：ドロップする作物
	@Override
	protected Item getCrop() {
		return ItemInit.ine;
	}

	//たまごっちメモ：成長の最大段階数
	@Override
	public int getMaxAge() {
        return 7;
    }

	//たまごっちメモ：骨粉による上がるAGE数(最低、最大)
	@Override
	protected int getBonemealAgeIncrease(World worldIn) {
        return MathHelper.getInt(worldIn.rand, 2, 5);
    }

	//ドロップ数を変更
  	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		int age = getAge(state);
        boolean p1 = rand.nextBoolean();
        if(age >= getMaxAge()) {
			//作物回収量変更
			if (p1) {
				for (int i = 0; i < 4; i++) {
					dAdd(drops);
				}
        	} else {		//その他作物
        		dAdd(drops);
        	}
        	for(int i = rand.nextInt(4)+4; i > 0; i--) {
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
