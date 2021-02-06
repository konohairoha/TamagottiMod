package tamagotti.handlers;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tamagotti.TamagottiMod;
import tamagotti.packet.KeyPressPKT;
import tamagotti.packet.TileFEPKT;
import tamagotti.packet.UpdateGemModePKT;

public class PacketHandler {

	private static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(TamagottiMod.MODID);

	public static void register() {
		int disc = 0;
		HANDLER.registerMessage(UpdateGemModePKT.Handler.class, UpdateGemModePKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(KeyPressPKT.Handler.class, KeyPressPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(TileFEPKT.Handler.class, TileFEPKT.class, disc++, Side.CLIENT);
	}

	public static void sendToServer(IMessage msg) {
		HANDLER.sendToServer(msg);
	}

	public static void sendServer(IMessage msg) {
		HANDLER.sendToServer(msg);
	}

	public static void sendToClient(IMessage msg) {
		HANDLER.sendToAll(msg);
	}

}
