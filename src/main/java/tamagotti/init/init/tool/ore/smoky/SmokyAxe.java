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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import tamagotti.init.items.tool.tamagotti.TAxe;
import tamagotti.util.WorldHelper;

public class SmokyAxe extends TAxe {

	private final int range;

	public SmokyAxe(String name, ToolMaterial material, float damage, float speed, int meta) {
		super(name, material, damage, speed);
		this.range = meta;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (state.getBlockHardness(world, pos) > 0.0D && !world.isRemote) {

			EntityPlayer player = (EntityPlayer) living;
			int area = this.range + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			if (stack.hasDisplayName()) { area += 2; }
			int upPos = 0;
			int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;
			int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

    		//リストの作成（めっちゃ大事）
    		List<ItemStack> drop = new ArrayList<>();

    		int maxY = (pos.getY() + area * 2 + upPos) >= 256 ? 256 : area * 2 + upPos;

			for (int y = 0; y <= maxY; y++) {
				for (int x = -area; x <= area; x++) {
					for (int z = -area; z <= area; z++) {

						//ブロックを取得するための定義
						BlockPos p1 = pos.add(x, y, z);
						IBlockState target = world.getBlockState(p1);
						Block block = target.getBlock();

						//空気ブロックとたいるえんちちーなら何もしない
						if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }

						String oreName;

						try {
							oreName = this.checkLog(OreDictionary.getOreIDs(new ItemStack(block)));
						}

						catch (Throwable e) { continue; }

						// 鉱石辞書の名前が原木か葉っぱなら範囲採掘
						if (oreName.equals("logWood") || oreName.equals("treeLeaves")) {

							// 原木かつupPosを++してないとき
							if (oreName.equals("logWood") && (p1.getY() - pos.getY()) != upPos) {

								try {
									Block upBlock = world.getBlockState(p1.up()).getBlock();

									// 上のブロックが原木のとき
									if (this.checkUpLog(OreDictionary.getOreIDs(new ItemStack(upBlock)))) {
										upPos++;
									}
								} catch (Throwable e) { }
							}

							drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p1, canSilk, FOURTUNE));
							world.setBlockToAir(p1);

						}
					}
				}
			}

			//リストに入れたアイテムをドロップさせる
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			stack.damageItem(1, player);
		}
		return true;
	}

	public String checkLog (int[] recipeId) {

		String recipe = "";

		if (recipeId.length == 0) { return recipe; }

		for (int i = 0; i < recipeId.length; i++) {

			String name = OreDictionary.getOreName(recipeId[i]);

			if (name.equals("logWood") || name.equals("treeLeaves")) {
				recipe = OreDictionary.getOreName(recipeId[i]);
				break;
			}
		}
		return recipe;
	}

	public boolean checkUpLog (int[] recipeId) {

		if (recipeId.length == 0) { return false; }

		for (int i = 0; i < recipeId.length; i++) {
			if (OreDictionary.getOreName(recipeId[i]).equals("logWood")) { return true; }
		}

		return false;
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		int add = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		int area = (this.range + add) * 2 + 1;
		if (stack.hasDisplayName()) {
			area += 4;
			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：範囲増加 + 4"));
		}
		if (add < 1) {
			tooltip.add(I18n.format(TextFormatting.RED + "効率のエンチャントで採掘範囲増加"));
		}
		tooltip.add(I18n.format(TextFormatting.BLUE + "木の高さにっよて高さ上昇"));
		tooltip.add(I18n.format(TextFormatting.BLUE + (area + "×" + area + "×" + area + "を採掘")));
	}
}
