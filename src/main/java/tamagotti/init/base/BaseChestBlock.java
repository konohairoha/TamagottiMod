package tamagotti.init.base;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.tile.TileBaseChest;

public class BaseChestBlock extends BaseFaceContainer {

	public BaseChestBlock(String name) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
		BlockInit.blockList.add(this);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!world.isRemote) {
			this.actionBlock(world, pos, player);
		}
		return true;
	}

	// ブロックの処理
	public void actionBlock (World world, BlockPos pos, EntityPlayer player) {
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune){}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileBaseChest chest = (TileBaseChest) world.getTileEntity(pos);
		chest.setDestroyedByCreativePlayer(false);
		chest.fillWithLoot(player);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileBaseChest chest = (TileBaseChest) world.getTileEntity(pos);
		if (!chest.isCleared() && chest.shouldDrop()) {
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("BlockEntityTag", chest.saveToNbt(new NBTTagCompound()));
			stack.setTagCompound(tag);
			spawnAsEntity(world, pos, stack);
		}
		world.updateComparatorOutputLevel(pos, state.getBlock());
		super.breakBlock(world, pos, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		//xx_xx.langファイルから文字を取得する方法
		TextComponentTranslation langtext = new TextComponentTranslation("tip.wadansu.name", new Object[0]);
		tooltip.add(I18n.format(TextFormatting.GOLD + langtext.getFormattedText()));
	}
}
