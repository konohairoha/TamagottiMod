package tamagotti.init.blocks.chest.ychest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import tamagotti.init.blocks.chest.thopper.TileTHopper;
import tamagotti.util.TInventory;
import tamagotti.util.TUtil;

public class TileYChest extends TileTHopper {

	protected TInventory inv = new TInventory(104);
	public int x,y,z;
	public boolean isOpen = false;
	public int numPlayersUsing = 0;
	public int particleTime = 0;
	public boolean flag = false;

	@Override
	public int getSizeInventory() {
		return 104;
	}

	@Override
	public void update() {

		if (TUtil.isActive(this.world, this.pos)) {

			this.updateTile();
			this.onTickUpdate();

			if (!this.world.isRemote && !this.flag) {
				this.suctionDrop();
			}

		} else {

			this.particleTime++;

			if (this.particleTime >= 100) {
				this.particleTime = 0;
				TUtil.spawnParticles(this.world, this.pos);
			}
		}
	}

	@Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new ContainerYChest(player, this);
	}

	@Override
	public void incrStackInSlot(int i, ItemStack input) {
		inv.incrStackInSlot(i, input);
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inv.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int num) {
		return inv.decrStackSize(i, num);
	}

	@Override
	public ItemStack removeStackFromSlot(int i) {
		return inv.removeStackFromSlot(i);
	}

	// インベントリ内のスロットにアイテムを入れる
	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		inv.setInventorySlotContents(i, stack);
		this.markDirty();
	}

	//----------ここからインベントリを開いたときに音が鳴る処理----------
	@Override
	public void openInventory(EntityPlayer player) {

		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}
			++this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
			this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			--this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
			this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
		}
	}

	@Override
	public void onTickUpdate() {

		if (this.numPlayersUsing > 0 && !this.isOpen) {
			this.isOpen = true;
			this.world.playSound(null, this.pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.isOpen) {
			this.isOpen = false;
			this.world.playSound(null, this.pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	@Override
	public void updateTile() {

		if (!this.world.isRemote && this.numPlayersUsing != 0) {
			this.numPlayersUsing = 0;

			for (EntityPlayer player : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-5, -5, -5), pos.add(5, 5, 5)))) {

				if (!(player.openContainer instanceof ContainerYChest)) { continue; }

				IInventory iinventory = ((ContainerYChest) player.openContainer).tile;
				if (iinventory == this) {
					++this.numPlayersUsing;
				}
			}
		}
	}

	@Override
	protected int[] slotsSides() {
		return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
				41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
				82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103};
	}

	@Override
	public void loadFromNbt(NBTTagCompound compound) {
		this.inv.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound saveToNbt(NBTTagCompound compound) {
		this.inv.writeToNBT(compound);
		return compound;
	}
}
