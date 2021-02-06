package tamagotti.init.blocks.chest.gc;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import tamagotti.init.blocks.chest.wadansu.ContainerWadansuChest;
import tamagotti.init.tile.TileBaseChest;

public class TileGCChest extends TileBaseChest implements ITickable {

	private static final int[] SLOTS = new int[54];
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(54, ItemStack.EMPTY);
	public int ontick = 0;
	private boolean hasBeenCleared;
	public boolean removeTick = false;		//TickUpdate用Removeフラグ

	@Override
	public void update() {

		this.ontick++;

		//30秒経ったら
		if(this.ontick % 600 == 0) {

			//捨てる処理のタイミングで音を鳴らす
			this.world.playSound(null, this.pos, SoundEvents.BLOCK_SAND_BREAK,
					SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.19F + 0.9F);
			this.removeTick = true;
			this.ontick = 0;
			this.saveToNbt(new NBTTagCompound()); 	//ここでチェストの中身のNBTの更新を行う。saveToNbtメソッド参照。
			this.markDirty();	//一応マルチのことを考えてクライアントへのNBT書き換え通知もしておく。
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tags) {
		this.chestContents = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if (!this.checkLootAndRead(tags) && tags.hasKey("Items", 9)) {
			ItemStackHelper.loadAllItems(tags, this.chestContents);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tags) {
		if (!this.checkLootAndWrite(tags)) {
			ItemStackHelper.saveAllItems(tags, this.chestContents, false);
		}
		// TickのUpdate処理でremoveTickフラグが建つ。
		if (this.removeTick) {
			this.chestContents.clear(); //チェストの中身をすべて取得して空にする超便利メソッドがあった。
			this.removeTick = false;
		}
		return tags;
	}

	public static ItemStack getAndRemove(List<ItemStack> stacks, int index) {
		return index >= 0 && index < stacks.size() ? (ItemStack) stacks.set(index, ItemStack.EMPTY) : ItemStack.EMPTY;
	}

	// IInventoryの実装
	// IInventoryはインベントリ機能を提供するインタフェース. インベントリに必要なメソッドを適切にオーバーライド
	// Inventoryの要素数を返すメソッド
	@Override
	public int getSizeInventory() {
		return this.chestContents.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.chestContents) {
			if (!itemstack.isEmpty()) { return false; }
		}
		return true;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	static {
		for (int i = 0; i < SLOTS.length; SLOTS[i] = i++) { ; }
	}

	@Override
	public void clear() {
		this.hasBeenCleared = true;
		super.clear();
	}

	@Override
	public boolean isCleared() {
		return this.hasBeenCleared;
	}

	@Override
	public String getName() {
		return "guicupboard";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerWadansuChest(playerIn, this);
	}

	@Override
	public String getGuiID() {
		return "CupBoard";
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.chestContents;
	}
}
