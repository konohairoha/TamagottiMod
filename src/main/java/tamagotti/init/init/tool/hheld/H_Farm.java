package tamagotti.init.items.tool.hheld;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.init.items.tool.tamagotti.iitem.IHeld;

public class H_Farm extends TItem implements IHeld {

	public H_Farm(String name) {
        super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		} else {

			if (facing == EnumFacing.UP && world.isAirBlock(pos.up())) {

				if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
				if (world.getBlockState(pos).getBlockHardness(world, pos) > 0.0D && !world.isRemote) {

					IBlockState farm = Blocks.FARMLAND.getDefaultState();
					IBlockState dirt = Blocks.DIRT.getDefaultState();

					int area = 4;
					for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, 0, -area), pos.add(area, 0, area))) {
						world.setBlockState(p, farm, 2);
						world.setBlockState(p.down(), dirt, 2);
						this.breakBlock(world, p.add(0, 1, 0), true);
						this.breakBlock(world, p.add(0, 2, 0), true);
					}

					world.setBlockState(pos.add(0, 0, 0), Blocks.WATER.getDefaultState(), 2);
					world.setBlockState(pos.add(0, 1, 0), BlockInit.kogen_s.getDefaultState(), 2);
				}
				world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return EnumActionResult.SUCCESS;
		}
	}

	//	ツールチップの表示 langファイルのtip.Item.name で記述
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		//xx_xx.langファイルから文字を取得する方法
		String text = new TextComponentTranslation("tip.farm.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}

	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos, boolean dropBlock) {

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

	@Override
	public AxisAlignedBB getRange(BlockPos pos) {
		int range = 4;
		return new AxisAlignedBB(pos.add(-range, -0.75, -range - 1), pos.add(range + 1, 0.25, range));
	}
}
