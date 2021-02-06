package tamagotti.init.entity.monster;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import tamagotti.init.event.TSoundEvent;

public class EntityAoi extends EntityVillager {

	@Nullable
	private MerchantRecipeList buyingList;
	@Nullable
	private EntityPlayer buyingPlayer;
	private int careerId;
	private final InventoryBasic inventory;
	public float renderOffsetX;
	public float renderOffsetY;
	public float renderOffsetZ;
	private int timeUntilReset;
	private boolean needsInitilization;
	private boolean isWillingToMate;
	private int wealth;
	private UUID lastBuyingPlayer;

	public EntityAoi(World world) {
		this(world, 0);
	}

	public EntityAoi(World world, int professionId) {
		super(world);
		this.inventory = new InventoryBasic("Items", false, 8);
		this.setProfession(professionId);
		this.setSize(0.6F, 1.95F);
		((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected float getSoundPitch() {
		return 1F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) {

		if (src.getTrueSource() instanceof IMob) {

			EntityLivingBase zombie = (EntityLivingBase) src.getTrueSource();
			zombie.setHealth(0);

			EntityAoi aoi = new EntityAoi(this.world);
			aoi.setGrowingAge(0);
			aoi.setProfession(this.world.rand.nextInt(4));
			aoi.setLocationAndAngles(zombie.posX, zombie.posY, zombie.posZ, zombie.rotationYaw, 0.0F);
	        this.world.spawnEntity(aoi);
			return false;
		}

		super.attackEntityFrom(src, damage);
		return true;
	}

	//名前の設定
	@Override
	public ITextComponent getDisplayName() {
		Team team = this.getTeam();
		String s = this.getCustomNameTag();
		if (s != null && !s.isEmpty()) {
			TextComponentString text = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
			text.getStyle().setHoverEvent(this.getHoverEvent());
			text.getStyle().setInsertion(this.getCachedUniqueIdString());
			return text;
		} else {
			String s1 = "琴葉葵";
			{
				ITextComponent itext = new TextComponentTranslation(s1, new Object[0]);
				itext.getStyle().setHoverEvent(this.getHoverEvent());
				itext.getStyle().setInsertion(this.getCachedUniqueIdString());
				if (team != null) {
					itext.getStyle().setColor(team.getColor());
				}
				return itext;
			}
		}
	}

	//普段の音
	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTrading() ? TSoundEvent.AOI_VOICE_1 : TSoundEvent.AOI_VOICE_0;
	}

	//ダメージを受けたときの音
	@Override
	protected SoundEvent getHurtSound(DamageSource damage) {
		return TSoundEvent.AOI_DAMAGE;
	}

	//死んだときの音
	@Override
	protected SoundEvent getDeathSound() {
		return TSoundEvent.AOI_DETH;
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		recipe.incrementToolUses();
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound(TSoundEvent.AOI_VOICE_1, this.getSoundVolume(), this.getSoundPitch());
		int i = 5 + this.rand.nextInt(30) + 20;

		if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
			this.timeUntilReset = 40;
			this.needsInitilization = true;
			this.isWillingToMate = true;
			if (this.buyingPlayer != null) {
				this.lastBuyingPlayer = this.buyingPlayer.getUniqueID();
			} else {
				this.lastBuyingPlayer = null;
			}
			i += 5;
		}

		if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
			this.wealth += recipe.getItemToBuy().getCount();
		}

		if (recipe.getRewardsExp()) {
			this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
		}

		if (this.buyingPlayer instanceof EntityPlayerMP) {
			CriteriaTriggers.VILLAGER_TRADE.trigger((EntityPlayerMP) this.buyingPlayer, this, recipe.getItemToSell());
		}
	}

	//交換時の音
	@Override
	public void verifySellingItem(ItemStack stack) {
		if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playSound(stack.isEmpty() ? TSoundEvent.AOI_NO : TSoundEvent.AOI_VOICE_1, this.getSoundVolume(), this.getSoundPitch());
		}
	}
}
