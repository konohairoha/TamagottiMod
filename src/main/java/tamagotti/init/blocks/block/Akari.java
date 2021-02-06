package tamagotti.init.blocks.block;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import tamagotti.init.base.BaseGlassBlock;

public class Akari extends BaseGlassBlock {

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.2D, 0.2D, 0.2D, 0.8D, 0.0D, 0.8D);
	}

    public Akari(String name) {
    	super(name);
        setSoundType(SoundType.METAL);
		setLightLevel(1F);
    }

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
}