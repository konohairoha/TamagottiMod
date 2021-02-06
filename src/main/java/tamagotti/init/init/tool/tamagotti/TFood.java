package tamagotti.init.items.tool.tamagotti;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.config.TConfig;
import tamagotti.init.ItemInit;
import tamagotti.init.PotionInit;

public class TFood extends ItemFood {

	Random rand = new Random();
	private final int data;

    public TFood(int amount, float saturation ,String name, int meta) {
        super(amount, saturation, false);
        setUnlocalizedName(name);
        setRegistryName(name);
        setAlwaysEdible();
        this.data = meta;
        if (this.data == 21 || this.data == 22) {
    		ItemInit.itemList.add(this);
        } else {
    		ItemInit.foodList.add(this);
        }
    }

    /**
     *  0 = 効果無し
     *  1 = 小豆
     *  2 = 豆乳
     *  3 = 納豆
     *  4 = おにぎり
     *  5 = 餅
     *  6 = チーズ
     *  7 = 麺
     *  8 = 米
     *  9 = ずんだ
     *  10 = パン
     *  11 = サラダ
     *  12 = 魚
     *  13 = ケーキ
     *  14 = オムライス
     *  15 = フルーツ
     *  16 = きりたん
     *  17 = きなこ
     *  18 = たまごっちライス
     *  19 = ずんたまごっち
     *  20 = たまごっちサンド
     *  21 = たまごっち
     *  22 = たまごっちネオ
     *  23 = レッドブル
     *  24 = 揚げ物
     *  25 = ハンバーグ
     *  26 = プリン
     *  27 = ホットココア
     *  28 = 炊き込みご飯
     *  29 = 謎肉
     *  30 = ふぐ刺し
     *  31 = トマト煮
     *  32 = 豚まん
     *  33 = 餃子
     */

