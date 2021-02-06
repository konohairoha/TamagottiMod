package tamagotti.init.items.tool.xtool;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TSword;
import tamagotti.init.items.tool.tamagotti.iitem.IMode;
import tamagotti.init.items.tool.tamagotti.iitem.IRelode;
import tamagotti.init.items.tool.tamagotti.iitem.ITool;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.TUtil;

public class XSword extends TSword implements ITool, IRelode, IMode {


	public XSword(String name, ToolMaterial material) {
		super(name, material, 0.0, 0);
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		if (Keyboard.isKeyDown(42)) {
			NBTTagCompound tags = stack.getTagCompound();
			tooltip.add(I18n.format(TextFormatting.RED + "スターローズクオーツを持って" + ClientKeyHelper.getKeyName(TKeybind.MODE) + "キーでレベルアップ"));
			tooltip.add(I18n.format(TextFormatting.RED + "レベル1以上で" + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーで耐久値回復"));
			if (tags == null) {
				tooltip.add(I18n.format(TextFormatting.GREEN + "レベル：0"));
				tooltip.add(I18n.format(TextFormatting.GREEN + "ダメージ倍率：1倍"));
			} else {
				float damage = 1 + (float) (tags.getInteger("level") * 0.375);
				tooltip.add(I18n.format(TextFormatting.GREEN + "レベル：" + tags.getInteger("level")));
				tooltip.add(I18n.format(TextFormatting.GREEN + "ダメージ倍率：" + damage + "倍"));
			}
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
	}

	// ダメージを受けるときの処理
	@Override
	public float reduceDamage(DamageSource src, ItemStack stack) {
		return 1.0F;
	}

    // ダメージ倍率
	@Override
	public float increaceDamage(EntityLivingBase target, ItemStack stack) {
		float add = 0;
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null) {
			add = (float) (tags.getInteger("level") * 0.375);
		}
		return 1.0F + add;
	}

	@Override
	public void changeMode(ItemStack stack, EntityPlayer player) {

		NBTTagCompound tags = TUtil.getXToolLevel(stack);

		//インベントリ内のアイテム消費
		Object[] obj = TUtil.getStackFromInventory(player.inventory.mainInventory, ItemInit.starrosequartz, 0, 1);

		if (obj != null && tags.getInteger("level") < 4) {
			player.inventory.decrStackSize((Integer) obj[0], 1);
			player.inventoryContainer.detectAndSendChanges();
			tags.setInteger("level", tags.getInteger("level") + 1);
		}
	}

	@Override
	public void doRelode(EntityPlayer player, ItemStack stack) {

		NBTTagCompound tags = TUtil.getXToolLevel(stack);

		// レベルが１以上なら
		if (tags.getInteger("level") > 0 && stack.getItemDamage() > 0) {
			tags.setInteger("level", tags.getInteger("level") - 1);
			stack.setItemDamage((int) (stack.getItemDamage() - (stack.getMaxDamage() * 0.25)));
		}
	}
}
