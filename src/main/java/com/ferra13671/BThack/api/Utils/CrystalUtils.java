package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;

public final class CrystalUtils implements Mc {

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        try {
            double factor = (1.0 - MathUtils.getDistance(entity.getPos(), new Vec3d(posX, posY, posZ)) / 12.0) * getBlockDensity(new Vec3d(posX, posY, posZ), entity.getBoundingBox());

            float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0f * 7.0f * 12.0f + 1.0f);

            double damage = 1.0;

            if (entity instanceof LivingEntity) {
                damage = getBlastReduction((LivingEntity) entity, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, entity, posX, posY, posZ, 6.0f, false, Explosion.DestructionType.DESTROY));
            }

            return (float) damage;
        } catch (Exception ignored) {}

        return 0.0f;
    }

    public static float getBlastReduction(LivingEntity entityLivingBase, float damage, Explosion explosion) {
        if (entityLivingBase instanceof PlayerEntity) {
            damage = getDamageAfterAbsorb(damage, (float) entityLivingBase.getArmor(), (float) entityLivingBase.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getProtectionAmount(entityLivingBase.getArmorItems(), mc.world.getDamageSources().explosion(explosion)), 0.0f, 20.0f) / 25.0f;

            if (entityLivingBase.hasStatusEffect(StatusEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }

            return damage;
        }

        damage = getDamageAfterAbsorb(damage, (float) entityLivingBase.getArmor(), (float) entityLivingBase.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));

        return damage;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        Block block = mc.world.getBlockState(blockPos).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN || block == Blocks.BEDROCK;
    }

    public static float getDamageAfterAbsorb(float p_getDamageAfterAbsorb_0_, float p_getDamageAfterAbsorb_1_, float p_getDamageAfterAbsorb_2_) {
        float lvt_3_1_ = 2.0F + p_getDamageAfterAbsorb_2_ / 4.0F;
        float lvt_4_1_ = MathHelper.clamp(p_getDamageAfterAbsorb_1_ - p_getDamageAfterAbsorb_0_ / lvt_3_1_, p_getDamageAfterAbsorb_1_ * 0.2F, 20.0F);
        return p_getDamageAfterAbsorb_0_ * (1.0F - lvt_4_1_ / 25.0F);
    }

    public static float getBlockDensity(Vec3d p_getBlockDensity_1_, Box p_getBlockDensity_2_) {
        double d0 = 1.0 / ((p_getBlockDensity_2_.maxX - p_getBlockDensity_2_.minX) * 2.0 + 1.0);
        double d1 = 1.0 / ((p_getBlockDensity_2_.maxY - p_getBlockDensity_2_.minY) * 2.0 + 1.0);
        double d2 = 1.0 / ((p_getBlockDensity_2_.maxZ - p_getBlockDensity_2_.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        if (d0 >= 0.0 && d1 >= 0.0 && d2 >= 0.0) {
            int j2 = 0;
            int k2 = 0;

            for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
                for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
                    for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                        double d5 = p_getBlockDensity_2_.minX + (p_getBlockDensity_2_.maxX - p_getBlockDensity_2_.minX) * (double)f;
                        double d6 = p_getBlockDensity_2_.minY + (p_getBlockDensity_2_.maxY - p_getBlockDensity_2_.minY) * (double)f1;
                        double d7 = p_getBlockDensity_2_.minZ + (p_getBlockDensity_2_.maxZ - p_getBlockDensity_2_.minZ) * (double)f2;
                        if (!BlockUtils.hasLineOfSight(new Vec3d(d5 + d3, d6, d7 + d4), p_getBlockDensity_1_)) {
                            ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float)j2 / (float)k2;
        } else {
            return 0.0F;
        }
    }
}
