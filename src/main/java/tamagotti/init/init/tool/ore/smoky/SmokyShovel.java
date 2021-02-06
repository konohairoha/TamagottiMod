package tamagotti.init.items.tool.ore.smoky;

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
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TShovel;
import tamagotti.util.WorldHelper;

public class SmokyShovel extends TShovel {

	private final int cycle;

	public SmokyShovel(String name, ToolMaterial material, int size) {
		super(name, material);
        this.cycle = size;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {
		
		if (state.getBlockHardness(world, pos) > 0.0D && !world.isRemote) {
			
			EntityPlayer player = (EntityPlayer) living;
			EnumFacing sideHit = rayTrace(world, player, false).sideHit;
			int xa = 0, za = 0;	//向きに合わせて座標を変えるための変数
			int area = this.cycle + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;
			int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

			//上と下以外は採掘する座標を変える
            switch (sideHit) {
                case UP:
                case DOWN:
                	break;
                case NORTH:
                	za = area;
                	break;
                case SOUTH:
                	za = -area;
                	break;
                case EAST:
                	xa = -area;
                	break;
                case WEST:
                	xa = area;
                	break;
            }

    		//リストの作成（めっちゃ大事）
    		List<ItemStack> drop = new ArrayList<>();

			for (int x = -area + xa; x <= area + xa; x++) {
				for (int z = -area + za; z <= area + za; z++) {
					for (int y = 0; y <= area * 2; y++) {

						//ブロックを取得するための定義
						BlockPos blockPos = pos.add(x, y, z);
						IBlockState target = world.getBlockState(blockPos);
						Block block = target.getBlock();

						//空気ブロックとたいるえんちちーなら何もしない
						if(block == Blocks.AIR || block.hasTileEntity(target)){ continue; }

						drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, blockPos, canSilk, FOURTUNE));
						world.setBlockToAir(blockPos);
					}
				}
			}

			//リストに入れたアイテムをドロップさせる
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			stack.damageItem(1, living);
		}
		return true;
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if(this.cycle == 1) {
			return repair.getItem() == ItemInit.fluorite ? true : super.getIsRepairable(toRepair, repair);
		}
		return repair.getItem() == ItemInit.smokyquartz ? true : super.getIsRepairable(toRepair, repair);
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		int area = this.cycle * 2 + 1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) < 1) {
			tooltip.add(I18n.format(TextFormatting.RED + "効率のエンチャントで採掘範囲増加"));
		}
		tooltip.add(I18n.format(TextFormatting.BLUE + (area + "×" + area + "×" + area + "を採掘")));
	}
}
