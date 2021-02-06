package tamagotti.init.blocks.chest.gc;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseChestBlock;

public class GC extends BaseChestBlock {

	public GC(String name) {
		super(name);
		setSoundType(SoundType.STONE);
	}

	// ブロックの処理
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player) {
		player.openGui(TamagottiMod.INSTANCE, TGuiHandler.GCCHEST_GUI, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileGCChest();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		//xx_xx.langファイルから文字を取得する方法
		String tip1 = new TextComponentTranslation("tip.gc.name", new Object[0]).getFormattedText();
		String tip2 = new TextComponentTranslation("tip.gc2.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + tip1));
		tooltip.add(I18n.format(TextFormatting.YELLOW + tip2));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.15, 0.75, 0.15, 0.85, 0.0, 0.85);
	}
}
