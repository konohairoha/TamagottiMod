package tamagotti.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import tamagotti.TamagottiMod;

public class GUIUtil {

	public static void drawModalRectWithCustomSizedTexture(int x, int y, float z, float u, float v, int width,
			int height, float textureWidth, float textureHeight) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, z)
				.tex(u * f, (v + height) * f1).endVertex();
		buffer.pos(x + width, y + height, z)
				.tex((u + width) * f, (v + height) * f1).endVertex();
		buffer.pos(x + width, y, z)
				.tex((u + width) * f, v * f1).endVertex();
		buffer.pos(x, y, z).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	public static void registerPotion(String name, Potion potion, RegistryEvent.Register<Potion> event){
		event.getRegistry().register(potion.setRegistryName(TamagottiMod.MODID, name));
	}
}
