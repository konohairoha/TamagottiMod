package tamagotti.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tamagotti.init.items.armor.MArmor;
import tamagotti.init.items.tool.tamagotti.iitem.IMode;
import tamagotti.init.items.tool.tamagotti.iitem.IRelode;
import tamagotti.key.TKeybind;

public class KeyPressPKT implements IMessage {

	public TKeybind key;

	public KeyPressPKT() {
	}

	public KeyPressPKT(TKeybind key) {
		this.key = key;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = TKeybind.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.key.ordinal());
	}

	public static class Handler implements IMessageHandler<KeyPressPKT, IMessage> {

		@Override
		public IMessage onMessage(final KeyPressPKT message, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				@Override
				public void run() {

					EntityPlayerMP player = ctx.getServerHandler().player;
					ItemStack stack = player.getHeldItemMainhand();

					// 防具はfor文で回す前に処理しないと二重処理が発生
					if (message.key == TKeybind.MODE) {

						ItemStack item = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

						if (stack.isEmpty() && item.getItem() instanceof MArmor) {

							MArmor armor = (MArmor) item.getItem();
							armor.changeMode(player, item);
							return;
						}
					}

					// 押したキーで処理
					switch (message.key) {
					case RELODE:
						if (stack.getItem() instanceof IRelode) {
							IRelode relode = (IRelode) stack.getItem();
							relode.doRelode(player, stack);
						}
						return;
					case MODE:
						if (stack.getItem() instanceof IMode) {
							IMode mode = (IMode) stack.getItem();
							mode.changeMode(stack, player);
						}
						return;
					default:
						break;
					}
				}
			});
			return null;
		}
	}
}
