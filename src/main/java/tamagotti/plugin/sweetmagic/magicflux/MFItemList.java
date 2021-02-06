package tamagotti.plugin.sweetmagic.magicflux;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.magiaflux.IMagiaFluxItemListPlugin;
import sweetmagic.api.magiaflux.MagiaFluxInfo;
import sweetmagic.api.magiaflux.SMMagiaFluxItemListPlugin;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;

@SMMagiaFluxItemListPlugin(priority = EventPriority.LOW)
public class MFItemList implements IMagiaFluxItemListPlugin {

	@Override
	public void setMF(MagiaFluxInfo info) {
		info.setMF(new MagiaFluxInfo(new ItemStack(ItemInit.ruby), 1000));
		info.setMF(new MagiaFluxInfo(new ItemStack(BlockInit.rubyblock), 9000));
	}
}
