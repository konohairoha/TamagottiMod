package tamagotti.init.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.init.PotionInit;
import tamagotti.init.render.RenderPara;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TamagottiMod.MODID, value = Side.CLIENT)
public class RenderItemEvent {

	@SubscribeEvent
	public static void renderLivingPost(RenderLivingEvent.Post<EntityLivingBase> event) {
		if (event.getEntity().isPotionActive(PotionInit.sticky)) {
			EntityLivingBase entity = event.getEntity();
			RenderPara para = new RenderPara();
			para.render(entity, event.getRenderer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick(), false);
		}
	}
}
