package tamagotti.init.blocks.crops;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;

public class BlockBamboo extends Block implements IGrowable {


	public BlockBamboo(String name) {
		super(Material.PLANTS);
		setUnlocalizedName(name);
		setRegistryName(name);
		setTickRandomly(true);
		setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(2) == 0) {
				if (worldIn.getBlockState(pos.down(15)).getBlock() != BlockInit.bamboo) {
					this.grow(worldIn, rand, pos, state);
				}
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.down()).getBlock();
		if (block == this) {
			return true;
		} else if (block != Blocks.GRASS && block != Blocks.DIRT) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
		this.checkForDrop(worldIn, pos, state);
	}

	protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (this.canBlockStay(worldIn, pos)) {
			return true;
		} else {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos) {
		return this.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemInit.bambooi;
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return world.rand.nextFloat() < 0.45D;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(BlockInit.bamboo);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public BlockFaceShape func_193383_a(IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		if (world.getBlockState(pos.down()).getBlock() == BlockInit.bamboo || this.checkForDrop(world, pos, state)) {
			if (world.isAirBlock(pos.up())) {
				world.setBlockState(pos.add(0, 1, 0), BlockInit.bamboo.getDefaultState());
			} else if (world.getBlockState(pos.up()).getBlock() == BlockInit.bamboo) {
				for (int y = 1; y <= 10; y++) {
					if (world.getBlockState(pos.add(0, y - 1, 0)).getBlock() == BlockInit.bamboo) {
						if (world.isAirBlock(pos.up(y))) {
							world.setBlockState(pos.add(0, y, 0), BlockInit.bamboo.getDefaultState());
							return;
						}
					} else { return; }
				}
			}
		}
	}
}
