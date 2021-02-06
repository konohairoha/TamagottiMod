package tamagotti.init.items.armor;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class TArmorMaterial {

	public static ArmorMaterial akane = EnumHelper
			.addArmorMaterial("akane", "tamagottimod:akane", 40, new int[] { 4, 7, 9, 4 }, 35, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3);

	public static ArmorMaterial yukari = EnumHelper
			.addArmorMaterial("yukari", "tamagottimod:yukari", 40, new int[] { 4, 7, 9, 4 }, 35, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3);

	public static ArmorMaterial copper = EnumHelper
			.addArmorMaterial("copper", "tamagottimod:empty", 15, new int[] { 1, 3, 4, 1 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);

	public static ArmorMaterial fl = EnumHelper
			.addArmorMaterial("fl", "tamagottimod:empty", 23, new int[] { 2, 5, 6, 2 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);

	public static ArmorMaterial redberyl = EnumHelper
			.addArmorMaterial("redberyl", "tamagottimod:empty", 33, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);

	public static ArmorMaterial bettyu = EnumHelper
			.addArmorMaterial("bettyu", "tamagottimod:empty", 33, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);

	public static ArmorMaterial tamagotti= EnumHelper
			.addArmorMaterial("tamagotti", "tamagottimod:empty", 15, new int[] { 2, 5, 6, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);

	public static ArmorMaterial yukaaka= EnumHelper
			.addArmorMaterial("yukaaka", "tamagottimod:empty", 50, new int[] { 6, 8, 10, 4 }, 32, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 4);

	public static ArmorMaterial machine = EnumHelper
			.addArmorMaterial("machine", "tamagottimod:machine", 35, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);

	/**
	 *  .addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness)
	 * name "Material名"
	 * textureName テクスチャの名前 "Modid:pngファイル名の_layer前まで"
	 * durability 防具の耐久度 革5 鉄15 金7 ダイヤモンド33
	 * reductionAmounts 各部の防御ポイント 革{ 1, 2, 3, 1 }、鉄{ 2, 5, 6, 2 }、金{ 1, 3, 5, 2 }、ダイヤモンド{ 3, 6, 8, 3 }
	 * enchantability 数値が大きいほど良いエンチャントが付き易い 革15、鉄9、金25、ダイヤモンド10
	 * soundOnEquip 効果音
	 * toughness タフネス、ダメージカット率をより上げる デフォルト0 ダイヤモンドのみ2
	 */
}
