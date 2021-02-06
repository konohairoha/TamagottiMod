package tamagotti.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import sweetmagic.init.tile.gui.GuiSMWand;
import sweetmagic.init.tile.inventory.InventorySMWand;
import tamagotti.init.blocks.chest.dchest.ContainerDChest;
import tamagotti.init.blocks.chest.dchest.GuiDChest;
import tamagotti.init.blocks.chest.dchest.TileDChest;
import tamagotti.init.blocks.chest.gc.TileGCChest;
import tamagotti.init.blocks.chest.kago.ContainerKagoChest;
import tamagotti.init.blocks.chest.kago.GuiKagoChest;
import tamagotti.init.blocks.chest.kago.TileKagoChest;
import tamagotti.init.blocks.chest.sw.ContainerSWChest;
import tamagotti.init.blocks.chest.sw.GuiSWChest;
import tamagotti.init.blocks.chest.sw.TileSWChest;
import tamagotti.init.blocks.chest.tbshelf.TileTBSChest;
import tamagotti.init.blocks.chest.tchest.ContainerTChest;
import tamagotti.init.blocks.chest.tchest.GuiTChest;
import tamagotti.init.blocks.chest.tchest.TileTChest;
import tamagotti.init.blocks.chest.thopper.ContainerTHopper;
import tamagotti.init.blocks.chest.thopper.GuiTHopperChest;
import tamagotti.init.blocks.chest.thopper.TileTHopper;
import tamagotti.init.blocks.chest.tzon.TileTZonChest;
import tamagotti.init.blocks.chest.wadansu.ContainerWadansuChest;
import tamagotti.init.blocks.chest.wadansu.GuiWadansuChest;
import tamagotti.init.blocks.chest.wadansu.TileWadasu;
import tamagotti.init.blocks.chest.ychest.ContainerYChest;
import tamagotti.init.blocks.chest.ychest.GuiYChest;
import tamagotti.init.blocks.chest.ychest.TileYChest;
import tamagotti.init.tile.TileBaseFurnace;
import tamagotti.init.tile.TileTFelling;
import tamagotti.init.tile.TileTGeneratorT1;
import tamagotti.init.tile.TileTHarvester;
import tamagotti.init.tile.TileTPlanter;
import tamagotti.init.tile.container.BookContainer;
import tamagotti.init.tile.container.ContainarMFRifle;
import tamagotti.init.tile.container.ContainerDFurnace;
import tamagotti.init.tile.container.ContainerPot;
import tamagotti.init.tile.container.ContainerTBookNeo;
import tamagotti.init.tile.container.ContainerTDisplay;
import tamagotti.init.tile.container.ContainerTFelling;
import tamagotti.init.tile.container.ContainerTHarvester;
import tamagotti.init.tile.container.ContainerTPack;
import tamagotti.init.tile.container.ContainerTPlanter;
import tamagotti.init.tile.container.ContianerTGeneratorT1;
import tamagotti.init.tile.furnace.TileDFurnace;
import tamagotti.init.tile.furnace.TileTDisplay;
import tamagotti.init.tile.gui.GuiBook;
import tamagotti.init.tile.gui.GuiDFurnace;
import tamagotti.init.tile.gui.GuiPot;
import tamagotti.init.tile.gui.GuiTBookNeo;
import tamagotti.init.tile.gui.GuiTDisplay;
import tamagotti.init.tile.gui.GuiTFelling;
import tamagotti.init.tile.gui.GuiTGeneratorT1;
import tamagotti.init.tile.gui.GuiTHarvester;
import tamagotti.init.tile.gui.GuiTPack;
import tamagotti.init.tile.gui.GuiTPlanter;
import tamagotti.init.tile.inventory.InventoryTBookNeo;
import tamagotti.init.tile.inventory.InventoryTPack;

public class TGuiHandler implements IGuiHandler {

	/**
	 * GUIのID　TestChest用
	 * 備忘メモ：コアクラスのInitでNetworkRegistry.INSTANCE.registerGuiHandlerで登録を行うこと。
	 */

	public static final int TBOOK_GUI = 1;
	public static final int WADANSU_GUI = 2;
	public static final int KAGO_GUI = 3;
	public static final int TCHEST_GUI = 5;
	public static final int TZCHEST_GUI = 6;
	public static final int TPCHEST_GUI = 7;
	public static final int SWCHEST_GUI = 8;
	public static final int THOPPER_GUI = 9;
	public static final int YCHEST_GUI = 10;
	public static final int GCCHEST_GUI = 11;
	public static final int DCHEST_GUI = 12;
	public static final int TBOX_GUI = 13;
	public static final int TBSHELF_GUI = 14;
	public static final int POT_GUI = 16;
	public static final int POT_N_GUI = 17;
	public static final int POT_C_GUI = 18;
	public static final int TDISPLAY_GUI = 19;
	public static final int TFURNACE_GUI = 20;
	public static final int TPACK_GUI = 21;
	public static final int TBOOKNEO_GUI = 22;
	public static final int THARVESTER_GUI = 23;
	public static final int TPLANER_GUI = 24;
	public static final int TFELLING_GUI = 25;
	public static final int MFRIFLE_GUI = 26;
	public static final int TGENT1_GUI = 27;

	///サーバ側の処理
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		EnumHand hand = EnumHand.MAIN_HAND;

