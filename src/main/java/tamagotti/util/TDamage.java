package tamagotti.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class TDamage extends DamageSource {

	public TDamage(String name){
		super("TamagottiDamage");
	}

	public static DamageSource ZundaDamage(Entity par1, Entity par2) {
        return (new EntityDamageSourceIndirect("zunda", par1, par2)).setProjectile();
    }

	public static DamageSource RitterDamage(Entity par1, Entity par2) {
        return (new EntityDamageSourceIndirect("ritter", par1, par2)).setProjectile();
    }

	public static DamageSource ShotterDamage(Entity par1, Entity par2) {
        return (new EntityDamageSourceIndirect("shotter", par1, par2)).setProjectile();
    }

	public static DamageSource ShelterDamage(Entity par1, Entity par2) {
        return (new EntityDamageSourceIndirect("shelter", par1, par2)).setProjectile();
    }

	public static DamageSource BlasterDamage(Entity par1, Entity par2) {
        return (new EntityDamageSourceIndirect("blaster", par1, par2)).setProjectile();
    }

	public static DamageSource AkariDamage(Entity par1, Entity par2) {
        return (new EntityDamageSourceIndirect("akari", par1, par2)).setProjectile();
    }
}