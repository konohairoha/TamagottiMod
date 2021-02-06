package tamagotti.init.blocks.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.util.SittableUtil;

public class TChair extends BaseFaceBlock {

	private static final AxisAlignedBB DOWN = new AxisAlignedBB(0.075D, 2D, 0.075D, 0.925D, 0D, 0.925D);
	private static final AxisAlignedBB TOP = new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getBlock() == BlockInit.tchair_top ? TOP : DOWN;
	}

	public TChair(String name) {
		super(Material.WOOD, name);
        setHardness(1F);
        setSoundType(SoundType.WOOD);
    }

	// 以下が座れるようにするための処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		int addY = state.getBlock() == BlockInit.tchair_down ? 0 : -1;
		if (!world.isRemote && SittableUtil.sitOnBlock(world, pos.getX(), pos.getY() + 0.15 + addY, pos.getZ(), player, 6 * 0.0625)) {
			world.updateComparatorOutputLevel(pos, this);
		}
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return SittableUtil.isSomeoneSitting(worldIn, pos.getX(), pos.getY(), pos.getZ()) ? 1 : 0;
	}

	// 以下が2ブロックの処理
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos.up(), BlockInit.tchair_top.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.breakBlock(this == BlockInit.tchair_down ? pos.up() : pos.down(), world, true);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, World world, boolean dropBlock) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (block.isAir(state, world, pos)) {
			return false;
		} else {
			world.playEvent(2001, pos, Block.getStateId(state));

			if (dropBlock) {
				block.dropBlockAsItem(world, pos, state, 0);
			}

			return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}
	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this == BlockInit.tchair_down ? new ItemStack(this).getItem() : ItemStack.EMPTY.getItem();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.tchair_down);
	}
}
