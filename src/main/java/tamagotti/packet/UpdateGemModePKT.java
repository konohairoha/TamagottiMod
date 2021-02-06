package tamagotti.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tamagotti.init.items.tool.tamagotti.TBag;

public class UpdateGemModePKT implements IMessage {

	private int x;
	private int y;
	private int z;
	private int dim;

	public UpdateGemModePKT() {}

	public UpdateGemModePKT(int x, int y, int z, int dim) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.dim = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.dim);
	}

	public static class Handler implements IMessageHandler<UpdateGemModePKT, IMessage> {

		@Override
		public IMessage onMessage(final UpdateGemModePKT pkt, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {
				

				@Override
				public void run() {
					ItemStack stack = ctx.getServerHandler().player.getHeldItem(EnumHand.MAIN_HAND);
					if (stack.isEmpty())
						stack = ctx.getServerHandler().player.getHeldItem(EnumHand.OFF_HAND);

					if (!stack.isEmpty() && (stack.getItem() instanceof TBag)) {
						stack.getTagCompound().setInteger("x", pkt.x);
						stack.getTagCompound().setInteger("y", pkt.y);
						stack.getTagCompound().setInteger("z", pkt.z);
						stack.getTagCompound().setInteger("dim", pkt.dim);
					}
				}
			});

			return null;
		}
	}
}
