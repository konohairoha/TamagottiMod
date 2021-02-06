package tamagotti.init.items.tool.cook;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.util.TUtil;

public class Riter extends TItem {

	public Riter(String name) {
        super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(512);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        pos = pos.offset(facing);
        ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos, facing, stack)) { return EnumActionResult.FAIL; }

		if (stack.getItemDamage() < this.getMaxDamage(stack)) {

			if (world.isAirBlock(pos)) {
    			world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
    			world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 2);
    		}

			if (player instanceof EntityPlayerMP) {
    			CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
    		}

			stack.damageItem(1, player);

    	} else {

        	// 耐久回復(消費アイテム、データ値、個数、回復量)
        	TUtil.itemRecovery(player, stack, ItemInit.minioiltank, 0, 1, this.getMaxDamage(stack));
    	}
    	return EnumActionResult.SUCCESS;
    }

    @Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		int damage = stack.getMaxDamage() - stack.getItemDamage();
		if(damage == 0) {
			tooltip.add(I18n.format(TextFormatting.RED + "オイルタンクをインベントリに入れた状態で地面を右クリック"));
		} else {
			tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックで着火"));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "油の残量： " + damage));
		}
	}
}
