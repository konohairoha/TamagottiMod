package tamagotti.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityTamagotti extends EntityArrow implements IProjectile {

	protected int tickTime = 0;
	public float exexplosion;

	public EntityTamagotti(World worldIn) {
		super(worldIn);
	}

	public EntityTamagotti(World worldIn, EntityLivingBase throwerIn, float ex) {
		super(worldIn, throwerIn);
		this.exexplosion = ex;
	}

	public EntityTamagotti(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.tickTime++;
		if (this.tickTime > 60) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.exexplosion, true);
			this.setDead();

			// 水の中にあるとき
			if (!this.world.isRemote && this.isInWater()) {

				for (int i = 0; i < (int)this.exexplosion; i++) {

					if (this.rand.nextFloat() < 0.1F) {

						// 座標を変えるためにランダム用意
						int xRand = this.rand.nextInt(7);
						int yRand = this.rand.nextInt(3);
						int zRand = this.rand.nextInt(7);
						double x = this.posX + 0.5F + xRand - 3;
						double y = this.posY + 0.5F + yRand;
						double z = this.posZ + 0.5F + zRand - 3;

						LootContext.Builder lootcontext = new LootContext.Builder((WorldServer) this.world);

						// ルートテーブルをリストに入れる
						List<ItemStack> items = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING)
								.generateLootForPools(this.world.rand, lootcontext.build());

						// アイテムをスポーン
						for (ItemStack stack : items) {
							this.world.spawnEntity(new EntityItem(this.world, x, y, z, stack));
						}
					}
				}


			}
		}
	}

	//アイテムを拾えないように
	@Override
	public ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	//エンティティに当たると爆発
	@Override
	protected void arrowHit(EntityLivingBase living) {
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.exexplosion, true);
		this.setDead();
	}
}