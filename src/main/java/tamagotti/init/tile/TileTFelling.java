package tamagotti.init.tile;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import tamagotti.forgeenergy.Action;
import tamagotti.handlers.PacketHandler;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.tile.furnace.WrappedItemHandler;
import tamagotti.init.tile.slot.StackHandler;
import tamagotti.packet.TileFEPKT;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TileTFelling extends TileTFEBase {

	// エネルギー消費したらtrue
	public boolean isActionHarvest = false;

	public int range = 8;
	public int pY = 0;
	public int upY = 0;
	public boolean upYFlag = false;
	public boolean isAction = false;

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

			// エネルギーを消費したなら
			if (this.isActionHarvest && this.isAction && this.tickTime % 4 == 0) {
				this.actionPlant();
				this.tickTime = 0;
				ItemHelper.compactInventory(this.plantInv);
			}

			else if (this.isActionHarvest && !this.isAction && this.tickTime % 300 == 0) {
				this.tickTime = 0;
				this.isAction = true;
			}

			else if (this.tickTime % 10 == 0) {
				ItemHelper.compactInventory(this.plantInv);
			}

			// クライアントに送信
            this.isActionHarvest = false;
			PacketHandler.sendToClient(new TileFEPKT(this.energy.getStored(), this.tickTime, this.pos));
		}

		// レッドストーン入力があるなら
		else {
			this.notActive();
		}
	}

	// 作物回収
	public void actionPlant () {

		// 向き取得
		IBlockState state = this.world.getBlockState(this.pos);
		EnumFacing facing = state.getValue(BaseFaceBlock.FACING);

		// 向きに合わせて座標を動かすための変数の用意
		int changeX = 0, changeZ = 0;
		switch (facing) {
		case NORTH:
			changeZ = this.range;
			break;
		case SOUTH:
			changeZ = -this.range;
			break;
		case EAST:
			changeX = -this.range;
			break;
		case WEST:
			changeX = this.range;
			break;
		case DOWN:
		case UP:
		default:
			break;

		}

		for (int x = -this.range - 4; x < this.range + 4; x++) {
			for (int z = -this.range - 4; z < this.range + 4; z++) {

				BlockPos pos = new BlockPos(this.pos.getX() + x + changeX, this.pos.getY() + this.pY, this.pos.getZ() + z + changeZ);
				Block b1 = this.world.getBlockState(pos).getBlock();
				if (b1 == Blocks.AIR) { continue; }

				// 作物かどうか
				if (this.checkPlant(b1, pos)) {
					this.inputPlant(b1, pos);

					// 一度もY座標を増やしていないなら
					if (!this.upYFlag) {
						this.upY++;
						this.upYFlag = true;
					}
				}
			}
		}

		this.upYFlag = false;

		++this.pY;

		if (this.pY > this.range + 4 + this.upY || this.pY > 256 || this.upY > 256) {
			this.pY = 0;
			this.upY = 0;
			this.upYFlag = false;
			this.isAction = false;
		}
	}

	// 作物かどうか
	public boolean checkPlant (Block block, BlockPos pos) {

		try {

			IBlockState state = this.world.getBlockState(pos);

			//空気ブロックとたいるえんちちーなら何もしない
			if(block == Blocks.AIR || block.hasTileEntity(state)){ return false; }

			boolean flag = false;

			int[] oreIds = OreDictionary.getOreIDs(new ItemStack(block));	// 鉱石辞書のidを入れる
			String oreName = "";	//　鉱石辞書の名前を初期化

			if (oreIds.length == 0) {
				if (block == Blocks.BROWN_MUSHROOM_BLOCK || block == Blocks.RED_MUSHROOM_BLOCK) {
					oreName = "logWood";
				} else { return false; }
			} else {
				for (int i = 0; i < oreIds.length; i++) {
					oreName = OreDictionary.getOreName(oreIds[i]);

					// 鉱石辞書の名前が原木か葉っぱなら伐採
					if (oreName == "logWood" || oreName == "treeLeaves") {
						return true;
					}
				}

			}

			// 鉱石辞書の名前が原木か葉っぱなら伐採
			if (oreName == "logWood" || oreName == "treeLeaves") {
				flag = true;
			}

			return flag;
		} catch (Throwable e) {
			return false;
		}
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
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
    }


	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.plantInv.serializeNBT());
		tags.setInteger("Y", this.pY);
		tags.setInteger("upY", this.upY);
		tags.setBoolean("upYFlag", this.upYFlag);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.plantInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.pY = tags.getInteger("Y");
		this.upY = tags.getInteger("upY");
		this.upYFlag = tags.getBoolean("upYFlag");
		this.isAction = tags.getBoolean("upYFlag");
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
