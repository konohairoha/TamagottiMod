package tamagotti.init.items.tool.ore.silver;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TPick;
import tamagotti.util.WorldHelper;

public class SilverHammer extends TPick {

	public SilverHammer(String name, ToolMaterial material) {
		super(name, material);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (state.getBlockHardness(world, pos) > 0.0D && !world.isRemote) {

    		//リストの作成（めっちゃ大事）
    		List<ItemStack> drop = new ArrayList<>();
			EntityPlayer player = (EntityPlayer) living;
			EnumFacing sideHit = rayTrace(world, player, false).sideHit;
			int area = 1, xa = 0, za = 0;
			int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;
			int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

            switch (sideHit) {
                case UP:
                	xa = 0;
                	za = 0;
                case DOWN:
                	break;
                case NORTH:
                	za = 1;
                	break;
                case SOUTH:
                	za = -1;
                	break;
                case EAST:
                	xa = -1;
                	break;
                case WEST:
                	xa = 1;
                	break;
            }

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-area + xa, 0, -area + za), pos.add(area + xa, area * 2, area + za))) {
				IBlockState target = world.getBlockState(p);
				Block blocks = target.getBlock();

				if (blocks == Blocks.AIR || blocks.hasTileEntity(target) || blocks == Blocks.BEDROCK ||
						blocks == Blocks.FLOWING_LAVA || blocks == Blocks.FLOWING_WATER) {
					continue;
				}

				drop.addAll(WorldHelper.getBlockDrops(world, player, target, blocks, p, canSilk, FOURTUNE));
				world.setBlockToAir(p);
			}

			//リストに入れたアイテムをドロップさせる
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			stack.damageItem(1, living);
		}
		return true;
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.BLUE + "3×3×3を採掘"));
	}
}
