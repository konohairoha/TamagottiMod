package tamagotti.init.blocks.furnace;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseFaceContainer;
import tamagotti.init.tile.TileFryPan;

public class FryPan extends BaseFaceContainer {

	public final boolean isBurning;
	public static boolean keepInventory;

	public FryPan(boolean burnin, String name, List<Block> list) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
		this.isBurning = burnin;
		list.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.2D, 0.2D, 0.1D, 0.8D, 0.0D, 0.8D);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFryPan) {
                player.displayGUIChest((TileFryPan)tile);
                player.addStat(StatList.FURNACE_INTERACTION);
            }
        }
        return true;
    }

    public static void setState(boolean active, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;
		if (active) {
            world.setBlockState(pos, BlockInit.frypan_1.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		} else {
            world.setBlockState(pos, BlockInit.frypan.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
        }
        keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFryPan();
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		if (stack.hasDisplayName()) {
        	TileFryPan tileentity = (TileFryPan) worldIn.getTileEntity(pos);
			if (tileentity instanceof TileFryPan) {
                tileentity.setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + "かまどの1.3倍の性能"));
	}

    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFryPan) {
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
				NBTTagCompound tags = new NBTTagCompound();
				NBTTagCompound tags1 = new NBTTagCompound();
				tags.setTag("BlockEntityTag", ((TileFryPan) tile).writeToNBT(tags1));
				stack.setTagCompound(tags);
				spawnAsEntity(world, pos, stack);
				world.updateComparatorOutputLevel(pos, state.getBlock());
			}
		}
        super.breakBlock(world, pos, state);
    }

    @Override
	public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
