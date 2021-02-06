package tamagotti.init.tile;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class TileBaseChest extends TileEntityLockableLoot implements ISidedInventory {

	private static final int[] SLOTS = new int[54];
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(54, ItemStack.EMPTY);
	private boolean hasBeenCleared;
	private boolean destroyedByCreativePlayer;

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.loadFromNbt(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		return this.saveToNbt(compound);
	}

	public void loadFromNbt(NBTTagCompound compound) {
		this.chestContents = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if (!this.checkLootAndRead(compound) && compound.hasKey("Items", 9)) {
			ItemStackHelper.loadAllItems(compound, this.chestContents);
		}
	}

	public NBTTagCompound saveToNbt(NBTTagCompound compound) {
		if (!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.chestContents, false);
		}
		return compound;
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(pos, -50, nbtTagCompound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	// IInventoryの実装
	// IInventoryはインベントリ機能を提供するインタフェース. インベントリに必要なメソッドを適切にオーバーライド
	// Inventoryの要素数を返すメソッド
	@Override
	public int getSizeInventory() {
		return this.chestContents.size();
	}

	// 1スロットあたりの最大スタック数
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	// 主にContainerで利用する, GUIを開けるかどうかを判定するメソッド
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.chestContents) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
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

	public boolean isCleared() {
		return this.hasBeenCleared;
	}

	public boolean shouldDrop() {
		return !this.isDestroyedByCreativePlayer() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
	}

	public boolean isDestroyedByCreativePlayer() {
		return this.destroyedByCreativePlayer;
	}

	public void setDestroyedByCreativePlayer(boolean par1) {
		this.destroyedByCreativePlayer = par1;
	}

	@Override
	public String getName() {
		return "guiwadansu";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return "Wada";
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.chestContents;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	public boolean isBlock (Block block) {
		return world.getBlockState(this.pos).getBlock() == block;
	}
}
