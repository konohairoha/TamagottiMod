package tamagotti.init.tile.furnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.ItemHandlerHelper;
import tamagotti.init.tile.TileBaseFurnace;
import tamagotti.util.ItemHelper;

public class TileDFurnace extends TileBaseFurnace {

	public TileDFurnace() {
		this.ticksBeforeSmelt = 1;
		this.maxBurnTime = 100000;
	}

	// インベントリの数
	@Override
	protected int getInvSize() {
		return 31;
	}

	@Override
	public void updateState () {}

	@Override
	protected void smeltItem() {
		ItemStack toSmelt = inputInv.getStackInSlot(0);
		ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(toSmelt).copy();
		if (ItemHelper.getOreDictionaryName(toSmelt).startsWith("ore")) {
			smeltResult.grow(smeltResult.getCount() * 2);
		}
		ItemHandlerHelper.insertItemStacked(this.outputInv, smeltResult, false);
		toSmelt.shrink(1);
	}

	public void openInventory(EntityPlayer player) {
		if(!world.isRemote) {
			this.world.playSound(null, this.pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}
    }

	@Override
	public void spwanPatickle() {}
}
