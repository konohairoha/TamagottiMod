package tamagotti.init.items.tool.ore.ruby;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.TPick;
import tamagotti.init.items.tool.tamagotti.iitem.IMode;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class RubyShovel extends TPick implements IMode {

	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;
	Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);

	public RubyShovel(String name, ToolMaterial material) {
		super(name, material);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.SNOW_LAYER) {
			return true;
		} else {
			return block == Blocks.SNOW;
		}
	}

	@Override
	public void changeMode(ItemStack stack, EntityPlayer player) {
		NBTTagCompound tags = ItemHelper.getNBT(stack);
		boolean active = !tags.getBoolean(TAG_ACTIVE);
		tags.setBoolean(TAG_ACTIVE, active);
		player.sendMessage(new TextComponentString(active ? "砂埋め立てモード" : "土埋め立てモード"));
		player.getEntityWorld().playSound(null, new BlockPos(player),
				active ? SoundEvents.BLOCK_CLOTH_BREAK : SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.NEUTRAL, 2F, 1F);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return TUtil.shouldCauseReequipAnimation(oldStack, newStack, slotChanged, TAG_ACTIVE, TAG_MODE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(face), face, stack)) { return EnumActionResult.FAIL; }

		int area = 4 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, -area, -area), pos.add(area, 0, area))) {
			Block block = world.getBlockState(p).getBlock();
			Material material = world.getBlockState(p).getMaterial();
			if (block == Blocks.AIR || material == Material.WATER || material == Material.LAVA || material == Material.VINE) {
				if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {
					world.setBlockState(p, Blocks.DIRT.getDefaultState(), 2);
				} else {
					world.setBlockState(p, Blocks.SAND.getDefaultState(), 2);
				}
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
		tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.MODE) + "キーで設置ブロック切り替え"));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "ブロックを右クリックで下に"));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "" + range + "×" + (area + 1)  + "×" + range + "に土で埋める"));
    }
}
