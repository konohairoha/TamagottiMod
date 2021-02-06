package tamagotti.init.tile;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import tamagotti.init.blocks.block.WindMill;

public class TileWindMill extends TileTBase {

	public boolean onActive = true;

	@Override
	public void update() {
	}

	// 風車の向きを返す
	public EnumFacing getFace() {
		return this.world.getBlockState(this.pos).getValue(WindMill.FACING);
	}

	// 動いてるかどうか
	public boolean getActive() {
		return this.onActive;
	}

	public void setActive(boolean onActive) {
		this.onActive = onActive;
	}

	public float acceleration (float tick) {
		float angle = (this.world.getTotalWorldTime() + tick) / 20.0F * (180F / (float) Math.PI);
		return angle;
	}

	@Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        tags.setBoolean("active", this.getActive());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        this.setActive(tags.getBoolean("active"));
        return tags;
    }

    //クライアント側のワールドにこのTileEntity情報を登録する処理４種をオーバライドする。
    //これがないとサーバ側に持っていたこのクラスの情報をクライアント側のNBT読み込み処理で実質Null状態に初期化されるので注意
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tags = new NBTTagCompound();
		this.writeToNBT(tags);
		return new SPacketUpdateTileEntity(this.pos, -50, tags);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
	}
}
