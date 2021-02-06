package tamagotti.init.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.TamagottiMod;
import tamagotti.init.ItemInit;

public class TLootTableEvent {

	@SubscribeEvent
	public void onEvent(LootTableLoadEvent event) {	//ルートテーブル読み込み用イベント

		//↓2行はルートテーブルへの追加に必須。
		LootTable loot = event.getTable();
		LootPool pool = loot.getPool("main");
		ResourceLocation res = event.getName();

		if (pool == null) { return ; }

		if (res.equals(LootTableList.GAMEPLAY_FISHING_FISH)) {	//Item, Weight(大きいほどレア度低め？), Quality(魚：-1、ごみ：-2、お宝：2), LootFunction(↑のコメントアウトしているほうはデータ値を与える。), LootCondition, ModID+":itemID"
			pool.addEntry(new LootEntryItem(ItemInit.unknown_meat, 10, -2, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":unknown_meat"));
			pool.addEntry(new LootEntryItem(ItemInit.shrimp, 25, -1, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":shrimp"));
			pool.addEntry(new LootEntryItem(ItemInit.tamagottineo, 3, 2, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":tamagottineo"));
			pool.addEntry(new LootEntryItem(ItemInit.tamagottipickaxeneo, 1, 2, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":tamagottipickaxeneo"));
			pool.addEntry(new LootEntryItem(ItemInit.tamagottiaxeneo, 1, 2, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":tamagottiaxeneo"));
			pool.addEntry(new LootEntryItem(ItemInit.tamagottishovelneo, 1, 2, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":tamagottishovelneo"));
			pool.addEntry(new LootEntryItem(ItemInit.tamagottiswordneo, 1, 2, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":tamagottiswordneo"));
		}

		//村のチェスト
		if (res.equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
			pool.addEntry(new LootEntryItem(ItemInit.tsail, 20, 8, new LootFunction[0], new LootCondition[0], TamagottiMod.MODID + ":tsal"));
		}
	}
}
