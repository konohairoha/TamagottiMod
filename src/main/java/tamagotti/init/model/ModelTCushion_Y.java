package tamagotti.init.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelTCushion_Y extends ModelTCushion_A{

	public final boolean isBaked;

	public ModelTCushion_Y(boolean baked) {
		super(baked);
		isBaked = baked;
		textureWidth = 48;
		textureHeight = 16;

		cution = new ModelRenderer(this, 0, 0);
		cution.addBox(-8F, -1.125F, -8F, 12, 1, 12);
		cution.setRotationPoint(0F, 0F, 0F);
		cution.setTextureSize(48, 16);
	}
}