		switch (ID) {
		case TGuiHandler.WADANSU_GUI:
			return new ContainerWadansuChest(player, (TileWadasu) tile);
		case TGuiHandler.KAGO_GUI:
			return new ContainerKagoChest(player, (TileKagoChest) tile);
		case TGuiHandler.TCHEST_GUI:
			return new ContainerTChest(player, (TileTChest) tile);
		case TGuiHandler.SWCHEST_GUI:
			return new ContainerSWChest(player, (TileSWChest) tile);
		case TGuiHandler.TZCHEST_GUI:
			return new ContainerTChest(player, (TileTZonChest) tile);
		case TGuiHandler.THOPPER_GUI:
			return new ContainerTHopper((TileTHopper) tile, player);
		case TGuiHandler.TBOOK_GUI:
			return new BookContainer(player.inventory);
		case TGuiHandler.YCHEST_GUI:
			return new ContainerYChest(player, (TileYChest) tile);
		case TGuiHandler.DCHEST_GUI:
			return new ContainerDChest(player, (TileDChest) tile);
		case TGuiHandler.GCCHEST_GUI:
			return new ContainerWadansuChest(player, (TileGCChest) tile);
		case TGuiHandler.TBSHELF_GUI:
			return new ContainerSWChest(player, (TileTBSChest) tile);
		case TGuiHandler.POT_GUI:
			return new ContainerPot(player.inventory, (TileBaseFurnace) tile);
		case TGuiHandler.POT_N_GUI:
			return new ContainerPot(player.inventory, (TileBaseFurnace) tile);
		case TGuiHandler.POT_C_GUI:
			return new ContainerPot(player.inventory, (TileBaseFurnace) tile);
		case TGuiHandler.TDISPLAY_GUI:
			return new ContainerTDisplay(player.inventory, (TileTDisplay) tile);
		case TGuiHandler.TFURNACE_GUI:
			return new ContainerDFurnace(player.inventory, (TileDFurnace) tile);
		case TGuiHandler.TPACK_GUI:
			return new ContainerTPack(player.inventory, new InventoryTPack(player.getHeldItem(hand), player));
		case TGuiHandler.TBOOKNEO_GUI:
			return new ContainerTBookNeo(player.inventory, new InventoryTBookNeo(player.getHeldItem(hand), player), hand);
		case TGuiHandler.THARVESTER_GUI:
			return new ContainerTHarvester(player.inventory, (TileTHarvester) tile);
		case TGuiHandler.TPLANER_GUI:
			return new ContainerTPlanter(player.inventory, (TileTPlanter) tile);
		case TGuiHandler.TFELLING_GUI:
			return new ContainerTFelling(player.inventory, (TileTFelling) tile);
		case TGuiHandler.MFRIFLE_GUI:
			return new ContainarMFRifle(player.inventory, new InventorySMWand(player.getHeldItem(hand), player));
		case TGuiHandler.TGENT1_GUI:
			return new ContianerTGeneratorT1(player.inventory, (TileTGeneratorT1) tile);
		}
		return null;
	}

	//クライアント側の処理
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		EnumHand hand = EnumHand.MAIN_HAND;

		switch (ID) {
		case TGuiHandler.WADANSU_GUI:
			return new GuiWadansuChest(player, (TileWadasu) tile);
		case TGuiHandler.KAGO_GUI:
			return new GuiKagoChest(player, (TileKagoChest) tile);
		case TGuiHandler.TCHEST_GUI:
			return new GuiTChest(player, (TileTChest) tile);
		case TGuiHandler.SWCHEST_GUI:
			return new GuiSWChest(player, (TileSWChest) tile);
		case TGuiHandler.TZCHEST_GUI:
			return new GuiTChest(player, (TileTZonChest) tile);
		case TGuiHandler.THOPPER_GUI:
			return new GuiTHopperChest((TileTHopper) tile, player);
		case TGuiHandler.TBOOK_GUI:
			return new GuiBook(player.inventory);
		case TGuiHandler.YCHEST_GUI:
			return new GuiYChest(player, (TileYChest) tile);
		case TGuiHandler.DCHEST_GUI:
			return new GuiDChest(player, (TileDChest) tile);
		case TGuiHandler.GCCHEST_GUI:
			return new GuiWadansuChest(player, (TileGCChest) tile);
		case TGuiHandler.TBSHELF_GUI:
			return new GuiSWChest(player, (TileTBSChest) tile);
		case TGuiHandler.POT_GUI:
			return new GuiPot(player.inventory, (TileBaseFurnace) tile);
		case TGuiHandler.POT_N_GUI:
			return new GuiPot(player.inventory, (TileBaseFurnace) tile);
		case TGuiHandler.POT_C_GUI:
			return new GuiPot(player.inventory, (TileBaseFurnace) tile);
		case TGuiHandler.TDISPLAY_GUI:
			return new GuiTDisplay(player.inventory, (TileTDisplay) tile);
		case TGuiHandler.TFURNACE_GUI:
			return new GuiDFurnace(player.inventory, (TileDFurnace) tile);
		case TGuiHandler.TPACK_GUI:
			return new GuiTPack(player.inventory, new InventoryTPack(player.getHeldItem(hand), player));
		case TGuiHandler.TBOOKNEO_GUI:
			return new GuiTBookNeo(player.inventory, new InventoryTBookNeo(player.getHeldItem(hand), player), hand);
		case TGuiHandler.THARVESTER_GUI:
			return new GuiTHarvester(player.inventory, (TileTHarvester) tile);
		case TGuiHandler.TPLANER_GUI:
			return new GuiTPlanter(player.inventory, (TileTPlanter) tile);
		case TGuiHandler.TFELLING_GUI:
			return new GuiTFelling(player.inventory, (TileTFelling) tile);
		case TGuiHandler.MFRIFLE_GUI:
			return new GuiSMWand(player.inventory, new InventorySMWand(player.getHeldItem(hand), player));
		case TGuiHandler.TGENT1_GUI:
			return new GuiTGeneratorT1(player.inventory, (TileTGeneratorT1) tile);
		}
		return null;
	}
}
