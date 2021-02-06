package tamagotti.init.blocks.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import tamagotti.init.base.BaseModelBlock;

public class TFence extends BaseModelBlock {

	public TFence(String name) {
		super(Material.WOOD, name);
        setSoundType(SoundType.WOOD);
        setHardness(0.75F);
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.375D, 1.0D, 0.375D, 0.625D, 0.0D, 0.625D);
	}
}