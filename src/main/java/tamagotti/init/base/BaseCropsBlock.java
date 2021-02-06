package tamagotti.init.base;

import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.BlockInit;

public class BaseCropsBlock extends BlockCrops {

	protected Random rand = new Random();
	protected final int data;

	public BaseCropsBlock(String name, int meta) {
		setUnlocalizedName(name);
		setRegistryName(name);
		this.data = meta;
		BlockInit.noTabList.add(this);
	}

	//たまごっちメモ：ドロップする数の最低数
	@Override
	public int quantityDropped(Random random) {
        return 1;
    }

	//たまごっちメモ：AGEが最大のときに種と作物をドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.isMaxAge(state) ? this.getCrop() : this.getSeed();
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getSeed());
	}

	//たまごっちメモ：骨粉による上がるAGE数(最低、最大)
	@Override
	protected int getBonemealAgeIncrease(World world) {
        return MathHelper.getInt(world.rand, 1, 3);
    }

	//たまごっちメモ：耕した土以外のときは作物が消える
	@Override
    public boolean canBlockStay(World world,BlockPos pos, IBlockState state) {
        IBlockState soil = world.getBlockState(pos.down());
        return (world.getLight(pos) >= 8 || world.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
    }

  	//ドロップ数を変更
  	@Override
  	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (this.getAge(state) >= this.getMaxAge()) {
			drops.add(new ItemStack(this.getCrop(), 1, 0));
			for (int i = rand.nextInt(3) + 1; i > 0; i--) {
				drops.add(new ItemStack(this.getSeed(), 1, 0));
			}
		} else { //最大成長Ageではない場合、種を落とすようにするための処理
			drops.add(new ItemStack(this.getSeed(), 1, 0));
		}
	}
}
