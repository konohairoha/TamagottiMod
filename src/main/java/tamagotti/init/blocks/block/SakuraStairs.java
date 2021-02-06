package tamagotti.init.blocks.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import tamagotti.init.BlockInit;

public class SakuraStairs extends BlockStairs {

	public SakuraStairs(String name, IBlockState state) {
		super(state);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setHardness(0.5F);
		this.setResistance(5.0F);
		this.useNeighborBrightness = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
		BlockInit.blockList.add(this);
	}
}
