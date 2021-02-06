package tamagotti.init.tile;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import tamagotti.forgeenergy.Energy;
import tamagotti.forgeenergy.EnergyProxy;
import tamagotti.forgeenergy.IEnergy;
import tamagotti.init.tile.slot.StackHandler;
import tamagotti.util.TUtil;

public class TileTFEGeneBase extends TileTBase {

	// エネルギー
    public final IEnergy energy = new Energy(32000);
    public final EnergyProxy energyProxy = new EnergyProxy(this.energy, 2147483647, 0);

	// アイテムスロット
	private final ItemStackHandler stackInv = new StackHandler(this, 1);

	@Override
	public void update () {

		if (this.world.isRemote) { return; }

		// レッドストーン入力がないなら稼働
		if (TUtil.isActive(this.world, this.pos)) {

		}
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
	public int getMfProgressScaled(int value) {
		return this.isMaxEnergy() ? value : (int) (value * this.energy.getStored() / this.energy.getCapacity());
    }

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		if (CapabilityEnergy.ENERGY == capability) {
			return true;
		}

//		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//			return true;
//		}

		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		if (capability == CapabilityEnergy.ENERGY && facing != EnumFacing.UP) {
			return (T) this;
		}

//		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//			return (T) this.stackInv;
//		}

		return super.getCapability(capability, facing);
	}

	// スタックスロットの取得
	public IItemHandler getStackInv() {
		return this.stackInv;
	}

	// スタックスロットのアイテムを取得
	public  ItemStack getStackInvItem() {
		return this.getStackInv().getStackInSlot(0);
	}

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        this.writeNBT(tags);
        tags.setInteger("energy", this.energy.getStored());
		tags.setTag("StackInv", this.stackInv.serializeNBT());
        return tags;
    }

	@Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        this.readNBT(tags);
        this.energy.setStored(tags.getInteger("energy"));
		this.stackInv.deserializeNBT(tags.getCompoundTag("StackInv"));
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
}
