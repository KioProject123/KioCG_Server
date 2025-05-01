package com.kiocg.entity.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ThirstData {
    public static final int MAX_VALUE = 20;
    public static final float MAX_REGAIN = 20.0F;
    public static final int AIR_OFFSET_TICK = 5;

    private final Player player;
    private int thirstValue = MAX_VALUE;
    private float thirstRegain;

    // 在水下偏移氧气条数据包
    public boolean underWater;

    public ThirstData(Player player) {
        this.player = player;
    }

    public int getThirstValue() {
        return thirstValue;
    }

    public boolean needsWater() {
        return this.thirstValue < MAX_VALUE;
    }

    public void setThirstValue(int value) {
        thirstValue = value;
    }

    public void addThirstValue(int add) {
        int value = thirstValue + add;
        if (value > MAX_VALUE) {
            value = MAX_VALUE;
            thirstRegain = 0;
        }
        thirstValue = Math.max(value, 0);
    }

    public float getThirstRegain() {
        return thirstRegain;
    }

    public void setThirstRegain(float value) {
        thirstRegain = value;
    }

    public void addThirstRegain(float add) {
        final Level world = player.level();
        if (world.getBiome(player.blockPosition()).value().climateSettings.downfall() <= 0.0) {
            thirstRegain += (float) (add * world.paperConfig().kiocgConfig.theLongDark.thirstValue.lowDownfallBiomeMultiplier);
            return;
        }

        thirstRegain += add;
    }

    // 10: 295-273
    // 9: 265-243
    // 8: 235-213
    // ...
    // 1: 25-3
    // 0: -5--33
    private int getThirstProgress() {
        int thirstValue = Mth.ceil(this.thirstValue / 2);
        if (!underWater) {
            return (300 / 10) * (thirstValue - 1) + (3 + AIR_OFFSET_TICK);
        } else {
            return (300 / 10) * thirstValue - (5 + AIR_OFFSET_TICK);
        }
    }

    public boolean isThirsty(Player player) {
        return thirstValue <= 0 && !player.getAbilities().invulnerable;
    }

    public void tick() {
        if (!player.getAbilities().invulnerable) {
            if (thirstRegain > MAX_REGAIN) {
                thirstRegain -= MAX_REGAIN;
                if (player.level().getDifficulty() != Difficulty.PEACEFUL) {
                    addThirstValue(-1);
                }
            }

            if (thirstValue <= 6 && player.tickCount % 20 == 0) {
                if (!player.hasEffect(MobEffects.WEAKNESS) || player.getEffect(MobEffects.WEAKNESS).getAmplifier() == 0 && player.getEffect(MobEffects.WEAKNESS).endsWithin(11 * 20)) {
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 11 * 20, 0));
                }
            }

            player.getEntityData().set(Entity.DATA_AIR_SUPPLY_ID, getThirstProgress(), true);
        }
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        thirstValue = nbt.getIntOr("KioCG.ThirstValue", MAX_VALUE);
        thirstRegain = nbt.getFloatOr("KioCG.ThirstRegain", 0.0F);
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("KioCG.ThirstValue", thirstValue);
        nbt.putFloat("KioCG.ThirstRegain", thirstRegain);
    }
}
