package tamagotti.init.event;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import tamagotti.TamagottiMod;
import tamagotti.handlers.PacketHandler;
import tamagotti.key.ClientKeyHelper;
import tamagotti.packet.KeyPressPKT;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = TamagottiMod.MODID)
public class KeyPressEvent {

	// キー入力イベント
	@SubscribeEvent
	public static void keyPress(KeyInputEvent event) {
		for (KeyBinding k : ClientKeyHelper.mcToPe.keySet()) {
			if (k.isPressed()) {
				PacketHandler.sendToServer(new KeyPressPKT(ClientKeyHelper.mcToPe.get(k)));
			}
		}
	}
}
