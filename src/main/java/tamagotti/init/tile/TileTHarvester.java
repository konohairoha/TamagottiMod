package tamagotti.init.tile;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import tamagotti.forgeenergy.Action;
import tamagotti.handlers.PacketHandler;
import tamagotti.init.BlockInit;
import tamagotti.init.tile.furnace.WrappedItemHandler;
import tamagotti.init.tile.slot.StackHandler;
import tamagotti.packet.TileFEPKT;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TileTHarvester extends TileTFEBase {

	// エネルギー消費したらtrue
	public boolean isActionHarvest = false;

	public int range = 9;
	public int pX = -this.range;
	public int pZ = -this.range;

	// 作物スロット
	private final ItemStackHandler plantInv = new StackHandler(this, this.getInvSize());

	@Override
	public void update () {

		if (this.world.isRemote) { return; }

		// レッドストーン入力がないなら稼働
		if (TUtil.isActive(this.world, this.pos)) {

			super.update();

			// エネルギーがあるなら
			if (!this.isEmptyEnergy()) {

				// エネルギーが1以上なら消費
		        if (this.energy.extract(this.getEnergyUse(), Action.SIMULATE) >= 0) {
		            this.energy.extract(this.getEnergyUse(), Action.PERFORM);
		            this.isActionHarvest = true;
		        }

		        // エネルギーが0未満なら0に書き換え
		        else {
		            this.energy.setStored(0);
		        }
			}

			// 自動整理
			if (this.tickTime % 4 == 0) {
				ItemHelper.compactInventory(this.plantInv);
			}

			// エネルギーを消費したなら
			if (this.isActionHarvest && this.tickTime % 2 == 0) {
				this.actionPlant();
				this.isActionHarvest = false;
				this.tickTime = 0;
			}

			// クライアントに送信
			PacketHandler.sendToClient(new TileFEPKT(this.energy.getStored(), this.tickTime, this.pos));
		}

		// レッドストーン入力があるなら
		else {
			this.notActive();
		}
	}

	// 作物回収
	public void actionPlant () {

		BlockPos pos = new BlockPos(this.pos.getX() + this.pX, this.pos.getY(), this.pos.getZ() + this.pZ);
		Block b1 = this.world.getBlockState(pos).getBlock();
		Block b2 = this.world.getBlockState(pos.down()).getBlock();

		// ブロックより一つ下が作物かどうか
		if (this.checkPlant(b2, pos.down())) {
			this.inputPlant(b2, pos.down());
		}

		// 作物かどうか
		else if (this.checkPlant(b1, pos)) {
			this.inputPlant(b1, pos);
		}

		++this.pX;

		if (this.pX > this.range) {
			this.pX = -this.range;
			++this.pZ;
		}

		if (this.pZ > this.range) {
			this.pX = -this.range;
			this.pZ = -this.range;
		}
	}

	// 作物かどうか
	public boolean checkPlant (Block block, BlockPos pos) {

		IBlockState state = this.world.getBlockState(pos);
		boolean flag = true;

		if (block instanceof IGrowable) {
			flag = ((IGrowable) block).canGrow(this.world, pos, state, false);
		}

		if (block instanceof IShearable) {
			flag = !((IShearable) block).isShearable(new ItemStack(Items.SHEARS), this.world, pos);
		}

		if (block == Blocks.PUMPKIN || block == Blocks.MELON_BLOCK) {
			flag = false;
		}

		if (block == Blocks.REEDS || block == BlockInit.bamboo) {
			Block under = this.world.getBlockState(pos.down()).getBlock();
			if (under == Blocks.REEDS || under == BlockInit.bamboo) {
				flag = false;
			}
		}

		return !flag;
	}

	// アイテムを入れる
	public void inputPlant (Block block, BlockPos pos) {

		if (this.checkInv()) { return; }

		List<ItemStack> stackList = block.getDrops(this.world, pos, this.world.getBlockState(pos), 0);
		for (ItemStack stack : stackList) {
			ItemHandlerHelper.insertItemStacked(this.getOutput(), stack, false);
		}
		this.breakBlock(this.world, pos);
	}

	// 最終スロットが空じゃなかったらfalseを返す
	public boolean checkInv () {
		ItemHelper.compactInventory(this.plantInv);
		return !this.getOutputItem(this.getInvSize() - 1).isEmpty();
	}

	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
		world.playEvent(2001, pos, Block.getStateId(state));
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }


	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.plantInv.serializeNBT());
		tags.setInteger("X", this.pX);
		tags.setInteger("Z", this.pZ);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.plantInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.pX = tags.getInteger("X");
		this.pZ = tags.getInteger("Z");
	}


	// インベントリの取得
	public int getInvSize () {
		return 36;
	}

	// 作物スロットの取得
	public IItemHandler getOutput() {
		return this.plantInv;
	}

	// 作物スロットのアイテムを取得
	public  ItemStack getOutputItem(int slot) {
		return this.getOutput().getStackInSlot(slot);
	}

	private final IItemHandlerModifiable autoOutput = new WrappedItemHandler(this.plantInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler autoSide= new CombinedInvWrapper(autoOutput);
	private final CombinedInvWrapper joined = new CombinedInvWrapper(autoOutput);


	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(joined);
			} else {
				switch (side) {
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(autoOutput);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(autoSide);
				}
			}
		}

		return super.getCapability(cap, side);
	}
}
