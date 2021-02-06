package tamagotti;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import tamagotti.config.TConfig;
import tamagotti.handlers.OreDictionaryHandler;
import tamagotti.handlers.PacketHandler;
import tamagotti.handlers.RecipeHandler;
import tamagotti.handlers.RegistyHandler;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.event.EntityItemPickEvent;
import tamagotti.init.event.GuardSplashEvent;
import tamagotti.init.event.LivingHurt;
import tamagotti.init.event.PDethEvent;
import tamagotti.init.event.PlayerUpdateEvent;
import tamagotti.init.event.TDeathEvent;
import tamagotti.init.event.THarvestEvent;
import tamagotti.init.event.TLootTableEvent;
import tamagotti.init.event.UpdateTickPlayerEvent;
import tamagotti.init.event.ZoomEvent;
import tamagotti.proxy.CommonProxy;

/*
 * modを読み込む用のコアクラス
 * 登録はせずに呼び出しだけを行う
 */
@Mod(modid = "tamagottimod", name = "TamagottiMod", version = "6.0.3")
public class TamagottiMod {

	//GUIを考えてMODのインスタンスを作っておく
	@Instance("tamagottimod")
	public static TamagottiMod INSTANCE;
	public static CreativeTabs tamagottiTab = new TamagottiTab("tamagottiTab"),tamagottiFoodTab = new TamagottiFoodTab("tamagottiFoodTab");
	@SidedProxy(clientSide = "tamagotti.proxy.ClientProxy", serverSide = "tamagotti.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Logger logger;
	public static final String MODID = "tamagottimod";

	@Mod.EventHandler	//Init1番目
	public void preInit(FMLPreInitializationEvent event) {

		//すぐconfigの読み込みをする
		TConfig.INSTANCE.load(event.getModConfigurationDirectory());

		logger = event.getModLog();
		proxy.registerEntityRender();
		proxy.loadEntity();
		proxy.preInit(event);

		// tileの登録
		RegistyHandler.TileRegister(MODID);

		// パケット
		PacketHandler.register();
	}

	@EventHandler	//Init2番目
	public static void init(FMLInitializationEvent event) {

		proxy.init(event);
		MinecraftForge.EVENT_BUS.register(event);
		MinecraftForge.EVENT_BUS.register(new ZoomEvent());
		MinecraftForge.EVENT_BUS.register(new GuardSplashEvent());
		MinecraftForge.EVENT_BUS.register(new TDeathEvent());
		MinecraftForge.EVENT_BUS.register(new THarvestEvent());
		MinecraftForge.EVENT_BUS.register(new TLootTableEvent());
		MinecraftForge.EVENT_BUS.register(new PDethEvent());
		MinecraftForge.EVENT_BUS.register(new LivingHurt());
		MinecraftForge.EVENT_BUS.register(new UpdateTickPlayerEvent());
		MinecraftForge.EVENT_BUS.register(new PlayerUpdateEvent());
		MinecraftForge.EVENT_BUS.register(new EntityItemPickEvent());

		// 追加レシピ読み込み
		RecipeHandler.registerCrafting();

    	// 種の追加
    	RegistyHandler.addSeed();

		// 追加精錬読み込み
		RecipeHandler.registerSmelting();

		// 鉱石辞書読み込み
		OreDictionaryHandler.registerOreDictionary();

		//GUIの登録
		NetworkRegistry.INSTANCE.registerGuiHandler(TamagottiMod.INSTANCE, new TGuiHandler());
	}

	@EventHandler	//Init3番目
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

    public static ResourceLocation prefix(String name) {
		return new ResourceLocation(MODID, name);
	}
}
