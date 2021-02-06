package tamagotti.init.blocks.chest.tchest;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import tamagotti.init.tile.TileBaseChest;

public class TileTChest extends TileBaseChest {

	private static final int[] SLOTS = new int[104];
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(104, ItemStack.EMPTY);

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.loadFromNbt(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return this.saveToNbt(nbt);
	}

	@Override
	public void loadFromNbt(NBTTagCompound nbt) {
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt) && nbt.hasKey("Items", 9)){
            ItemStackHelper.loadAllItems(nbt, this.chestContents);
        }
    }

	@Override
	public NBTTagCompound saveToNbt(NBTTagCompound nbt) {
        if (!this.checkLootAndWrite(nbt)) {
            ItemStackHelper.saveAllItems(nbt, this.chestContents, false);
        }
        return nbt;
    }

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(pos, -50, nbt);
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
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	static {
        for (int i = 0; i < SLOTS.length; SLOTS[i] = i++){ ;}
    }

	@Override
	public int getSizeInventory() {
		return this.chestContents.size();
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
	public String getName() {
		return "guitchest";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerTChest(playerIn, this);
	}

	@Override
	public String getGuiID() {
		return "TChest";
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.chestContents;
    }

	@Override
	public void openInventory(EntityPlayer player) {
		if(!world.isRemote) {
			this.world.playSound(null, this.pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}
    }
}
