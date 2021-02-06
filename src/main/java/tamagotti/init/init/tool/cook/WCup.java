package tamagotti.init.items.tool.cook;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.init.items.tool.tamagotti.TItem;

public class WCup extends TItem {

    public WCup(String name) {
        super(name);
    }

    @Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {

        pos = pos.offset(facing);
        ItemStack stack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos, facing, stack)) { return EnumActionResult.FAIL; }

		if (world.isAirBlock(pos) || world.getBlockState(pos.add(0, 0, 0)).getBlock() == Blocks.WATER) {

			world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
			world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 11);

			if (!player.capabilities.isCreativeMode) {
	            stack.shrink(1);
	        }
		}

		if (player instanceof EntityPlayerMP) {
			CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
		}

		return EnumActionResult.SUCCESS;
    }
}