package tamagotti.util;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;

public final class PlayerHelper {

	public static final UUID HEALTH_MODIFIER_ID = UUID.fromString("60b1b9b5-dc5d-43a2-aa4e-655353070dbe");
	public static final String HEALTH_MODIFIER_NAME = "Tamagotti Health Modifier";

	public static void swingItem(EntityPlayer player, EnumHand hand) {
		if (player.getEntityWorld() instanceof WorldServer) {
			((WorldServer) player.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(player,
					new SPacketAnimation(player, hand == EnumHand.MAIN_HAND ? 0 : 3));
		}
	}

	public static void setMaxHealthModifier(EntityLivingBase living, double amount) {
		IAttributeInstance healthAttribute = living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		AttributeModifier modifier = healthAttribute.getModifier(HEALTH_MODIFIER_ID);
		if (modifier != null) {
			healthAttribute.removeModifier(modifier);
		}
		healthAttribute.applyModifier(new AttributeModifier(HEALTH_MODIFIER_ID, HEALTH_MODIFIER_NAME, amount, 0));
	}

	public static double getMaxHealthModifier(EntityLivingBase living) {
		IAttributeInstance healthAttribute = living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		AttributeModifier modifier = healthAttribute.getModifier(HEALTH_MODIFIER_ID);
		if (modifier != null) {
			return modifier.getAmount();
		}
		return 0;
	}

	public static void tryMakeEntityClimb(EntityLivingBase entity, double climbSpeed) {

		entity.fallDistance = 0;

		if (entity.isSneaking()) {
			entity.motionY = 0.0D;
		} else if (entity.moveForward > 0.0F && entity.motionY < climbSpeed) {
			entity.motionY = climbSpeed;
		}
	}
}