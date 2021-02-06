package tamagotti.util;

import java.util.UUID;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tamagotti.init.entity.ai.EntityAIDonMov;

public class EventUtil {

	public static final UUID UUID_donmov  = UUID.fromString("6fd1ce57-8e37-504d-f859-6262b644ef19");
	public static final AttributeModifier modifierDonmove = (new AttributeModifier(UUID_donmov, "donmov", -1.0d, 2)).setSaved(false);

    // ガード中か
	public static boolean isGuard(EntityLivingBase living) {
        ItemStack active = living.getActiveItemStack();
		if (!EventUtil.isEmptyStack(active) && living.isHandActive()) {

            //使用中である。
            Item stack = active.getItem();
            if (!EventUtil.isBashing(living) && stack.getItemUseAction(active) == EnumAction.BLOCK) {
                return true;
            }
        }
        return false;
    }

    //バッシュ中か否か
	public static boolean isBashing(EntityLivingBase living) {
        if (living != null && living.isActiveItemStackBlocking() && living.isSwingInProgress){
            return true;
        }
        return false;
    }

	public static boolean isEmptyStack(ItemStack stackIn) {
		return stackIn == null || stackIn == ItemStack.EMPTY;
	}

    // 敵AIを動かさない
    public static void tameAIDonmov(EntityLiving target, int power) {

        boolean isLearning = false;
        int tick = power * 20;
        for (EntityAITasks.EntityAITaskEntry entry : target.tasks.taskEntries) {
            if (entry.action instanceof EntityAIDonMov) {
                EntityAIDonMov ai = (EntityAIDonMov)entry.action;
                ai.tick = tick;
                isLearning = true;
                break;
            }
        }
        if (!isLearning) {
            EntityAIDonMov ai = new EntityAIDonMov(target);
            ai.tick = tick;
            target.tasks.addTask(0, ai);
        }
    }
}