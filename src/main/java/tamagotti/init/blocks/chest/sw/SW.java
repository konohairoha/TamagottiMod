package tamagotti.init.blocks.chest.sw;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseChestBlock;
import tamagotti.util.CollisionHelper;

public class SW extends BaseChestBlock {

	public static final AxisAlignedBB NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 0.35, 0.5, 0, 0.65, 0, 1);
	public static final AxisAlignedBB EAST = CollisionHelper.getBlockBounds(EnumFacing.EAST, 0.35, 0.5, 0, 0.65, 0, 1);
	public static final AxisAlignedBB SOUTH = CollisionHelper.getBlockBounds(EnumFacing.SOUTH, 0.35, 0.5, 0, 0.65, 0, 1);
	public static final AxisAlignedBB WEST = CollisionHelper.getBlockBounds(EnumFacing.WEST, 0.35, 0.5, 0, 0.65, 0, 1);
	public static final AxisAlignedBB[] BOX = { SOUTH, WEST, NORTH, EAST };

	public SW(String name) {
		super(name);
	}

	// ブロックの処理
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player) {
		player.openGui(TamagottiMod.INSTANCE, TGuiHandler.SWCHEST_GUI, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileSWChest();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		return BOX[facing.getHorizontalIndex()];
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> list, @Nullable Entity entity, boolean one) {
		EnumFacing facing = state.getValue(FACING);
		super.addCollisionBoxToList(pos, box, list, BOX[facing.getHorizontalIndex()]);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		EnumFacing face = state.getValue(FACING);
		IBlockState left_block = world.getBlockState(pos.offset(face.rotateYCCW()));
		IBlockState right_block = world.getBlockState(pos.offset(face.rotateY()));
		boolean left = left_block.getBlock() instanceof SW && left_block.getValue(FACING).equals(face);
		boolean right = right_block.getBlock() instanceof SW && right_block.getValue(FACING).equals(face);

		if (right) {
			if (left) {
				return state.withProperty(TYPE, Type.BOTH);
			} else {
				return state.withProperty(TYPE, Type.RIGHT);
			}
		} else if (left) {
			if (right) {
				return state.withProperty(TYPE, Type.BOTH);
			} else {
				return state.withProperty(TYPE, Type.LEFT);
			}
		}
		return state.withProperty(TYPE, Type.DEFAULT);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TYPE}) ;
	}
}
