package tamagotti.init.tile;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import tamagotti.forgeenergy.Action;
import tamagotti.handlers.PacketHandler;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.tile.slot.SlotPredicates;
import tamagotti.init.tile.slot.StackHandler;
import tamagotti.packet.TileFEPKT;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TileTPlanter extends TileTFEBase {

	// エネルギー消費したらtrue
	public boolean isActionHarvest = false;
	public boolean isAction = false;

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
			if (this.tickTime % 10 == 0) {
				ItemHelper.compactInventory(this.plantInv);
			}

			// エネルギーを消費したなら
			if (this.isActionHarvest && this.isAction && this.tickTime % 2 == 0) {
				if (!this.getOutputItem(0).isEmpty()) {
					this.actionPlant();
				}
				this.tickTime = 0;
			}

			else if (this.isActionHarvest && !this.isAction && this.tickTime % 300 == 0) {
				this.tickTime = 0;
				this.isAction = true;
			}

			// クライアントに送信
			PacketHandler.sendToClient(new TileFEPKT(this.energy.getStored(), this.tickTime, this.pos));
		}

		// レッドストーン入力があるなら
		else {
			this.notActive();
		}
	}

	// 植え付け
	public void actionPlant () {

		// 向き取得
		IBlockState state = this.world.getBlockState(this.pos);
		EnumFacing facing = state.getValue(BaseFaceBlock.FACING);

		// 向きに合わせて座標を動かすための変数の用意
		int changeX = 0, changeZ = 0;
		switch (facing) {
		case NORTH:
			changeZ = 9;
			break;
		case SOUTH:
			changeZ = -9;
			break;
		case EAST:
			changeX = -9;
			break;
		case WEST:
			changeX = 9;
			break;
		case DOWN:
		case UP:
		default:
			break;

		}

		BlockPos pos = new BlockPos(this.pos.getX() + this.pX + changeX, this.pos.getY(), this.pos.getZ() + this.pZ + changeZ);

		if (this.checkDirt(pos)) {
		} else if (this.checkDirt(pos.down())) {}

		++this.pX;

		if (this.pX > this.range) {
			this.pX = -this.range;
			++this.pZ;
		}

		if (this.pZ > this.range) {
			this.pX = -this.range;
			this.pZ = -this.range;
			this.isAction = false;
		}

	}

	public boolean checkDirt (BlockPos pos) {

        if (this.world.isAirBlock(pos)) { return false; }
        if (!this.world.isAirBlock(pos.up())) { return false; }
		IBlockState state = this.world.getBlockState(pos);
        IPlantable plant;

		for (int i = 0; i < this.plantInv.getSlots(); i++) {

			ItemStack stack = this.getOutputItem(i);
			if (stack.isEmpty()) { continue; }

			Item item = stack.getItem();
			if (item instanceof IPlantable) {

				plant = (IPlantable) item;
				if (((BlockBush) plant.getPlant(this.world, pos).getBlock()).canBlockStay(this.world, pos.up(), state)) {
					this.world.setBlockState(pos.up(), plant.getPlant(this.world, pos.up()), 2);
					stack.shrink(1);
					ItemHelper.compactInventory(this.plantInv);
					world.playSound(null, pos.up(), SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.PLAYERS, 1.5F, 1F / (world.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
					return true;
				}

			// 苗木なら
			} else if (item instanceof ItemBlock && (((ItemBlock) item).getBlock() instanceof BlockSapling ||
					SlotPredicates.isOreSapring(((ItemBlock) item).getBlock()))) {

				BlockBush sap = (BlockBush) ((ItemBlock) item).getBlock();
				if (sap.canBlockStay(this.world, pos.up(), state)) {
					this.world.setBlockState(pos.up(), sap.getDefaultState(), 2);
					stack.shrink(1);
					ItemHelper.compactInventory(this.plantInv);
					this.world.playSound(null, pos.up(), SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.PLAYERS, 1.5F, 1F / (this.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.plantInv.serializeNBT());
		tags.setInteger("X", this.pX);
		tags.setInteger("Z", this.pZ);
		tags.setBoolean("isAction", this.isAction);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.plantInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.pX = tags.getInteger("X");
		this.pZ = tags.getInteger("Z");
		this.isAction = tags.getBoolean("isAction");
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

	public IItemHandler side = new CombinedInvWrapper(this.plantInv);
	public CombinedInvWrapper top = new CombinedInvWrapper(this.plantInv);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.top);
			} else {
				switch (side) {
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}
}
