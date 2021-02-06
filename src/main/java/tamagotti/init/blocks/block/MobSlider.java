package tamagotti.init.blocks.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.base.BaseFaceBlock;

public class MobSlider extends BaseFaceBlock {

	private final int data;

	public MobSlider(String name, int meta) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
		setHardness(0.5F);
		this.data = meta;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.90000000000000002D, 1.0D);
	}

	/**
	 * 0 = モブスライダー
	 * 1 = プレイヤージャンパー
	 */

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (this.data == 0) {
			EnumFacing face = state.getValue(FACING);

			switch (face) {
			case NORTH:
				entity.motionZ = 16D;
				entity.motionY = 0.125D;
				break;
			case SOUTH:
				entity.motionZ = -16D;
				entity.motionY = 0.125D;
				break;
			case EAST:
				entity.motionX = -16D;
				entity.motionY = 0.125D;
				break;
			case WEST:
				entity.motionX = 16D;
				entity.motionY = 0.125D;
				break;
			case DOWN:
			case UP:
			default:
				break;
			}
		} else if (this.data == 1) {
			entity.motionY += 8D;
		}
	}
}
