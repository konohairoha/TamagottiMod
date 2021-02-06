package tamagotti.handlers;


import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import tamagotti.config.TConfig;
import tamagotti.gen.BambooGen;
import tamagotti.gen.SabakuDunGen;
import tamagotti.gen.SakuraGen;
import tamagotti.gen.THouseGen;
import tamagotti.gen.TWorldGen;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;
import tamagotti.init.PotionInit;
import tamagotti.init.blocks.chest.dchest.TileDChest;
import tamagotti.init.blocks.chest.gc.TileGCChest;
import tamagotti.init.blocks.chest.kago.TileKagoChest;
import tamagotti.init.blocks.chest.sw.TileSWChest;
import tamagotti.init.blocks.chest.tbshelf.TileTBSChest;
import tamagotti.init.blocks.chest.tchest.TileTChest;
import tamagotti.init.blocks.chest.thopper.TileTHopper;
import tamagotti.init.blocks.chest.thopper.TileTHopperF;
import tamagotti.init.blocks.chest.thopper.TileTHopperN;
import tamagotti.init.blocks.chest.thopper.TileTHopperS;
import tamagotti.init.blocks.chest.tzon.TileTZonChest;
import tamagotti.init.blocks.chest.wadansu.TileWadasu;
import tamagotti.init.blocks.chest.ychest.TileYChest;
import tamagotti.init.tile.TileBaseFurnace;
import tamagotti.init.tile.TileFryPan;
import tamagotti.init.tile.TileJumper;
import tamagotti.init.tile.TileLCESpaner;
import tamagotti.init.tile.TileLink;
import tamagotti.init.tile.TileOverclock;
import tamagotti.init.tile.TileTBeacon;
import tamagotti.init.tile.TileTFelling;
import tamagotti.init.tile.TileTGeneratorT1;
import tamagotti.init.tile.TileTHarvester;
import tamagotti.init.tile.TileTPlanter;
import tamagotti.init.tile.TileTSpawner;
import tamagotti.init.tile.TileWindMill;
import tamagotti.init.tile.TileWindMillL;
import tamagotti.init.tile.furnace.TileDFurnace;
import tamagotti.init.tile.furnace.TileTDisplay;

public class RegistyHandler {

	public static VillagerRegistry.VillagerProfession PROF_ENGINEER;

	public static void Common(FMLPreInitializationEvent event) {

		//レジスター関連をブロック先読み(作物のため)
        BlockInit.init();
        ItemInit.init();
        BlockInit.register();
        ItemInit.register();
        PotionInit.init();

		if (TConfig.housegen) {
			GameRegistry.registerWorldGenerator(new THouseGen(), 7);
			THouseGen.initLoot();
			GameRegistry.registerWorldGenerator(new SabakuDunGen(), 4);
			SabakuDunGen.initLoot();
		}

		if (TConfig.toregen) {
			GameRegistry.registerWorldGenerator(new TWorldGen(), 0);
		}

        GameRegistry.registerWorldGenerator(new BambooGen(), 1);
        GameRegistry.registerWorldGenerator(new SakuraGen(), 6);
    }

	// Tileの登録
	public static void TileRegister(String MODID) {
		GameRegistry.registerTileEntity(TileBaseFurnace.class, new ResourceLocation(MODID, "QF1"));
		GameRegistry.registerTileEntity(TileFryPan.class, new ResourceLocation(MODID, "QF3"));
		GameRegistry.registerTileEntity(TileTDisplay.class, new ResourceLocation(MODID, "QF5"));
		GameRegistry.registerTileEntity(TileTHopper.class, new ResourceLocation(MODID, "QF6"));
		GameRegistry.registerTileEntity(TileTHopperN.class, new ResourceLocation(MODID, "QF7"));
		GameRegistry.registerTileEntity(TileYChest.class, new ResourceLocation(MODID, "QF8"));
		GameRegistry.registerTileEntity(TileDChest.class, new ResourceLocation(MODID, "QF9"));
		GameRegistry.registerTileEntity(TileTHopperF.class, new ResourceLocation(MODID, "QF10"));
		GameRegistry.registerTileEntity(TileTHopperS.class, new ResourceLocation(MODID, "QF11"));
		GameRegistry.registerTileEntity(TileWadasu.class, new ResourceLocation(MODID, "Wada"));
		GameRegistry.registerTileEntity(TileKagoChest.class, new ResourceLocation(MODID, "Kago"));
		GameRegistry.registerTileEntity(TileGCChest.class, new ResourceLocation(MODID, "GC"));
		GameRegistry.registerTileEntity(TileTChest.class, new ResourceLocation(MODID, "Tama"));
		GameRegistry.registerTileEntity(TileTZonChest.class, new ResourceLocation(MODID, "Ama"));
		GameRegistry.registerTileEntity(TileSWChest.class, new ResourceLocation(MODID, "SW"));
		GameRegistry.registerTileEntity(TileTBSChest.class, new ResourceLocation(MODID, "TBShelf"));
		GameRegistry.registerTileEntity(TileTBeacon.class, new ResourceLocation(MODID, "TBeacon"));
		GameRegistry.registerTileEntity(TileJumper.class, new ResourceLocation(MODID, "Jmper"));
		GameRegistry.registerTileEntity(TileOverclock.class, new ResourceLocation(MODID, "Overclock"));
		GameRegistry.registerTileEntity(TileTSpawner.class, new ResourceLocation(MODID, "TSpawner"));
		GameRegistry.registerTileEntity(TileLCESpaner.class, new ResourceLocation(MODID, "LCESpawner"));
		GameRegistry.registerTileEntity(TileDFurnace.class, new ResourceLocation(MODID, "DFurnace"));
		GameRegistry.registerTileEntity(TileWindMill.class, new ResourceLocation(MODID, "WindMill"));
		GameRegistry.registerTileEntity(TileWindMillL.class, new ResourceLocation(MODID, "WindMill_L"));
		GameRegistry.registerTileEntity(TileTHarvester.class, new ResourceLocation(MODID, "THarvester"));
		GameRegistry.registerTileEntity(TileTPlanter.class, new ResourceLocation(MODID, "TPlanter"));
		GameRegistry.registerTileEntity(TileTFelling.class, new ResourceLocation(MODID, "TFelling"));
		GameRegistry.registerTileEntity(TileLink.class, new ResourceLocation(MODID, "TTLink"));
		GameRegistry.registerTileEntity(TileTGeneratorT1.class, new ResourceLocation(MODID, "TGENT1"));
	}

