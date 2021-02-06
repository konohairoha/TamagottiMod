package tamagotti.init.base;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.blocks.chest.ychest.TileYChest;

public class BaseHopperBlock extends BaseFaceContainer {

	public BaseHopperBlock(String name) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
		BlockInit.blockList.add(this);
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune){}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileYChest tile = (TileYChest) world.getTileEntity(pos);
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			tile.loadFromNbt(tag);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileYChest tile = (TileYChest) world.getTileEntity(pos);
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TYPE}) ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		//xx_xx.langファイルから文字を取得する方法
		TextComponentTranslation langtext = new TextComponentTranslation("tip.wadansu.name", new Object[0]);
		tooltip.add(I18n.format(TextFormatting.GOLD + langtext.getFormattedText()));
	}
}