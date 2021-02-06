package tamagotti.init.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import tamagotti.init.items.tool.tamagotti.iitem.IAmulet;

public class PDethEvent {

	protected HashMap<String, InventoryPlayer> keepInveontoryMap = new HashMap<String, InventoryPlayer>();
	private static final Map<UUID, InventoryPlayer> playerKeepsMap = new HashMap<>();
	int xpValue;

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {

		if (event.getEntityLiving() == null) { return; }

		EntityLivingBase living = event.getEntityLiving();
		if (living.world.isRemote) { return; }

		if (living instanceof EntityPlayer && !living.world.getGameRules().getBoolean("keepInventory")) {

			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			int pnv = player.inventory.mainInventory.size();

			for (int inv = 0; inv < pnv; inv++) {
				ItemStack stack = player.inventory.getStackInSlot(inv);
				if (stack != null && stack.getItem() instanceof IAmulet) {

					IAmulet amu = (IAmulet) stack.getItem();

					// 必要経験値量の取得
					if (player.xpBarCap() >= amu.needExp()) {

						this.xpValue = player.experienceLevel;

						if (amu.isShrink()) {
							stack.shrink(1);
						}

						this.xpValue -= amu.needExp();

						InventoryPlayer keepInventory = new InventoryPlayer(null);
						UUID playerUUID = player.getUniqueID();
						keepAllArmor(player, keepInventory);
						keepOffHand(player, keepInventory);
						for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
							keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
							player.inventory.mainInventory.set(i, ItemStack.EMPTY);
						}
						playerKeepsMap.put(playerUUID, keepInventory);
					}
				}
			}
		}
	}

	private static void keepAllArmor(EntityPlayer player, InventoryPlayer keepInventory) {
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			keepInventory.armorInventory.set(i, player.inventory.armorInventory.get(i).copy());
			player.inventory.armorInventory.set(i, ItemStack.EMPTY);
		}
	}

	private static void keepOffHand(EntityPlayer player, InventoryPlayer keepInventory) {
		for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
			keepInventory.offHandInventory.set(i, player.inventory.offHandInventory.get(i).copy());
			player.inventory.offHandInventory.set(i, ItemStack.EMPTY);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

		EntityPlayer player = event.player;
		InventoryPlayer keepInventory = playerKeepsMap.remove(player.getUniqueID());

		if (keepInventory != null) {
			NonNullList<ItemStack> displaced = NonNullList.create();
			for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
				ItemStack kept = keepInventory.armorInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.armorInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}

			for (int i = 0; i < player.inventory.offHandInventory.size(); i++){
				ItemStack kept = keepInventory.offHandInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.offHandInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}

			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				ItemStack kept = keepInventory.mainInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.mainInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 1));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 0));
			player.addExperienceLevel(this.xpValue);
		}
	}

	@SubscribeEvent
	public static void onPlayerLogout(PlayerLoggedOutEvent event) {
		dropStoredItems(event.player);
	}

	private static void dropStoredItems(EntityPlayer player) {
		InventoryPlayer keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			keepInventory.player = player;
			keepInventory.dropAllItems();
		}
	}
}
