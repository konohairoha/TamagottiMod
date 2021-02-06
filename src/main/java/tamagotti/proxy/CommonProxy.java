package tamagotti.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tamagotti.TamagottiMod;
import tamagotti.config.TConfig;
import tamagotti.handlers.RegistyHandler;
import tamagotti.init.entity.monster.EntityAoi;
import tamagotti.init.entity.monster.EntityBurningSkullHead;
import tamagotti.init.entity.monster.EntityKumongous;
import tamagotti.init.entity.monster.EntityLCEvoker;
import tamagotti.init.entity.monster.EntityLCEvokerDep;
import tamagotti.init.entity.monster.EntityRebornZombie;
import tamagotti.init.entity.monster.EntitySkullFlame;
import tamagotti.init.entity.monster.EntityTChicken;
import tamagotti.init.entity.monster.EntityZombieMaster;
import tamagotti.init.entity.projectile.EntityAkari;
import tamagotti.init.entity.projectile.EntityBettyu;
import tamagotti.init.entity.projectile.EntityBlaster;
import tamagotti.init.entity.projectile.EntityExBall;
import tamagotti.init.entity.projectile.EntityKiritan;
import tamagotti.init.entity.projectile.EntityNova;
import tamagotti.init.entity.projectile.EntityRitter;
import tamagotti.init.entity.projectile.EntityRubyShot;
import tamagotti.init.entity.projectile.EntityScorp;
import tamagotti.init.entity.projectile.EntitySeika;
import tamagotti.init.entity.projectile.EntityShot;
import tamagotti.init.entity.projectile.EntityShotter;
import tamagotti.init.entity.projectile.EntitySittableBlock;
import tamagotti.init.entity.projectile.EntityTArrow;
import tamagotti.init.entity.projectile.EntityTClystal;
import tamagotti.init.entity.projectile.EntityTCross;
import tamagotti.init.entity.projectile.EntityTCushion_A;
import tamagotti.init.entity.projectile.EntityTCushion_Y;
import tamagotti.init.entity.projectile.EntityTEx;
import tamagotti.init.entity.projectile.EntityTHalberd;
import tamagotti.init.entity.projectile.EntityTKArrow;
import tamagotti.init.entity.projectile.EntityTamagotti;
import tamagotti.init.entity.projectile.EntityTorpedo;
import tamagotti.init.entity.projectile.EntityZunda;

public class CommonProxy {

	static int counter = 0;

    public void preInit(FMLPreInitializationEvent event) {
        RegistyHandler.Common(event);
    }

    public void init(FMLInitializationEvent event) {}
    public void postInit(FMLPostInitializationEvent event) {}
	public void registerEntityRender() {}
	public boolean isJumpPressed() { return false; }

	public void loadEntity() {

		eggEntity(EntityTChicken.class, "tchicken");
		eggEntity(EntityZombieMaster.class, "zombiemater");
		eggEntity(EntityRebornZombie.class, "rebornzombie");
		eggEntity(EntitySkullFlame.class, "skullflame");
		eggEntity(EntityLCEvoker.class, "lcevoker");
		eggEntity(EntityLCEvokerDep.class, "lcevokerdep");
		eggEntity(EntityKumongous.class, "kumongous");

		if (TConfig.mobspawn) {

			// スポーン
			for (Biome bio : ForgeRegistries.BIOMES) {

				// 特定のバイオームなら次へ
				if (this.checkBiome(bio)) { continue; }

				// たまごっちメモ：えんちちー、スポーン確率、最低人数、最大人数、スポーンするバイオーム
				EntityRegistry.addSpawn(EntityTChicken.class, 15, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityZombieMaster.class, 13, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityRebornZombie.class, 7, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityKumongous.class, 5, 0, 1, EnumCreatureType.MONSTER, bio);
			}
			EntityRegistry.addSpawn(EntitySkullFlame.class, 20, 0, 1, EnumCreatureType.MONSTER, Biomes.DESERT);

		}

		eggEntity(EntityBurningSkullHead.class, "burningSkullhead");

		arrowEntity(EntityTClystal.class, "tcrystal");
		arrowEntity(EntityTArrow.class, "tarrow");
		arrowEntity(EntityZunda.class, "zunda");
		arrowEntity(EntityRubyShot.class, "rubyshot");
		arrowEntity(EntityKiritan.class, "target");
		arrowEntity(EntityRitter.class, "ritter");
		arrowEntity(EntityScorp.class, "scorp");
		arrowEntity(EntityTorpedo.class, "torpedo");
		arrowEntity(EntityNova.class, "nova");
		arrowEntity(EntityBlaster.class, "balaster");
		arrowEntity(EntityAkari.class, "akari");
		arrowEntity(EntityShotter.class, "shotter");
		arrowEntity(EntityTamagotti.class, "tamagotti");
		arrowEntity(EntityBettyu.class, "battyu");
		arrowEntity(EntityTEx.class, "tex");
		arrowEntity(EntityTKArrow.class, "tkarrow");
		arrowEntity(EntityShot.class, "shot");
		arrowEntity(EntityTCross.class, "tcross");
		arrowEntity(EntityTHalberd.class, "thalberd");
		arrowEntity(EntitySittableBlock.class, "sittable");
		arrowEntity(EntityTCushion_A.class, "tcushion_a");
		arrowEntity(EntityTCushion_Y.class, "tcushion_y");
		arrowEntity(EntityAoi.class, "aoi");
		arrowEntity(EntityExBall.class, "exball");
		arrowEntity(EntitySeika.class, "seika");

	}

	public boolean checkBiome (Biome bio) {
		return bio == Biomes.MUSHROOM_ISLAND || bio == Biomes.MUSHROOM_ISLAND_SHORE ||
				bio == Biomes.HELL || bio == Biomes.VOID || bio instanceof BiomeEnd;
	}

	// スポーンエッグを用意させるメソッド
	public static void eggEntity(Class<? extends Entity> regClass, String name) {
		String regName = name;
		EntityRegistry.registerModEntity(new ResourceLocation(TamagottiMod.MODID, regName), regClass, regName, counter, TamagottiMod.INSTANCE, 64, 1, true, 300, 128);
		counter++;
	}

	// えんちちーの定義メソッド
	public static void arrowEntity(Class<? extends Entity> regClass, String name) {
		String regName = name;
		EntityRegistry.registerModEntity(new ResourceLocation(TamagottiMod.MODID, regName), regClass, regName, counter, TamagottiMod.INSTANCE, 128, 5, true);
		counter++;
	}
}
