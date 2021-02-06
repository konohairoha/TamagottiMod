package tamagotti.util;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tamagotti.init.ItemInit;

public class TMobLootTable {

	//stringで判断してアイテムを返す
	public static ItemStack loot(int id) {

		ItemStack item = null;
		Random rand = new Random();
		int aRand = rand.nextInt(3);
		int count = rand.nextInt(6) + 1;

		/**
		 * 0 = ゾンビマスター
		 * 1 = スカルフレイム
		 * 2 = バーニングスカルフレイム
		 * 3 = LCエヴォーカー
		 * 4 = LCエヴォーカーのうわさ
		 * 5 = クモグス
		 */

		switch(id) {
		case 0:
			count = rand.nextInt(8) + 2;
			if (aRand == 0) {
				item = new ItemStack(ItemInit.smokyquartz, count);
			} else {
				item = new ItemStack(ItemInit.unknown_meat, count);
			}
			break;
		case 1:
			if (aRand != 0) {
				item = new ItemStack(ItemInit.redberyl, count);
			} else {
				item = new ItemStack(Items.SKULL, count, 0);
			}
			break;
		case 2:
			if (aRand != 0) {
				item = new ItemStack(ItemInit.ruby, count);
			} else {
				item = new ItemStack(ItemInit.smokyquartz, count);
			}
			break;
		case 3:
			if (aRand != 0) {
				item = new ItemStack(ItemInit.starrosequartz, count);
			} else {
				item = new ItemStack(ItemInit.tamulet, 1);
			}
			break;
		case 4:
			if (aRand != 0) {
				item = new ItemStack(ItemInit.srq_ex, count + 3);
			} else {
				item = new ItemStack(ItemInit.tamulet, count);
			}
			break;
		case 5:
			if (aRand != 0) {
				item = new ItemStack(ItemInit.starrosequartz, count + 3);
			} else {
				item = new ItemStack(Items.SKULL, count);
			}
			break;
		}
		return item;
	}
}
