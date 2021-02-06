package tamagotti.init.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import tamagotti.util.EventUtil;

public class EntityAIDonMov extends EntityAIBase {

    public int tick = 20;
    public final EntityLiving myself;
    public final IAttributeInstance attri;

    public EntityAIDonMov(EntityLiving living) {
        this.myself = living;
        attri = myself.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        setMutexBits(~0x0);
    }

    @Override
    public boolean shouldExecute() {
        return this.tick > 0;
    }

    // ■中断可能か否か
    @Override
    public boolean isInterruptible() {
        return false;
    }

    @Override
    public void startExecuting() {
        if (this.attri.hasModifier(EventUtil.modifierDonmove)) {
        	this.attri.removeModifier(EventUtil.modifierDonmove);
        }
        this.attri.applyModifier(EventUtil.modifierDonmove);
    }

    @Override
    public void resetTask() {
    	this.tick = 0;
    	this.attri.removeModifier(EventUtil.modifierDonmove);
    }

    // Updates the task
    @Override
    public void updateTask() {
        --this.tick;
    }
}