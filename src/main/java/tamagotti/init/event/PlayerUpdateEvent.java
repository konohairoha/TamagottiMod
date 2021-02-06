package tamagotti.init.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.TamagottiMod;
import tamagotti.init.PotionInit;
import tamagotti.util.TUtil;

public class PlayerUpdateEvent {

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {

		if (!(event.getEntity() instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) event.getEntity();
		if (!player.isPotionActive(PotionInit.concentration)) { return; }

		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemBow)) { return; }

		if (player.onGround || player.isInWater() || !player.isHandActive()) { return; }

		World world = player.world;

		if (!world.isRemote) {
			EntityPlayerMP playerMP = ((EntityPlayerMP) player);
			playerMP.fallDistance = 0;
		}

		if(!TamagottiMod.proxy.isJumpPressed()) {
			player.motionY *= 0.55;
		}

		PotionEffect effect = player.getActivePotionEffect(PotionInit.concentration);

		for (int i = 0; i < effect.getAmplifier() + 2; i++) {
			this.tickHeldBow(player);
		}
	}

	private void tickHeldBow(EntityPlayer player) {
		TUtil.callPrivateMethod(EntityLivingBase.class, player, "updateActiveHand", "func_184608_ct");
	}
}
