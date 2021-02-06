package tamagotti.init.items.tool.hheld;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
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

public class H_House extends TItem implements IHeld {

	public H_House(String name) {
        super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		} else {

			IBlockState state = world.getBlockState(pos);
			if (facing == EnumFacing.UP && world.isAirBlock(pos.up()) && state.getBlockHardness(world, pos) > 0.0D) {
				if (!player.capabilities.isCreativeMode) { stack.shrink(1); }

				if (!world.isRemote) {

					IBlockState stone = Blocks.COBBLESTONE.getDefaultState();
					IBlockState air = Blocks.AIR.getDefaultState();

					//石で埋める
					for (BlockPos p : BlockPos.getAllInBox(pos.add(-4, 0, -4), pos.add(4, 5, 4))) {
						world.setBlockState(p, stone, 2);
					}

					//中を空洞に
					for (BlockPos p : BlockPos.getAllInBox(pos.add(-3, 1, -3), pos.add(3, 4, 3))) {
						world.setBlockState(p, air, 2);
					}

					//真ん中、床に灯り、天井にホワイトライトを生成
					for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, 5, -1), pos.add(1, 5, 1))) {
						world.setBlockState(p, BlockInit.w_light.getDefaultState(), 2);
					}

					//ﾄﾞｱ
					world.setBlockState(pos.add(0, 1, -4),
							Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH)
									.withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT), 2);
					world.setBlockState(pos.add(0, 2, -4),
							Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING,
									EnumFacing.SOUTH).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT)
									.withProperty( BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);

					//ベッドの置き方
					for (int x = -3; x <= -2; x++) {
						world.setBlockState(pos.add(x, 1, 3), Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING,
								EnumFacing.SOUTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 2);//頭側
						world.setBlockState(pos.add(x, 1, 2), Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING,
								EnumFacing.SOUTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT), 2);//足側
					}

					world.setBlockState(pos.add(2, 1, 3), Blocks.FURNACE.getDefaultState(), 2);
					world.setBlockState(pos.add(3, 1, 3), Blocks.CRAFTING_TABLE.getDefaultState(), 2);
					world.setBlockState(pos.add(0, 1, 0), BlockInit.akari.getDefaultState(), 2);
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
		TextComponentTranslation langtext = new TextComponentTranslation("tip.house.name", new Object[0]);
		String text = langtext.getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}

	@Override
	public AxisAlignedBB getRange(BlockPos pos) {
		int range = 4;
		return new AxisAlignedBB(pos.add(-range, -0.75, -range - 1), pos.add(range + 1, 0.25, range));
	}
}
