package tamagotti.init.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.init.PotionInit;

public class UpdateTickPlayerEvent {

	@SubscribeEvent
	public void renderfov(FOVUpdateEvent event) {

		EntityPlayer player = event.getEntity();
		if (!player.isPotionActive(PotionInit.sticky)) { return; }

		PotionEffect effect = player.getActivePotionEffect(PotionInit.sticky);
		int level = effect.getAmplifier() + 1;
		float move= (float) (0.9 - level * 0.1);

		if (player.moveForward > 0 && player.motionX * player.motionX + player.motionY * player.motionY +
				player.motionZ * player.motionZ < 3) {
			player.motionX *= move;
			player.motionZ *= move;
		}

		if (player.motionY > 0) {
			player.motionY *= move;
		} else if (player.motionY < 0 && player.motionY > -3) {
			player.motionY *= 1.1;
		}
	}
}
