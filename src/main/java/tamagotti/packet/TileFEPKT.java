package tamagotti.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tamagotti.init.tile.TileTFEBase;

public class TileFEPKT implements IMessage {

	public int energy;
	public int x;
	public int y;
	public int z;
	public int tickTime;

	public TileFEPKT() {
	}

	public TileFEPKT(int energy, int tickTime, BlockPos pos) {
		this.energy = energy;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.tickTime = tickTime;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.energy = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.tickTime = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.energy);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.tickTime);
	}

	public static class Handler implements IMessageHandler<TileFEPKT, IMessage> {

		@Override
		public IMessage onMessage(final TileFEPKT msg, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					// プレイヤーを取得
					EntityPlayer player = Minecraft.getMinecraft().player;

					if (player == null) { return; }

					BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
					TileEntity tile = player.world.getTileEntity(pos);

					if (!(tile instanceof TileTFEBase)) { return; }

					TileTFEBase febase = (TileTFEBase) tile;
					int energy = msg.energy;
					febase.energy.setStored(energy);
					febase.tickTime = msg.tickTime;

				}
			});
			return null;

		}
	}
}
