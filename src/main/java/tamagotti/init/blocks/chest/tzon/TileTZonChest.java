package tamagotti.init.blocks.chest.tzon;

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
import net.minecraft.util.SoundCategory;
import tamagotti.init.blocks.chest.tchest.ContainerTChest;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.tile.TileBaseChest;

public class TileTZonChest extends TileBaseChest {

	private static final int[] SLOTS = new int[104];
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(104, ItemStack.EMPTY);

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
	public String getName() {
		return "guitama";
	}

	@Override
	public Container createContainer(InventoryPlayer pInv, EntityPlayer player) {
		return new ContainerTChest(player, this);
	}

	@Override
	public String getGuiID() {
		return "TZon";
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.chestContents;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (!world.isRemote) {
			this.world.playSound(null, this.pos, TSoundEvent.OPEN, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.5F);
		}
	}
}
