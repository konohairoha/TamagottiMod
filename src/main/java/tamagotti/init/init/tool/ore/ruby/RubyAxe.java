package tamagotti.init.items.tool.ore.ruby;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
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
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.TAxe;

public class RubyAxe extends TAxe {

	public RubyAxe(String name, ToolMaterial material, float damage, float speed) {
		super(name, material, damage, speed);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) { return EnumActionResult.FAIL; }

		int area = 4 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, -area, -area), pos.add(area, 0, area))) {
			Block block = world.getBlockState(p).getBlock();
			Material material = world.getBlockState(p).getMaterial();
			if (block == Blocks.AIR || material == Material.WATER || material == Material.LAVA || material == Material.VINE) {
				world.setBlockState(p, Blocks.PLANKS.getDefaultState(), 2);
			}
		}
		world.playSound(null, pos, TSoundEvent.CHANGE, SoundCategory.NEUTRAL, 0.25F, 1.0F);
		stack.damageItem(1, player);
		return EnumActionResult.SUCCESS;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int area = 4 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
  		int range = area * 2 + 1;
  		tooltip.add(I18n.format(TextFormatting.GOLD + "ブロックを右クリックで下に"));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "" + range + "×" + (area + 1)  + "×" + range + "に木材で埋める"));
    }
}