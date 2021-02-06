package tamagotti.init.blocks.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.client.particle.ParticleSakura;
import tamagotti.init.BlockInit;

public class SakuraLeave extends BlockLeaves {

	public SakuraLeave(String name) {
		setRegistryName(name);
		setUnlocalizedName(name);
		setHardness(0F);
		setResistance(1F);
		setLightLevel(0.4F);
		this.setTickRandomly(true);
		this.setDefaultState(blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
		BlockInit.blockList.add(this);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) > 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		if (state.getValue(DECAYABLE)) { i |= 4; }
		if (state.getValue(CHECK_DECAY)) { i |= 8; }
		return i;
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, 0));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		return Item.getItemFromBlock(BlockInit.sakurasapling);
	}

	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {}

	@Override
	public ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1);
	}

	@Override
	public int getSaplingDropChance(IBlockState state) {
		return 20;
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this, 1));
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 60;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 30;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public boolean isRendered(EnumFacing face, IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	//テクスチャが透明で、重ねて表示したい場合
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess ace, BlockPos pos, EnumFacing side){
		AxisAlignedBB aabb = state.getBoundingBox(ace, pos);
        switch (side) {
            case DOWN:
                if (aabb.minY > 0.0D) return true;
                break;
            case UP:
                if (aabb.maxY < 1.0D) return true;
                break;
            case NORTH:
                if (aabb.minZ > 0.0D) return true;
                break;
            case SOUTH:
                if (aabb.maxZ < 1.0D) return true;
                break;
            case WEST:
                if (aabb.minX > 0.0D) return true;
                break;
            case EAST:
                if (aabb.maxX < 1.0D) return true;
        }
        return !ace.getBlockState(pos.offset(side)).doesSideBlockRendering(ace, pos.offset(side), side.getOpposite());
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		double d0 = pos.getX() - 0.03D - rand.nextDouble() * 0.1D;
		double d1 = pos.getY() - 0.07D /*- 1D - rand.nextDouble() * 0.11D*/;
		double d2 = pos.getZ() - 0.03D - rand.nextDouble() * 0.1D;
		if (world.getBlockState(pos.down()).getMaterial() == Material.AIR && !world.getBlockState(pos.down()).isOpaqueCube()) {
			if (rand.nextInt(35) == 0) {
				Particle newEffect = new ParticleSakura.Factory().createParticle(0, world, d0, d1, d2, 0, 0D, 0);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(newEffect);
			}
		}
	}
}
