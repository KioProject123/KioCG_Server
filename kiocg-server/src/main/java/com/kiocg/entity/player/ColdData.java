package com.kiocg.entity.player;

import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ColdData {
    public static final int MAX_VALUE = 60 * 20 * 10;
    private static final float STIFFNESS = 0.1f; // 刚度
    private static final float DAMPING = 0.5f; // 阻尼

    private final Player player;
    private int targetTemp = MAX_VALUE;
    private int currentTemp = targetTemp;

    private float velocity;

    public ColdData(Player player) {
        this.player = player;
    }

    public int getColdValue() {
        return targetTemp;
    }

    public void setColdValue(int value) {
        targetTemp = value;
    }

    public void addColdValue(int add) {
        targetTemp = Mth.clamp(targetTemp + add, 0, MAX_VALUE);
    }

    public float getColdProgress() {
        return (float) currentTemp / MAX_VALUE;
    }

    public boolean isFrozen(Player player) {
        return currentTemp <= 0 && !player.getAbilities().invulnerable;
    }

    public boolean isInWater;
    public boolean isInLava;

    public void tick() {
        if (!player.getAbilities().invulnerable) {
            final Level world = player.level();
            final BlockPos pos = player.blockPosition();
            final WorldConfiguration.KiocgConfig.TheLongDark.ColdValue config = world.paperConfig().kiocgConfig.theLongDark.coldValue;

            double ambientTemperature = config.ambientTemperatureBase;
            if (config.ambientSkyDarkenMultiplier != 0) {
                ambientTemperature += world.getSkyDarken() * config.ambientSkyDarkenMultiplier * world.getBrightness(LightLayer.SKY, pos);
            }
            if (config.biomeTemperatureMultiplier != 0) {
                ambientTemperature += (world.getBiome(pos).value().getTemperature(pos, world.getSeaLevel()) + config.biomeTemperatureOffset) * config.biomeTemperatureMultiplier;
            }

            double playerTemperature = config.playerTemperatureBase + player.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE); // 属性用于玩家温度
            if (config.playerLightBlockMultiplier != 0.0) {
                int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
                playerTemperature += Math.min(blockLight, 12) * config.playerLightBlockMultiplier;
            }

            if (config.isInWater != 0 && this.isInWater) {
                playerTemperature += config.isInWater;
            } else if (config.isInRain != 0 && player.isInRain()) {
                playerTemperature += config.isInRain;
            }

            if (config.isInLava != 0 && this.isInLava) {
                playerTemperature += config.isInLava;
            } else if (config.isOnFire != 0 && player.getRemainingFireTicks() > 0) {
                playerTemperature += config.isOnFire;
            }

            double finalTemperature = (playerTemperature + ambientTemperature) * config.finalTemperatureMultiplier;
            addColdValue((int) finalTemperature);
            ((ServerPlayer) player).connection.send(new ClientboundSetExperiencePacket(getColdProgress(), player.totalExperience, player.experienceLevel));
        }

        updateCurrentTemp();

        isInWater = false;
        isInLava = false;
    }

    private void updateCurrentTemp() {
        float displacement = targetTemp - currentTemp;
        float acceleration = STIFFNESS * displacement - DAMPING * velocity;
        velocity += acceleration;
        currentTemp = Mth.clamp((int) (currentTemp + velocity), 0, MAX_VALUE);
    }

    public void readAdditionalSaveData(ValueInput input) {
        targetTemp = input.getIntOr("KioCG.ColdValue", MAX_VALUE);
        currentTemp = targetTemp;
    }

    public void addAdditionalSaveData(ValueOutput output) {
        output.putInt("KioCG.ColdValue", targetTemp);
    }
}
