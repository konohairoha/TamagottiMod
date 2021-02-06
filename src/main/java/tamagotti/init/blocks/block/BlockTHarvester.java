package tamagotti.init.blocks.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iblock.IMFBlock;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.tile.TileTFEBase;
import tamagotti.init.tile.TileTFelling;
import tamagotti.init.tile.TileTHarvester;
import tamagotti.init.tile.TileTPlanter;

public class BlockTHarvester extends BaseFaceBlock {

	public final int data;

	public BlockTHarvester (String name, int data) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
		setHardness(0.5F);
		setResistance(1024F);
		this.data = data;
//		BlockInit.blockList.add(this);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		boolean actionFlag = this.actionBlock(world, state, pos, player, stack);
		if (!actionFlag && !world.isRemote) {
			this.actionBlock(world, pos, player, stack);
		}

		return true;
	}

	// ブロックでのアクション
	@Override@Optional.Method(modid = "sweetmagic")
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		// 何も持ってなかったら終了
		if (stack.isEmpty()) { return false; }

		// NBTを取得
		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null || !tags.hasKey("X") || tags.hasKey("energy")) { return false; }

		TileEntity tile = world.getTileEntity(pos);
		if (tile == null || !(tile instanceof IMFBlock)) { return false; }

		IMFBlock mfBlock = (IMFBlock) tile;
		boolean actionFlag = false;

		// 受け取り側かどうか
		if (mfBlock.getReceive()) {

			// NBTがnull以外なら
			BlockPos tilePos = new BlockPos(tags.getInteger("X"), tags.getInteger("Y"), tags.getInteger("Z"));
			mfBlock.addPosList(tilePos);

			if (!world.isRemote) {
				String tip = new TextComponentTranslation("tip.posregi.name", new Object[0]).getFormattedText();
				player.sendMessage(new TextComponentString(tip));
			} else {
				player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			}
			actionFlag = true;

			tags.removeTag("X");
			tags.removeTag("Y");
			tags.removeTag("Z");
		}

		// 送り側なら座標を登録
		else {
			actionFlag = this.setBlockPos(tags, stack, player, pos);
		}

		return actionFlag;
	}

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (this.data == 0) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.THARVESTER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		} else if (this.data == 1) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TPLANER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		} else if (this.data == 2) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TFELLING_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	// 座標を登録
	public boolean setBlockPos (NBTTagCompound tags, ItemStack stack, EntityPlayer player, BlockPos pos) {

		// NBTが保存したなかったら初期化
		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
		}

		tags.setInteger("X", pos.getX());
		tags.setInteger("Y", pos.getY());
		tags.setInteger("Z", pos.getZ());

		player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		String tip = new TextComponentTranslation("tip.pos.name", new Object[0]).getFormattedText();
		player.sendStatusMessage(new TextComponentTranslation(TextFormatting.GREEN + tip + " : " + " " +
				tags.getInteger("X") + ", " + tags.getInteger("Y") + ", " + tags.getInteger("Z")), true);

		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		switch (this.data) {
		case 0:
			return new TileTHarvester();
		case 1:
			return new TileTPlanter();
		case 2:
			return new TileTFelling();
		}
		return null;
	}

	// フェンスとかにつながないように
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.SOLID;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		TileTFEBase tile = (TileTFEBase) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
		NBTTagCompound tags = new NBTTagCompound();
		NBTTagCompound tileTags = tile.writeToNBT(new NBTTagCompound());
		if (tileTags.hasKey(tile.POST)) { tileTags.removeTag(tile.POST); }
		tags.setTag("BlockEntityTag", tileTags);
		stack.setTagCompound(tags);
		spawnAsEntity(world, pos, stack);
		world.updateComparatorOutputLevel(pos, state.getBlock());
        super.breakBlock(world, pos, state);
    }

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		TileTFEBase tile = (TileTFEBase) world.getTileEntity(pos);
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			tile.writeToNBT(tag);
		}
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		super.addInformation(stack, playerIn, tooltip, advanced);

  		if (this.data == 0) {
  	  		tooltip.add(I18n.format(TextFormatting.GREEN + "ブロックを中心に17×17で同じY座標か一つ上の座標に種植え"));
  		} else if (this.data == 1) {
  	  		tooltip.add(I18n.format(TextFormatting.GREEN + "ブロックの向きに合わせて17×17で同じY座標か一つ上の座標の作物を収穫"));
  		} else if (this.data == 2) {
  	  		tooltip.add(I18n.format(TextFormatting.GREEN + "ブロックの向きに合わせて9×9で木を伐採"));
  		}
    }
}
