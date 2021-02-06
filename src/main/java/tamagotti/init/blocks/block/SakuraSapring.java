package tamagotti.init.blocks.block;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.gen.SakuraGen;
import tamagotti.init.BlockInit;

public class SakuraSapring extends BlockBush implements IGrowable {

	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.099D, 0.0D, 0.099D, 0.89D, 0.8D, 0.89D);

	public SakuraSapring(String name) {
        super(Material.PLANTS);
        setRegistryName(name);
        setUnlocalizedName(name);
		setSoundType(SoundType.PLANT);
		BlockInit.blockList.add(this);
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		SakuraGen gen = new SakuraGen();
		gen.generate(world, pos);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SAPLING_AABB;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
				this.grow(worldIn, rand, pos, state);
			}
		}
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return worldIn.rand.nextFloat() < 0.45D;
	}
}
