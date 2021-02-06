package tamagotti.init.blocks.chest.ychest;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseHopperBlock;
import tamagotti.util.CollisionHelper;

public class YChest extends BaseHopperBlock {

	private static final AxisAlignedBB NORTH = CollisionHelper.getBlockBounds(EnumFacing.NORTH, 0.0, 1.0, 0.0, 0.7, 0.0, 1.0);
	private static final AxisAlignedBB EAST = CollisionHelper.getBlockBounds(EnumFacing.EAST, 0.0, 1.0, 0.0, 0.7, 0.0, 1.0);
	private static final AxisAlignedBB SOUTH = CollisionHelper.getBlockBounds(EnumFacing.SOUTH,0.0, 1.0, 0.0, 0.7, 0.0, 1.0);
	private static final AxisAlignedBB WEST = CollisionHelper.getBlockBounds(EnumFacing.WEST, 0.0, 1.0, 0.0, 0.7, 0.0, 1.0);
	private static final AxisAlignedBB[] BOX = { SOUTH, WEST, NORTH, EAST };

	public YChest(String name) {
		super(name);
		setSoundType(SoundType.WOOD);
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileYChest();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(TamagottiMod.INSTANCE, TGuiHandler.YCHEST_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		return BOX[facing.getHorizontalIndex()];
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> list, @Nullable Entity entity, boolean one) {
		EnumFacing facing = state.getValue(FACING);
		super.addCollisionBoxToList(pos, box, list, BOX[facing.getHorizontalIndex()]);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		IBlockState left_block = worldIn.getBlockState(pos.offset(facing.rotateYCCW()));
		IBlockState right_block = worldIn.getBlockState(pos.offset(facing.rotateY()));
		boolean left = left_block.getBlock() instanceof YChest && left_block.getValue(FACING).equals(facing);
		boolean right = right_block.getBlock() instanceof YChest && right_block.getValue(FACING).equals(facing);
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

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, java.util.List<String> tooltip,
			ITooltipFlag advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(I18n.format(TextFormatting.YELLOW + "半径1ブロックのアイテムを吸引"));
	}
}
