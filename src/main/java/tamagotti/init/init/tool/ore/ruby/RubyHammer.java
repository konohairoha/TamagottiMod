package tamagotti.init.items.tool.ore.ruby;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
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
import tamagotti.init.items.tool.tamagotti.TPick;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class RubyHammer extends TPick {

	public RubyHammer(String name, ToolMaterial material) {
		super(name, material);
	}

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 6, 0));
		}
		return multimap;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack) && !world.isRemote) { return EnumActionResult.FAIL; }

		List<ItemStack> drop = new ArrayList<>();
		int area = 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
		boolean canSilk = silk > 0;
		int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, -4, -area), pos.add(area, 0, area))) {
			IBlockState target = world.getBlockState(p);
			Block block = target.getBlock();

			//空気ブロックとたいるえんちちーなら何もしない
			if (block == Blocks.AIR || block == Blocks.BEDROCK || block.hasTileEntity(target)) { continue; }

			//リストに入れる
			List<ItemStack> bdrop = WorldHelper.getBlockDrops(world, player, target, block, p, canSilk, FOURTUNE);
			drop.addAll(bdrop);
			world.setBlockToAir(p);
		}

		WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
		world.playSound( null, pos, TSoundEvent.CHANGE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		stack.damageItem(1, player);
		return EnumActionResult.SUCCESS;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int area = (2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) * 2 + 1;
  		tooltip.add(I18n.format(TextFormatting.GOLD + "ブロックを右クリックで下に"));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "" + area + "×" + 4 + "×" + area + "をブロック破壊"));
    }
}
