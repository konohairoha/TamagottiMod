package tamagotti.init.blocks.furnace;

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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseFurnaceBlock;
import tamagotti.init.tile.furnace.TileDFurnace;

public class DFurnace extends BaseFurnaceBlock {

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 1, 0, 1, 0, 1);
	}

	public DFurnace(String name) {
		super(name);
		setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TFURNACE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
    }

    @Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileDFurnace();
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + "かまどの100倍の性能"));
		tooltip.add(I18n.format(TextFormatting.GOLD + "鉱石を３倍化"));
	}
}
