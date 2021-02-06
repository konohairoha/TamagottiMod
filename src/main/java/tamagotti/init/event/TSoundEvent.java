package tamagotti.init.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.TamagottiMod;

@Mod.EventBusSubscriber(modid = TamagottiMod.MODID)
public class TSoundEvent {

	public static final SoundEvent TYA = createEvent("tya");
	public static final SoundEvent SLIDE = createEvent("slide");
	public static final SoundEvent SHOT = createEvent("shot");
	public static final SoundEvent FIRE = createEvent("fire");
	public static final SoundEvent OPEN = createEvent("open");
	public static final SoundEvent CHANGE = createEvent("change");
	public static final SoundEvent KIDO = createEvent("kido");
	public static final SoundEvent IN = createEvent("in");
	public static final SoundEvent OUT = createEvent("out");
	public static final SoundEvent SENSOR = createEvent("sensor");
	public static final SoundEvent PAGE = createEvent("page");
	public static final SoundEvent SWING = createEvent("swing");
	public static final SoundEvent RELOAD = createEvent("reload");
	public static final SoundEvent AOI_DAMAGE = createEvent("aoi_damage");
	public static final SoundEvent AOI_DETH = createEvent("aoi_deth");
	public static final SoundEvent AOI_VOICE_0 = createEvent("aoi_voice_0");
	public static final SoundEvent AOI_VOICE_1 = createEvent("aoi_voice_1");
	public static final SoundEvent AOI_NO = createEvent("aoi_no");
	public static final SoundEvent DAGEKI = createEvent("dageki");
	public static final SoundEvent TBAG = createEvent("tbag");
	public static final SoundEvent MAGICON = createEvent("magicon");
	public static final SoundEvent MAGICOFF = createEvent("magicoff");
	public static final SoundEvent ARROW = createEvent("arrow");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = new ResourceLocation(TamagottiMod.MODID, sound);
		return new SoundEvent(name).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
		evt.getRegistry().register(TYA);
		evt.getRegistry().register(SLIDE);
		evt.getRegistry().register(SHOT);
		evt.getRegistry().register(FIRE);
		evt.getRegistry().register(OPEN);
		evt.getRegistry().register(CHANGE);
		evt.getRegistry().register(KIDO);
		evt.getRegistry().register(IN);
		evt.getRegistry().register(OUT);
		evt.getRegistry().register(SENSOR);
		evt.getRegistry().register(PAGE);
		evt.getRegistry().register(SWING);
		evt.getRegistry().register(RELOAD);
		evt.getRegistry().register(AOI_DAMAGE);
		evt.getRegistry().register(AOI_DETH);
		evt.getRegistry().register(AOI_VOICE_0);
		evt.getRegistry().register(AOI_VOICE_1);
		evt.getRegistry().register(AOI_NO);
		evt.getRegistry().register(DAGEKI);
		evt.getRegistry().register(TBAG);
		evt.getRegistry().register(MAGICON);
		evt.getRegistry().register(MAGICOFF);
		evt.getRegistry().register(ARROW);
	}

	public TSoundEvent() {}
}
