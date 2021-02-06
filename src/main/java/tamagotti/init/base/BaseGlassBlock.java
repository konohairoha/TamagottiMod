package tamagotti.init.base;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseGlassBlock extends BaseModelBlock {

	public BaseGlassBlock(String name) {
		super(Material.GLASS, name);
        setSoundType(SoundType.GLASS);
        setHardness(0.1F);
		setResistance(16.0F);
    }

	public BaseGlassBlock(Material material, String name) {
		super(material, name);
        setHardness(0.1F);
		setResistance(16.0F);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		BlockPos check = pos.offset(side);
		IBlockState state2 = world.getBlockState(check);
		if (state.getBlock() == this) {
			if (state2.getBlock() instanceof BlockBreakable) { return false; }
			if (!state2.isSideSolid(world, check, side.getOpposite())) { return true; }
		}
		return super.shouldSideBeRendered(state, world, pos, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	// 接してる面側が水だったら、その接してる水の側面を描画しない
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		boolean b = world.getBlockState(pos.up()).getMaterial() == Material.AIR;
		if (!b && world.getBlockState(pos.offset(face)).getMaterial() == Material.WATER) { return true; }
		return false;
	}
}
