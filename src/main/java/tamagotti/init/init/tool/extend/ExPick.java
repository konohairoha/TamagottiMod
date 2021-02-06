package tamagotti.init.items.tool.extend;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseRangeBreak;
import tamagotti.util.TUtil;

public class ExPick extends BaseRangeBreak {

	private final int cycle;

	public ExPick(String name, ToolMaterial material, int size) {
		super(name, material, size);
        this.cycle = size;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 0, 0));
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(),
					new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 4, 0));
		}
		return multimap;
	}
}