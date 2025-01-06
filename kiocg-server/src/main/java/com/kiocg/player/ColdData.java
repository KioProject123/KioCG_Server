package com.kiocg.player;

import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class ColdData {
    public static final int MAX_VALUE = 60 * 20 * 10;

    private int coldValue = MAX_VALUE;

    public int getColdValue() {
        return coldValue;
    }

    public void setColdValue(int value) {
        coldValue = value;
    }

    public void addColdValue(int add) {
        coldValue = Mth.clamp(coldValue + add, 0, MAX_VALUE);
    }

    public float getColdProgress() {
        return (float) coldValue / MAX_VALUE;
    }

    public boolean isFrozen(Player player) {
        return coldValue <= 0 && !player.getAbilities().invulnerable;
    }

    public boolean isInWater;
    public boolean isInLava;

    public void tick(ServerPlayer player) {
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

            if (config.isInWaterOrBubble != 0 && (this.isInWater || player.isInPowderSnow || player.isInWaterOrBubble())) {
                playerTemperature += config.isInWaterOrBubble;
            } else if (config.isInRain != 0 && player.isInRain()) {
                playerTemperature += config.isInRain;
            }

            if (config.isInLava != 0 && (this.isInLava || player.isInLava())) {
                playerTemperature += config.isInLava;
            } else if (config.isOnFire != 0 && player.getRemainingFireTicks() > 0) {
                playerTemperature += config.isOnFire;
            }

            double finalTemperature = (playerTemperature + ambientTemperature) * config.finalTemperatureMultiplier;
            addColdValue((int) finalTemperature);
            player.connection.send(new ClientboundSetExperiencePacket(getColdProgress(), player.totalExperience, player.experienceLevel));
        }

        isInWater = false;
        isInLava = false;
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("KioCG.ColdValue")) {
            coldValue = nbt.getInt("KioCG.ColdValue");
        }
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("KioCG.ColdValue", coldValue);
    }
}
