package tamagotti.init.blocks.chest.thopper;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.BlockInit;
import tamagotti.util.EnumSide;

public class THopper extends BlockContainer {

	protected static final AxisAlignedBB BASE = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
	protected static final AxisAlignedBB SOUTH = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
	protected static final AxisAlignedBB NORTH = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
	protected static final AxisAlignedBB WEST = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
	protected static final AxisAlignedBB EAST = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
	public static final PropertyEnum<EnumSide> SIDE = PropertyEnum.<EnumSide>create("side", EnumSide.class);			// side
	private final int data;

	public THopper(String name, int meta){
		super(Material.WOOD);
		setRegistryName(name);
		setUnlocalizedName(name);
		setSoundType(SoundType.METAL);
		setHardness(0.5F);
		setResistance(1024.0F);
        this.data = meta;
		BlockInit.blockList.add(this);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.THOPPER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getPlaceState(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> list, @Nullable Entity entity, boolean b) {
		this.getCollisionBoxList(state, world, pos, box, list, entity, b);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	public void getCollisionBoxList(IBlockState state, World world, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> list, @Nullable Entity entity, boolean b) {
		state = state.getActualState(world, pos);
		addCollisionBoxToList(pos, box, list, BASE);
		addCollisionBoxToList(pos, box, list, EAST);
		addCollisionBoxToList(pos, box, list, WEST);
		addCollisionBoxToList(pos, box, list, SOUTH);
		addCollisionBoxToList(pos, box, list, NORTH);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return this.getCollisionBox(blockState, worldIn, pos);
	}

	public AxisAlignedBB getCollisionBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return blockState.getBoundingBox(worldIn, pos);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public IBlockState getPlaceState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(SIDE, EnumSide.fromFacing(facing.getOpposite()));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileTHopper tile = (TileTHopper) world.getTileEntity(pos);
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			tile.loadFromNbt(tag);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileTHopper tile = (TileTHopper) world.getTileEntity(pos);
		ItemStack drop = new ItemStack(this, 1, this.damageDropped(state));
		drop.setTagCompound(tile.saveToNbt(new NBTTagCompound()));
		tile.flag = true;
		if (!world.isRemote) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 0.5D, pos.getZ(), drop));
		}
		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SIDE).index;
	}


	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(SIDE, EnumSide.fromIndex(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SIDE});
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
    	if(this.data == 0) {
    		return new TileTHopper();
    	} else if(this.data == 1){
    		return new TileTHopperN();
    	} else if(this.data == 2){
    		return new TileTHopperF();
    	} else if (this.data == 3) {
    		return new TileTHopperS();
    	} else {
    		return null;
    	}
	}

	public static EnumSide getSide(IBlockState state, PropertyEnum<EnumSide> prop) {
		if (state != null && hasProperty(state, prop)) {
			return state.getValue(prop);
		} else {
			return null;
		}
	}

	public static boolean hasProperty(IBlockState state, IProperty<EnumSide> prop) {
		return state.getProperties().containsKey(prop);
	}
}