	// 草から種の追加
	public static void addSeed () {
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.riceseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.sobaseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.soyseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.azukiseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.cabbageseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.lettuceseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.tomato), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.j_radishseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.e_plantseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.onion), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.negiseed), 5);
    	MinecraftForge.addGrassSeed(new ItemStack(ItemInit.canolaseed), 5);
	}

	public static void villagerTrade () {

//		PROF_ENGINEER = new VillagerRegistry.VillagerProfession(TamagottiMod.MODID, "tamagottimod:textures/entity/aoi.png", "tamagottimod:textures/entity/aoi.png");
//
//		VillagerRegistry.VillagerCareer career_engineer = new VillagerRegistry.VillagerCareer(VillagerRegistry.FARMER, TamagottiMod.MODID);
//
//		career_engineer.addTrade(1,
//				new EmeraldForItemstack(new ItemStack(ItemInit.tamagottineo, 1, 0), new EntityVillager.PriceInfo(8, 16)),
//				new ItemstackForEmerald(new ItemStack(ItemInit.tamagotticustom, 1, 0), new EntityVillager.PriceInfo(-3, -1))
//		);
//
//
//		career_engineer.addTrade(2,
//				new EmeraldForItemstack(new ItemStack(ItemInit.tamagottineo, 1, 0), new EntityVillager.PriceInfo(8, 16)),
//				new ItemstackForEmerald(new ItemStack(ItemInit.tamagotticollaboration, 1, 0), new EntityVillager.PriceInfo(-3, -1))
//		);
	}

//	private static class EmeraldForItemstack implements EntityVillager.ITradeList {
//		public ItemStack stack;
//		public EntityVillager.PriceInfo buyAmounts;
//
//		public EmeraldForItemstack(@Nonnull ItemStack item, @Nonnull EntityVillager.PriceInfo buyAmounts) {
//			this.stack = item;
//			this.buyAmounts = buyAmounts;
//		}
//
//		@Override
//		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
//			recipeList.add(new MerchantRecipe(RegistyHandler.copyStackWithAmount(this.stack, this.buyAmounts.getPrice(random)), Items.EMERALD));
//		}
//
//	}
//
//	/*
//	 * ==========================================
//	 * 			村人トレードのためのメソッド
//	 * ==========================================
//	 */
//
//	private static class ItemstackForEmerald implements EntityVillager.ITradeList {
//		public ItemStack sellingItem;
//		public EntityVillager.PriceInfo priceInfo;
//
//		public ItemstackForEmerald(Item item, EntityVillager.PriceInfo priceInfo) {
//			this.sellingItem = new ItemStack(item);
//			this.priceInfo = priceInfo;
//		}
//
//		public ItemstackForEmerald(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
//			this.sellingItem = stack;
//			this.priceInfo = priceInfo;
//		}
//
//		@Override
//		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random rand) {
//			int i = 1;
//			if (this.priceInfo != null) {
//				i = this.priceInfo.getPrice(rand);
//			}
//
//			ItemStack stack;
//			ItemStack stack1;
//			if (i < 0) {
//				stack = new ItemStack(Items.EMERALD);
//				stack1 = RegistyHandler.copyStackWithAmount(this.sellingItem, -i);
//			} else {
//				stack = new ItemStack(Items.EMERALD, i, 0);
//				stack1 = RegistyHandler.copyStackWithAmount(this.sellingItem, 1);
//			}
//			recipeList.add(new MerchantRecipe(stack, stack1));
//		}
//	}
//
//	public static ItemStack copyStackWithAmount(ItemStack stack, int amount) {
//		if (stack.isEmpty())
//			return ItemStack.EMPTY;
//		ItemStack s2 = stack.copy();
//		s2.setCount(amount);
//		return s2;
//	}
}
