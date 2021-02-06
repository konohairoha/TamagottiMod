package tamagotti.client.particle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ParticleSakura extends Particle {

	public static final String TEX = new String("tamagottimod:textures/particle/sakuraparticle.png");
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F)
			.addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB)
			.addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B)
			.addElement(DefaultVertexFormats.PADDING_1B);
	private TextureManager textureManager;

	public ParticleSakura(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn;
		this.motionZ = zSpeedIn;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleAlpha = 1F;
		this.particleMaxAge = 70;
		this.particleScale = 0.15F;
		this.textureManager = Minecraft.getMinecraft().getTextureManager();
	}

	public void setColor(float red, float green, float blue) {
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
	}

	@Override
	public void move(double x, double y, double z) {
		if (!this.onGround) {
			this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
			this.resetPositionToBB();
		}
	}

	@Override
	public void renderParticle(BufferBuilder buf, Entity entity, float tick, float rotX,
			float rotZ, float rotYZ, float rotXY, float rotXZ) {
		int i = (int) ((this.particleAge + tick) * 11.0F / this.particleMaxAge);

		if (i <= 16) {
			this.textureManager.bindTexture(new ResourceLocation(TEX));
			float fu = 0.0F;
			float fU = 1.0F;
			float fv = i * 0.03125F;
			float fV = fv + 0.03125F;
			float scale = this.particleScale;
			float fx = (float) (this.prevPosX + (this.posX - this.prevPosX) * tick - interpPosX);
			float fy = (float) (this.prevPosY + (this.posY - this.prevPosY) * tick - interpPosY);
			float fz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * tick - interpPosZ);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			buf.begin(7, VERTEX_FORMAT);
			buf.pos(fx - rotX * scale - rotXY * scale, fy - rotZ * scale, fz - rotYZ * scale - rotXZ * scale)
					.tex(fU, fV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buf.pos(fx - rotX * scale + rotXY * scale, fy + rotZ * scale, fz - rotYZ * scale + rotXZ * scale)
					.tex(fU, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buf.pos(fx + rotX * scale + rotXY * scale, fy + rotZ * scale, fz + rotYZ * scale + rotXZ * scale)
					.tex(fu, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buf.pos(fx + rotX * scale - rotXY * scale, fy - rotZ * scale, fz + rotYZ * scale - rotXZ * scale)
					.tex(fu, fV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
		}
	}

	@Override
	public void onUpdate() {	//ここでパーティクルの動きを記述する
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge >= this.particleMaxAge) {
			this.setExpired();
		}
		this.motionY -= 0.0014D;
		this.move(this.motionX, this.motionY, this.motionZ);
		if (this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		this.particleAge++;
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		@Override
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
				double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleSakura(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}

	@Override
	public int getFXLayer() {
		return 3;
	}
}
