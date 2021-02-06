package tamagotti.init.blocks.crops;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;

public class BlockTRoom extends BlockBush {

	public BlockTRoom(String name) {
		super(Material.PLANTS);
		setUnlocalizedName(name);
		setRegistryName(name);
        setHardness(4.0F);
		setResistance(999F);
		this.setTickRandomly(true);
		setSoundType(SoundType.PLANT);
		BlockInit.noTabList.add(this);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		boolean result = super.canBlockStay(world, pos, state);
		if (result == true) {
			IBlockState soil = world.getBlockState(pos.down());
			result = soil.getBlock() == Blocks.GRASS;
		}
        return result;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		player.world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
	}

    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		double d1 = pos.getX() + rand.nextFloat();
		double d2 = pos.getY() + rand.nextFloat();
		double d3 = pos.getZ() + rand.nextFloat();
		world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, d1, d2, d3, 0.0D, 0.0D, 0.0D);
	}
}
