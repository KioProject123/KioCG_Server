package com.kiocg.player;

import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ThirstData {
    public static final int MAX_VALUE = 10;

    private int thirstValue = MAX_VALUE;
    private float thirstRegain;

    public int getThirstValue() {
        return thirstValue;
    }

    public void setThirstValue(int value) {
        thirstValue = value;
    }

    public void addThirstValue(int add) {
        thirstValue = Mth.clamp(thirstValue + add, 0, MAX_VALUE);
    }

    public float getThirstRegain() {
        return thirstRegain;
    }

    public void setThirstRegain(float value) {
        thirstRegain = value;
    }

    public void addThirstRegain(float add) {
        thirstRegain += add;
    }

    private int getThirstProgress() {
        return 300 / 10 * (thirstValue - 1) + 8;
    }

    public boolean isThirsty(Player player) {
        return thirstValue <= 0 && !player.getAbilities().invulnerable;
    }

    public void tick(ServerPlayer player) {
        if (!player.getAbilities().invulnerable) {
            final Level world = player.level();
            final BlockPos pos = player.blockPosition();
            final WorldConfiguration.KiocgConfig.TheLongDark.ThirstValue config = world.paperConfig().kiocgConfig.theLongDark.thirstValue;

            if (world.getBiome(pos).value().climateSettings.downfall() <= 0.0) {
                addThirstRegain((float) config.lowDownfallBiomeThirst);
            }

            if (thirstRegain > 20.0F) {
                thirstRegain -= 20.0F;
                addThirstValue(-1);
            }

            if (thirstValue <= 3 && player.tickCount % 10 == 0) {
                if (!player.hasEffect(MobEffects.WEAKNESS) || player.getEffect(MobEffects.WEAKNESS).getAmplifier() == 0 && player.getEffect(MobEffects.WEAKNESS).endsWithin(20)) {
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0));
                }
            }

            player.getEntityData().set(Entity.DATA_AIR_SUPPLY_ID, getThirstProgress(), true);
        }
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("KioCG.ThirstValue")) {
            thirstValue = nbt.getInt("KioCG.ThirstValue");
            thirstRegain = nbt.getFloat("KioCG.ThirstRegain");
        }
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("KioCG.ThirstValue", thirstValue);
        nbt.putFloat("KioCG.ThirstRegain", thirstRegain);
    }
}
