package tamagotti.init.event;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;

public class THarvestEvent {

	// ブロック破壊時イベント
	@SubscribeEvent
	public void onMining(BlockEvent.HarvestDropsEvent event) {

		World world = event.getWorld();
		if (event.getHarvester() == null || !(event.getHarvester() instanceof EntityPlayer) || world.isRemote) { return; }

		Block block = event.getState().getBlock();
		if (block == Blocks.AIR) { return; }

		EntityPlayer player = event.getHarvester();
		ItemStack held = player.getHeldItemMainhand();
		Random rand = world.rand;
		List<ItemStack> dropList = event.getDrops();

		/**	タケノコのドロップ　この書き方だと条件満たしてれば何度もドロップできるので
		 *		乱数ー乱数でちょっと個数を極端にする。植えるの大変だし
		 *		大丈夫でしょ（白目）
		*/
		Item item = held.getItem(); //メインハンドに持ってるアイテムを取得
		BlockPos pos = event.getPos();

		if (block == BlockInit.bamboo && item instanceof ItemHoe) {

			if (world.getBlockState(pos.down()).getBlock() != BlockInit.bamboo) {
				dropList.add(new ItemStack(BlockInit.bamboo_s, rand.nextInt(3) + 1));
				held.damageItem(16, player);
			}
		} else if (block == Blocks.MOB_SPAWNER && item instanceof ItemPickaxe) {

			int a = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, held);

			//シルクタッチついてたら確定
			if (a > 0) {
				dropList.add(new ItemStack(ItemInit.bspawner));

			//ついてなかったら確率
			} else if(rand.nextInt(9) >= 6 - a) {
				dropList.add(new ItemStack(ItemInit.bspawner));
			}
		}
	}
}
