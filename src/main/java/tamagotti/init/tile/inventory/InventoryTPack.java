package tamagotti.init.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import tamagotti.handlers.PacketHandler;
import tamagotti.packet.UpdateGemModePKT;
import tamagotti.util.ItemHelper;

public class InventoryTPack implements IItemHandlerModifiable {

	private final IItemHandlerModifiable inventory = new ItemStackHandler(4);
	public final ItemStack invItem;

	public InventoryTPack(ItemStack stack, EntityPlayer player) {
		this.invItem = stack;
		this.readFromNBT(ItemHelper.getNBT(stack));
	}

	@Override
	public int getSlots() {
		return this.inventory.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		ItemStack ret = this.inventory.insertItem(slot, stack, simulate);
		this.writeBack();
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack ret = this.inventory.extractItem(slot, amount, simulate);
		this.writeBack();
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.inventory.setStackInSlot(slot, stack);
		this.writeBack();
	}

	private void writeBack() {
		for (int i = 0; i < this.inventory.getSlots(); ++i) {
			if (this.inventory.getStackInSlot(i).isEmpty()) {
				this.inventory.setStackInSlot(i, ItemStack.EMPTY);
			}
		}

		this.writeToNBT(this.invItem.getTagCompound());
	}

	public void readFromNBT(NBTTagCompound nbt) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inventory, null,
				nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND));
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("Items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.inventory, null));
	}

	// テレポート用のnbt保存
	public void teleportPlayer(EntityPlayer player, int id) {
		NBTTagCompound tag = this.getStackInSlot(id).getTagCompound();
		if (tag != null && tag.getInteger("y") > 0) {
			ItemStack stack = player.getHeldItemMainhand();
			NBTTagCompound nbt = stack.getTagCompound();

			if (nbt == null) {
				stack.setTagCompound(new NBTTagCompound());
				nbt = stack.getTagCompound();
			}
			nbt.setInteger("x", tag.getInteger("x"));
			nbt.setInteger("y", tag.getInteger("y"));
			nbt.setInteger("z", tag.getInteger("z"));
			nbt.setInteger("dim", tag.getInteger("dim"));

			player.sendStatusMessage(new TextComponentTranslation(TextFormatting.GREEN + "登録した座標：" + " " +
					nbt.getInteger("x") + ", " + nbt.getInteger("y") + ", " + nbt.getInteger("z")), true);

			// サーバーへ送りつける
			PacketHandler.sendToServer(new UpdateGemModePKT(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"), 0));
		}
	}
}
