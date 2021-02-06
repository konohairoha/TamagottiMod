package tamagotti.init;

import net.minecraft.potion.Potion;
import tamagotti.init.potion.PotionTamagotti;

public class PotionInit {

	public static Potion tamagotti;
	public static Potion akane;
	public static Potion yukari;
	public static Potion sticky;
	public static Potion concentration;

    public static void init() {
    	tamagotti = new PotionTamagotti(false, 0x56CBFD, "tamagotti", "textures/items/tamagotti.png");
    	akane = new PotionTamagotti(false, 0x56CBFD, "akane", "textures/potion/akane.png");
    	yukari = new PotionTamagotti(false, 0x56CBFD, "yukari", "textures/potion/yukari.png");
    	sticky = new PotionTamagotti(false, 0x56CBFD, "sticky", "textures/potion/web.png");
    	concentration = new PotionTamagotti(false, 0x56CBFD, "concentration", "textures/potion/concentration.png");
    }
}
