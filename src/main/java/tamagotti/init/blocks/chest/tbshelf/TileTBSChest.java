package tamagotti.init.blocks.chest.tbshelf;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import tamagotti.init.blocks.chest.sw.ContainerSWChest;
import tamagotti.init.tile.TileBaseChest;

public class TileTBSChest extends TileBaseChest {

	private static final int[] SLOTS = new int[150];
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(150, ItemStack.EMPTY);

	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		this.loadFromNbt(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		return this.saveToNbt(compound);
	}

	@Override
	public void loadFromNbt(NBTTagCompound compound) {
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound) && compound.hasKey("Items", 9)){
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }
    }

    @Override
	public NBTTagCompound saveToNbt(NBTTagCompound compound){
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
	public String getName(){
		return "guiswchest";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn){
        return new ContainerSWChest(playerIn, this);
    }

	@Override
	public String getGuiID(){
		return "SWChest";
	}

	@Override
	protected NonNullList<ItemStack> getItems(){
        return this.chestContents;
    }
}