    //食べた際にポーション効果を付加
    @Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {

		if (TConfig.foodeffect) {
			switch (this.data) {
			case 0:
        		break;
        	case 1:
        		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1200, 0));
        		break;
        	case 3:
        		player.clearActivePotions();
            	player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1200, 1));
        		break;
        	case 4:
        		player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 2, 2));
        		break;
        	case 5:
        		player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 20, 1));
        		break;
        	case 6:
        		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 2400, 0));
    			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2400, 1));
        		break;
        	case 7:
        		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0));
        		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 3));
        		break;
        	case 8:
        		player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1));
        		break;
        	case 9:
        		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1200, 1));
    			player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 30, 1));
        		break;
    		case 10:
    			player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1200, 1));
    			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 1));
    			break;
    		case 11:
    			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 1));
    	    	player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 600, 1));
        		break;
    		case 12:
    			player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 2400, 0));
        		break;
    		case 13:
    			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 0));
        		break;
    		case 14:
    			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1200, 1));
    	    	player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 0));
    	    	player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1200, 1));

    			AxisAlignedBB aabb = player.getEntityBoundingBox().grow(5D, 3D, 5D);
    			List<EntityLivingBase> list = player.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
    			if (list.isEmpty()) { return; }
				for (EntityLivingBase liv : list) {
					liv.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 100, 0));
    			}

        		break;
    		case 15:
    			player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
        		break;
    		case 16:
    			player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 2, 2));
    			break;
    		case 17:
    	    	player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 600, 1));
    			break;
    		case 18:
    	    	if(!world.isRemote) {
    	    		player.world.createExplosion(player, player.posX, player.posY, player.posZ, 4.0F, false);
    	    		player.addPotionEffect(new PotionEffect(PotionInit.tamagotti, 1200, 0));
    	    		player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 0, 1));
    	    		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2400, 0));
    	    	}
    			break;
    		case 19:
    	    	if(!world.isRemote) {
    	    		player.world.createExplosion(player, player.posX, player.posY, player.posZ, 8.0F, false);
    	    		player.addPotionEffect(new PotionEffect(PotionInit.tamagotti, 1200, 0));
    	    		player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 2400, 1));
    	    		player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2400, 1));
    	    		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2400, 1));
    	    	}
    			break;
    		case 20:
    	    	if(!world.isRemote) {
    	    		player.world.createExplosion(player, player.posX, player.posY, player.posZ, 4.0F, false);
    	    		player.addPotionEffect(new PotionEffect(PotionInit.tamagotti, 1200, 0));
    	    		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2400, 4));
    	    	}
    			break;
    		case 24:
    			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1));
    			break;
    		case 25:
    			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1800, 1));
    			player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 10));
    			break;
    		case 26:
    			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2400, 2));
    			player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 10));
    			break;
    		case 27:
    			player.clearActivePotions();
    	    	player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1));
    			break;
    		case 28:
    			player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 2400, 2));
    			break;
    		case 29:
    			int rand1 = rand.nextInt(8);
    			if (rand1 == 0) {
            		player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 1));
    			} else if (rand1 == 1) {
            		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 1));
    			} else if (rand1 == 2) {
            		player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 600, 1));
    			} else if (rand1 == 3) {
            		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 1));
    			} else if (rand1 == 4) {
            		player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 450, 1));
    			} else if (rand1 == 5) {
            		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 600, 1));
    			} else if (rand1 == 6) {
            		player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 1));
    			} else if (rand1 == 7) {
            		player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 600, 1));
            	}
        		break;
    		case 30:
    			int rand2 = rand.nextInt(100) + 1;
    			if (rand2 == 1) {
    				player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 1));
    				player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 2));
    				player.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 3));
    			} else {
    				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 1));
    				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
    				player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 2400, 0));
    			}
        		break;
    		case 31:
    			player.removePotionEffect(MobEffects.SLOWNESS);
    			player.removePotionEffect(MobEffects.WEAKNESS);
    			break;
    		case 32:
    			player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 2400, 1));
    			break;
    		case 33:
    	    	player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 600, 1));
    			player.removePotionEffect(MobEffects.WEAKNESS);
    			break;
        	}
		}

		switch (this.data) {

        case 2:
            player.clearActivePotions();
    		break;
		case 21:
	    	if(!world.isRemote) {
	    		player.world.createExplosion(player, player.posX, player.posY, player.posZ, 4.0F, false);
	    		player.addPotionEffect(new PotionEffect(PotionInit.tamagotti, 1200, 0));
	    	}
			break;
		case 22:
	    	if(!world.isRemote) {
	    		player.world.createExplosion(player, player.posX, player.posY, player.posZ, 16.0F, false);
				player.addPotionEffect(new PotionEffect(PotionInit.tamagotti, 4800, 2));
	    	}
			break;
		case 23:
			player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, 9));
			break;
        }
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
  	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		if (TConfig.foodeffect) {
			switch (this.data) {
  	    	case 0:
  	    		break;
  	    	case 1:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "衝撃吸収"));
  	    		break;
  	    	case 3:
	    		tooltip.add(I18n.format(TextFormatting.GOLD + "ポーション効果を除去、火炎耐性"));
	    		break;
  	    	case 4:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "即時回復"));
  	    		break;
  	    	case 5:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "満腹度回復"));
  	    		break;
  	    	case 6:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "暗視、攻撃力上昇"));
  	    		break;
  	    	case 7:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "暗視、移動速度アップ"));
  	    		break;
  	    	case 8:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "即時回復"));
  	    		break;
  	    	case 9:
  	    		tooltip.add(I18n.format(TextFormatting.GOLD + "耐性、満腹度回復"));
  	    		break;
  			case 10:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "採掘速度アップ、暗視"));
  				break;
  			case 11:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "火炎耐性、採掘速度"));
  	    		break;
  			case 12:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "水中呼吸"));
  	    		break;
  			case 13:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "再生速度アップ"));
  	    		break;
  			case 14:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "火炎耐性、耐性、衝撃吸収 + 範囲のモブに回復効果"));
  	    		break;
  			case 15:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "再生速度アップ"));
  	    		break;
  			case 16:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "即時回復"));
  				break;
  			case 17:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "透明"));
  				break;
  			case 18:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "爆発を発生+プラス効果"));
  				break;
  			case 19:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "爆発+色々"));
  				break;
  			case 20:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "移動速度アップ"));
  				break;
  			case 24:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "攻撃力アップ"));
  				break;
  			case 25:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "攻撃力アップ、即時回復"));
  				break;
  			case 26:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "移動速度アップ、即時回復"));
  				break;
  			case 27:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "ポーション効果除去、攻撃力アップ"));
  				break;
  			case 28:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "体力増強"));
  				break;
  			case 29:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "何が起こるかわからない"));
  				break;
  			case 30:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "プラス効果またはマイナス効果"));
  				break;
  			case 31:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "弱体化、移動速度ダウン除去"));
  				break;
  			case 32:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "釣り運アップ"));
  			case 33:
  				tooltip.add(I18n.format(TextFormatting.GOLD + "採掘速度アップ + 弱体化除去"));
  	  		}
  		}

		switch (this.data) {
		case 2:
			tooltip.add(I18n.format(TextFormatting.GOLD + "ポーション効果を除去"));
			break;
		case 21:
		case 22:
			tooltip.add(I18n.format(TextFormatting.GOLD + "爆発"));
			break;
		case 23:
			tooltip.add(I18n.format(TextFormatting.GOLD + "翼を授ける"));
			break;
		}
	}

  	//食べ方を飲み物に
    @Override
	public EnumAction getItemUseAction(ItemStack stack){
    	if(this.data == 2 || this.data == 23 || this.data ==27) {
			return EnumAction.DRINK;
		} else {
    		return EnumAction.EAT;
    	}
    }

    @Override
	public int getMaxItemUseDuration(ItemStack stack){
    	if(this.data == 2 || this.data == 4 || this.data == 16 || this.data == 23 || this.data == 26) {
    		return 16;
    	} else if(this.data == 3 || this.data == 8 || this.data == 9 || this.data == 10 || this.data == 13 || this.data == 18) {
			return 24;
		} else if (this.data == 7 || this.data == 19) {
			return 12;
		} else if (this.data == 5) {
			return 44;
		} else {
    		return 32;
    	}
    }

	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		if (this.data == 21 && entity instanceof EntityPlayer) {
			player.startRiding(entity, true);
			return true;
		}
		return false;
	}
}
