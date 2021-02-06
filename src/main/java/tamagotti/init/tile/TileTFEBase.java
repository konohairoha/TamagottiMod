package tamagotti.init.tile;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.api.iblock.IMFBlock;
import tamagotti.forgeenergy.Energy;
import tamagotti.forgeenergy.EnergyProxy;
import tamagotti.forgeenergy.IEnergy;
import tamagotti.handlers.PacketHandler;
import tamagotti.packet.TileFEPKT;

@Optional.Interface(iface = "sweetmagic.api.iblock.IMFBlock", modid = "sweetmagic")
public class TileTFEBase extends TileTBase implements IMFBlock {

	// エネルギー
    public final IEnergy energy = new Energy(32000);
    public final EnergyProxy energyProxy = new EnergyProxy(this.energy, 2147483647, 0);

	public int magiaFlux = 0;				// 所有しているMF
	public Set<BlockPos> posList = new HashSet<BlockPos>();	// MFブロックを保存するリスト
	public String POST = "pos";

	@Override@Optional.Method(modid = "sweetmagic")
	public void update () {

		super.update();

		// 一定時間経つと送受信をする
		if (!this.posList.isEmpty()) {
			this.sendRecivehandler();

			// クライアントに送信
			PacketHandler.sendToClient(new TileFEPKT(this.energy.getStored(), this.tickTime, this.pos));
		}
	}


	/*
	 * ====================================
	 * 			エネルギー関連
	 * ====================================
	 */

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityEnergy.ENERGY || super.hasCapability(cap, side);
	}

    // 向きを見てエネルギーを取得
	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {

		if (CapabilityEnergy.ENERGY == cap) {
			return CapabilityEnergy.ENERGY.cast(this.energyProxy);
		}
		return super.getCapability(cap, side);
	}

	// 1ごとのエネルギー消費量
    public int getEnergyUse() {
        return 1;
    }

    // エネルギーが最大かどうか
    public boolean isMaxEnergy () {
    	return this.energy.getStored() >= this.energy.getCapacity();
    }

    // エネルギーが空かどうか
    public boolean isEmptyEnergy () {
    	return this.energy.getStored() <= 0;
    }

    // ゲージ計算用
	@Override
	public int getMfProgressScaled(int value) {
		return this.isMaxEnergy() ? value : (int) (value * this.energy.getStored() / this.energy.getCapacity());
    }

	/*
	 * ====================================
	 * 			nbt保存処理開始
	 * ====================================
	 */

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        this.writeNBT(tags);
		if (!this.posList.isEmpty()) {
			this.savePosList(tags, this.posList, this.POST);
		}
        tags.setInteger("energy", this.energy.getStored());
        return tags;
    }

	@Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        this.readNBT(tags);
		if (tags.hasKey(this.POST)) {
			this.posList = this.loadAllPos(tags, this.POST);
		}
        this.energy.setStored(tags.getInteger("energy"));
    }


    // 書き込み
    public NBTTagCompound writeNBT(NBTTagCompound tags) {
		return tags;
	}

    // 読み込み
	public void readNBT(NBTTagCompound tags) { }

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

	@Override
	public void addPosList(BlockPos pos) {
		this.posList.add(pos);
	}

	@Override
	public int getMF() {
		return this.energy.getStored();
	}

	@Override
	public int getMaxMF() {
		return this.energy.getCapacity();
	}

	@Override
	public Set<BlockPos> getPosList() {
		return this.posList;
	}

	@Override
	public boolean getReceive() {
		return true;
	}

	@Override
	public int getTickTime() {
		return this.tickTime;
	}

	@Override
	public void setTickTime(int tickTime) {
		this.tickTime = tickTime;
	}

	@Override
	public void setMF(int mf) {
		this.energy.setStored(mf);
	}

	@Override
	public void setReceive(boolean arg0) {
	}

	@Override
	public int getUseMF () {
		return 1;
	}

	@Override
	public BlockPos getTilePos() {
		return this.pos;
	}

	@Override
	public World getTileWorld() {
		return this.world;
	}
}
