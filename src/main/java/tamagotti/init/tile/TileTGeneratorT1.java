package tamagotti.init.tile;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import tamagotti.forgeenergy.Action;
import tamagotti.init.ItemInit;
import tamagotti.init.tile.slot.StackHandler;
import tamagotti.util.TUtil;

public class TileTGeneratorT1 extends TileTFEGeneBase {

	// 燃焼スロット
	private final ItemStackHandler fuelInv = new StackHandler(this, 1);

	@Override
	public void update () {

		if (this.world.isRemote) { return; }

		// レッドストーン入力がないなら稼働
		if (TUtil.isActive(this.world, this.pos) && !this.isMaxEnergy()) {

			// 燃焼開始
			this.doBurning();

		}
	}

	// 燃焼開始
	public void doBurning () {

		// 燃焼スロットの取得
		ItemStack stack = this.getFuelInvItem();
		if (stack.isEmpty()) { return; }

		// エネルギー取得
		int burnTime = this.getBurnTime(stack);
		if (burnTime <= 0) { return; }

		// エネルギー投入
		this.energy.insert(burnTime, Action.PERFORM);
		stack.shrink(1);
	}

	// エネルギー取得
	public int getBurnTime (ItemStack stack) {

		int burnTime = 0;
		Item item = stack.getItem();

		if (item == ItemInit.tamagotti) {
			burnTime = 1000;
		} else if (item == ItemInit.tamagottineo) {
			burnTime = 10000;
		} else if (item == ItemInit.tamagottineo) {
			burnTime = 10000;
		} else if (item == ItemInit.tamagotticustom) {
			burnTime = 40000;
		} else if (item == ItemInit.tamagottilink) {
			burnTime = 100000;
		}

		return burnTime;
	}

	// 燃焼スロットの取得
	public IItemHandler getFuelInv() {
		return this.fuelInv;
	}

	// 燃焼スロットのアイテムを取得
	public  ItemStack getFuelInvItem() {
		return this.getFuelInv().getStackInSlot(0);
	}

    // 書き込み
    public NBTTagCompound writeNBT(NBTTagCompound tags) {
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		return tags;
	}

    // 読み込み
	public void readNBT(NBTTagCompound tags) {
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
	}
}
