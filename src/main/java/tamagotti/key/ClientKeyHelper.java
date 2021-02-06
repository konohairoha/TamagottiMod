package tamagotti.key;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.ImmutableBiMap;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;

@SideOnly(Side.CLIENT)
public class ClientKeyHelper {

	public static ImmutableBiMap<KeyBinding, TKeybind> mcToPe;
	private static ImmutableBiMap<TKeybind, KeyBinding> peToMc;

	public static void registerMCBindings() {
		ImmutableBiMap.Builder<KeyBinding, TKeybind> builder = ImmutableBiMap.builder();
		for (TKeybind k : TKeybind.values()) {
			KeyBinding mcK = new KeyBinding(k.keyName, k.defaultKeyCode, TamagottiMod.MODID);
			builder.put(mcK, k);
			ClientRegistry.registerKeyBinding(mcK);
		}
		mcToPe = builder.build();
        peToMc = mcToPe.inverse();
	}

	public static String getKeyName(TKeybind k) {
		return getName(peToMc.get(k));
	}

	public static String getName(KeyBinding k) {
		int keyCode = k.getKeyCode();
		if (keyCode > Keyboard.getKeyCount() || keyCode < 0) {
			return k.getDisplayName();
		}
		return Keyboard.getKeyName(keyCode);
	}
}
