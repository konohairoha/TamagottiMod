package tamagotti.init.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import tamagotti.init.items.tool.tamagotti.TBookNeo;
import tamagotti.init.tile.inventory.InventoryTBookNeo;

public class EntityItemPickEvent {

	@SubscribeEvent
	public void onEntityItemPickupEvent(EntityItemPickupEvent event) {

		// えんちちーの取得してしてるなら終了
		EntityItem entity = event.getItem();
		if (entity.isDead) { return; }


		// プレイヤー取得
		EntityPlayer player = event.getEntityPlayer();
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;
		ItemStack entityStack = entity.getItem();

		// インベントリの数だけ回す
		for (int i = 0; i < pInv.size(); i++) {

			// アイテムスタックを取得し空なら終了
			ItemStack stack = pInv.get(i);
			if (stack.isEmpty() || !(stack.getItem() instanceof TBookNeo)) { continue; }

			// nbtを取得し働状態じゃないなら終了
			NBTTagCompound tags = stack.getTagCompound();
			if (tags == null || !tags.getBoolean("Active")) { continue; }

			// インベントリを取得
			InventoryTBookNeo neo = new InventoryTBookNeo(stack, player);
			IItemHandlerModifiable inv = neo.inventory;

			for (int k = 0; k < inv.getSlots(); k++) {

				// クラネオインベントリ内のアイテムを取得
				ItemStack st = inv.insertItem(k, entityStack.copy(), true);
				if (!st.isEmpty()) { continue; }

				inv.insertItem(k, entityStack.copy(), false);
				neo.writeBack();
				entity.setDead();
		        event.setCanceled(true);
				player.getEntityWorld().playSound(null, new BlockPos(player), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.33F, 1.7F);
				return;
			}
		}
	}
}
