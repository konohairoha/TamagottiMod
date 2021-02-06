package tamagotti.init.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.util.GUIUtil;

public class PotionBase extends Potion {

	public final ResourceLocation sprite;

	public PotionBase (boolean effect, int color, String name, String dir) {
		super(effect, color);
		ForgeRegistries.POTIONS.register(this.setRegistryName(TamagottiMod.MODID, name));
		this.setPotionName(TamagottiMod.MODID + ".effect." + name);
		sprite = TamagottiMod.prefix(dir);
//		this.setIconIndex(1, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(sprite);
		GUIUtil.drawModalRectWithCustomSizedTexture(x + 3, y + 3, z, 0, 0, 18, 18, 18, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		Minecraft.getMinecraft().renderEngine.bindTexture(sprite);
		GUIUtil.drawModalRectWithCustomSizedTexture(x + 6, y + 7, z, 0, 0, 18, 18, 18, 18);
	}
}
