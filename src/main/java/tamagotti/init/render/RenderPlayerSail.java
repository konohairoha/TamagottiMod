package tamagotti.init.render;

import java.util.Iterator;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import tamagotti.init.model.ModelPlayerSail;

public class RenderPlayerSail extends RenderPlayer {

	public RenderPlayerSail(RenderManager render, boolean useSmallArms) {
		super(render, useSmallArms);
		this.mainModel = new ModelPlayerSail(0.0F, useSmallArms);

		Iterator<LayerRenderer<AbstractClientPlayer>> it = this.layerRenderers.iterator();
		while(it.hasNext()) {
			LayerRenderer<AbstractClientPlayer> layer = it.next();
			if(layer.getClass() == LayerBipedArmor.class) {
				it.remove();
			}
		}
	}
}