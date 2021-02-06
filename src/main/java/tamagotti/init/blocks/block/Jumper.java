package tamagotti.init.blocks.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.iitem.ILink;
import tamagotti.init.tile.TileJumper;
import tamagotti.init.tile.TileLCESpaner;
import tamagotti.init.tile.TileLink;
import tamagotti.init.tile.TileOverclock;

public class Jumper extends TIron {

	private final int data;

    public Jumper(String name, float hardness, float resistance, int harvestLevel, float light, int meta) {
		super(name, hardness, resistance, harvestLevel, light);
        this.data = meta;
    }

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return this.data == 1 ? new AxisAlignedBB(0D, 0D, 0D, 1D, 0.9D, 1D) : new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	/**
	 * 0 = モブジャンパー
	 * 1 = 範囲テレポブロック
	 * 3 = オーバークロッカー
	 * 4 = スポナー
	 */

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:
			return new TileJumper();
		case 1:
			return new TileLink();
		case 3:
			return new TileOverclock();
		case 4:
			return new TileLCESpaner();
		}
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		// 範囲テレポブロックなら
		if (this.data != 1) { return false; }

		// テレポートアイテムで右クリック
		if (!(player.getHeldItem(hand).getItem() instanceof ILink)) { return false; }

		// メインハンドのNBT取得
		NBTTagCompound tags = player.getHeldItemMainhand().getTagCompound();

		// NBTがないか、登録している座標がy0以下なら終了
		if (tags == null || tags.getInteger("y") <= 0) { return false; }

		TileLink tile = (TileLink) world.getTileEntity(pos);
		tile.posX = tags.getInteger("x");
		tile.posY = tags.getInteger("y");
		tile.posZ = tags.getInteger("z");


		// 座標とえんちちを保存するMapを作成
		Map<BlockPos, EntityLivingBase> entityMap = new HashMap<>();

		// 範囲内をえんちちのListに入れる
		List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-4, -4, -4), pos.add(4, 4, 4)));

		// 範囲内のえんちちをMapに入れる
		for (EntityLivingBase entity: entityList) {

			// 敵モブなら次へ
			if (entity instanceof IMob) { continue; }

			// ブロックから離れている距離を計算
			BlockPos p = new BlockPos(pos.getX() - entity.posX, pos.getY() - entity.posY, pos.getZ() - entity.posZ);

			// 座標情報とえんちちーをMapに入れる
			entityMap.put(p, entity);
		}

		// Mapを回す
		for(Map.Entry<BlockPos, EntityLivingBase> entry : entityMap.entrySet()){

			EntityLivingBase entity = entry.getValue();	// えんちちを取得
			BlockPos p = entry.getKey();					// 座標取得

			// テレポート先に離れている分だけ座標をずらす
			double x = tags.getInteger("x") + p.getX() + 0.5F;
			double y = tags.getInteger("y") - p.getY() + 0.5F;
			double z = tags.getInteger("z") + p.getZ() + 0.5F;

			// テレポートメソッドの呼び出し
			entity.setPositionAndUpdate(x, y, z);
			entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
		}

		return true;
	}

	// ブロックの上にいたら
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (this.data != 1 || !(entity instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) entity;

		if (!player.isSneaking()) { return; }

		TileLink tile = (TileLink) world.getTileEntity(pos);

		// テレポートメソッドの呼び出し
		player.setPositionAndUpdate(tile.posX, tile.posY, tile.posZ);
		player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		if (this.data == 3) {
			if (Keyboard.isKeyDown(42)) {
				tooltip.add(I18n.format(TextFormatting.GOLD + "特定ブロックの処理速度を上昇(重複可)"));
				tooltip.add(I18n.format(TextFormatting.GOLD + "・たまごっちホッパー"));
				tooltip.add(I18n.format(TextFormatting.GOLD + "・たまごっち鍋、たまごっちディスプレイ"));
				tooltip.add(I18n.format(TextFormatting.GOLD + "・たまごっちスポナー"));
			} else {
				tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
			}
		} else if (this.data == 1) {
			tooltip.add(I18n.format(TextFormatting.GOLD + "・たまごっちリンクを持って右クリックで範囲テレポート"));
		}
    }

	// フェンスとかにつながないように
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

  	// テレポートイベントクラス
  	public class EnderTeleportEvent extends LivingEvent {

  		private double targetX;
  		private double targetY;
  		private double targetZ;
  		private float attackDamage;

  		public EnderTeleportEvent(EntityLivingBase entity, double x, double y, double z, float dame) {
  			super(entity);
  			this.setTargetX(x);
			this.setTargetY(y);
			this.setTargetZ(z);
			this.setAttackDamage(dame);
  		}

  		public double getTargetX() { return targetX; }
  		public void setTargetX(double targetX) { this.targetX = targetX; }
  		public double getTargetY() { return targetY; }
  		public void setTargetY(double targetY) { this.targetY = targetY; }
  		public double getTargetZ() { return targetZ; }
  		public void setTargetZ(double targetZ) { this.targetZ = targetZ; }
  		public float getAttackDamage() { return attackDamage; }
  		public void setAttackDamage(float attackDamage) { this.attackDamage = attackDamage; }
	}
}