package tamagotti.init.base;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.tile.TileBaseFurnace;

public class BaseFurnaceBlock extends BaseFaceContainer {

	public static boolean keepInventory;

	public BaseFurnaceBlock(String name) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.1D, 0.9D, 0.1D, 0.9D, 0.0D, 0.9D);
	}

    @Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileBaseFurnace tile = (TileBaseFurnace) world.getTileEntity(pos);
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			NBTTagCompound tags = new NBTTagCompound();
			tags.setTag("BlockEntityTag", (tile).writeToNBT(new NBTTagCompound()));
			stack.setTagCompound(tags);
			spawnAsEntity(world, pos, stack);
			world.updateComparatorOutputLevel(pos, state.getBlock());
		}
		super.breakBlock(world, pos, state);
	}

    @Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

	@Override
	@Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}