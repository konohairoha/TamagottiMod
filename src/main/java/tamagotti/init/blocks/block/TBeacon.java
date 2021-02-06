package tamagotti.init.blocks.block;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.tile.TileTBeacon;

public class TBeacon extends BaseFaceBlock {

	public TBeacon(String name) {
		super(Material.GLASS, name);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.1D, 0.9D, 0.1D, 0.9D, 0.0D, 0.9D);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileTBeacon();
	}
}
