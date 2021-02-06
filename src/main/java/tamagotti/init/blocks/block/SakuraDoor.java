package tamagotti.init.blocks.block;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;

public class SakuraDoor extends BlockDoor {

	private final int data;

	public SakuraDoor(String name, int meta) {
        super(Material.WOOD);
        setRegistryName(name);
        setUnlocalizedName(name);
		setHardness(1F);
		setResistance(16F);
		setSoundType(SoundType.WOOD);
		this.data = meta;
		BlockInit.noTabList.add(this);
    }

    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : this.getItem();
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }

	private Item getItem() {
		switch (this.data) {
		case 0:
			return ItemInit.sakuradoori_0;
		case 1:
			return ItemInit.sakuradoori_1;
		case 2:
			return ItemInit.sakuradoori_2;
		}
		return null;
	}

    @Override
    @SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}