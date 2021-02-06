package tamagotti.proxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
import tamagotti.init.event.RenderSailEvent;
import tamagotti.init.render.RenderAkari;
import tamagotti.init.render.RenderAoi;
import tamagotti.init.render.RenderBettyu;
import tamagotti.init.render.RenderBlaster;
import tamagotti.init.render.RenderBurningSkullHead;
import tamagotti.init.render.RenderExBall;
import tamagotti.init.render.RenderKiritan;
import tamagotti.init.render.RenderKumongous;
import tamagotti.init.render.RenderLCEvoker;
import tamagotti.init.render.RenderLCEvokerDep;
import tamagotti.init.render.RenderNova;
import tamagotti.init.render.RenderRebornZombie;
import tamagotti.init.render.RenderRitter;
import tamagotti.init.render.RenderRubyShot;
import tamagotti.init.render.RenderScorp;
import tamagotti.init.render.RenderSeika;
import tamagotti.init.render.RenderShot;
import tamagotti.init.render.RenderShotter;
import tamagotti.init.render.RenderSkullFlame;
import tamagotti.init.render.RenderTArrow;
import tamagotti.init.render.RenderTChicken;
import tamagotti.init.render.RenderTCross;
import tamagotti.init.render.RenderTCrystal;
import tamagotti.init.render.RenderTCushion_A;
import tamagotti.init.render.RenderTCushion_Y;
import tamagotti.init.render.RenderTEx;
import tamagotti.init.render.RenderTHalberd;
import tamagotti.init.render.RenderTKArrow;
import tamagotti.init.render.RenderTamagotti;
import tamagotti.init.render.RenderTorpedo;
import tamagotti.init.render.RenderZombieMaster;
import tamagotti.init.render.RenderZunda;
import tamagotti.init.tile.TileWindMill;
import tamagotti.init.tile.TileWindMillL;
import tamagotti.init.tile.render.RenderTileWindMill;
import tamagotti.init.tile.render.RenderTileWindMillL;
import tamagotti.key.ClientKeyHelper;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

        // ブロックのレンダー
		ClientRegistry.bindTileEntitySpecialRenderer(TileWindMill.class, new RenderTileWindMill());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWindMillL.class, new RenderTileWindMillL());
		MinecraftForge.EVENT_BUS.register(new RenderSailEvent());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void loadEntity() {
		super.loadEntity();
		ClientKeyHelper.registerMCBindings();

		registRender(EntityTChicken.class, RenderTChicken.class);
		registRender(EntityZombieMaster.class, RenderZombieMaster.class);
		registRender(EntityRebornZombie.class, RenderRebornZombie.class);
		registRender(EntityTArrow.class, RenderTArrow.class);
		registRender(EntityKiritan.class, RenderKiritan.class);
		registRender(EntityRitter.class, RenderRitter.class);
		registRender(EntityNova.class, RenderNova.class);
		registRender(EntityBlaster.class, RenderBlaster.class);
		registRender(EntityTorpedo.class, RenderTorpedo.class);
		registRender(EntityTamagotti.class, RenderTamagotti.class);
		registRender(EntityBettyu.class, RenderBettyu.class);
		registRender(EntityTEx.class, RenderTEx.class);
		registRender(EntityAkari.class, RenderAkari.class);
		registRender(EntityScorp.class, RenderScorp.class);
		registRender(EntityShotter.class, RenderShotter.class);
		registRender(EntityZunda.class, RenderZunda.class);
		registRender(EntityRubyShot.class, RenderRubyShot.class);
		registRender(EntityTKArrow.class, RenderTKArrow.class);
		registRender(EntityShot.class, RenderShot.class);
		registRender(EntityTCross.class, RenderTCross.class);
		registRender(EntityTHalberd.class, RenderTHalberd.class);
		registRender(EntityTCushion_A.class, RenderTCushion_A.class);
		registRender(EntityTCushion_Y.class, RenderTCushion_Y.class);
		registRender(EntityAoi.class, RenderAoi.class);
		registRender(EntitySkullFlame.class, RenderSkullFlame.class);
		registRender(EntityLCEvokerDep.class, RenderLCEvokerDep.class);
		registRender(EntityLCEvoker.class, RenderLCEvoker.class);
		registRender(EntityTClystal.class, RenderTCrystal.class);
		registRender(EntityExBall.class, RenderExBall.class);
		registRender(EntitySeika.class, RenderSeika.class);
		registRender(EntityBurningSkullHead.class, RenderBurningSkullHead.class);
		registRender(EntityKumongous.class, RenderKumongous.class);
	}

	public static void registRender(Class<? extends Entity> cls, final Class<? extends Render> render) {
		RenderingRegistry.registerEntityRenderingHandler(cls, new IRenderFactory() {
			@Override
			public Render createRenderFor(RenderManager manager) {
				try {
					return render.getConstructor(manager.getClass()).newInstance(manager);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	@Override
	public boolean isJumpPressed() {
		return FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isKeyDown();
	}
}
