package com.kiocg.task;

import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.configuration.WorldConfiguration;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class TPSBarTask extends BossBarTask {
    private static final Predicate<Double> GOOD_TPS = value -> Math.round(value) >= 20;
    private static final Predicate<Double> Medium_TPS = value -> value >= 15;
    private static final Predicate<Double> GOOD_MSPT = value -> value <= 50;
    private static final Predicate<Double> Medium_MSPT = value -> value <= 65;
    private static final Predicate<Integer> GOOD_PING = value -> value < 100;
    private static final Predicate<Integer> Medium_PING = value -> value < 200;

    private static TPSBarTask instance;
    private double tps = 20.0D;
    private double mspt = 0.0D;
    private int tick = 0;

    public static TPSBarTask instance() {
        if (instance == null) {
            instance = new TPSBarTask();
        }
        return instance;
    }

    @Override
    BossBar createBossBar() {
        return BossBar.bossBar(Component.text(""), 0.0F, GlobalConfiguration.get().kiocgConfig.commandTpsBarProgressColorGood, GlobalConfiguration.get().kiocgConfig.commandTpsBarProgressOverlay);
    }

    @Override
    void updateBossBar(BossBar bossbar, Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        bossbar.progress(getBossBarProgress((float) serverPlayer.getNearbyChunkHot(), (float) serverPlayer.level().paperConfig().kiocgConfig.chunkHot.phaseGood));
        bossbar.color(getBossBarColor(serverPlayer));
        bossbar.name(MiniMessage.miniMessage().deserialize(GlobalConfiguration.get().kiocgConfig.commandTpsBarTitle,
                                                           Placeholder.component("hotpct", getChunkHotColor(serverPlayer, true)),
                                                           Placeholder.component("tps", getTPSColor()),
                                                           Placeholder.component("mspt", getMSPTColor()),
                                                           Placeholder.component("ping", getPingColor(player.getPing()))
                                                          ));
    }

    @Override
    public void run() {
        if (++tick < GlobalConfiguration.get().kiocgConfig.commandTpsBarTickInterval) {
            return;
        }
        tick = 0;

        this.tps = Math.min(MinecraftServer.getServer().tps1.getAverage(), 20.0D);
        this.mspt = Bukkit.getAverageTickTime();

        super.run();
    }

    private float getBossBarProgress(float dividend, float divisor) {
        return Math.max(Math.min(dividend / divisor, 1.0F), 0.0F);
    }

    private BossBar.Color getBossBarColor(net.minecraft.server.level.ServerPlayer player) {
        long hot = player.getNearbyChunkHot();
        WorldConfiguration.KiocgConfig.ChunkHot hotCfg = player.level().paperConfig().kiocgConfig.chunkHot;
        if (hot < hotCfg.phaseGood) {
            return GlobalConfiguration.get().kiocgConfig.commandTpsBarProgressColorGood;
        } else if (hot < hotCfg.phaseMedium) {
            return GlobalConfiguration.get().kiocgConfig.commandTpsBarProgressColorMedium;
        } else {
            return GlobalConfiguration.get().kiocgConfig.commandTpsBarProgressColorLow;
        }
    }

    private Component getTPSColor() {
        String color = getColor(tps, GOOD_TPS, Medium_TPS);
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%.2f", tps)));
    }

    private Component getMSPTColor() {
        String color = getColor(mspt, GOOD_MSPT, Medium_MSPT);
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%.2f", mspt)));
    }

    private Component getPingColor(int ping) {
        String color = getColor(ping, GOOD_PING, Medium_PING);
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%s", ping)));
    }

    public Component getChunkHotColor(net.minecraft.server.level.ServerPlayer player, boolean pct) {
        WorldConfiguration.KiocgConfig.ChunkHot hotCfg = player.level().paperConfig().kiocgConfig.chunkHot;
        Predicate<Long> GOOD_HOT = value -> value < hotCfg.phaseGood;
        Predicate<Long> Medium_HOT = value -> value < hotCfg.phaseMedium;
        long hot = player.getNearbyChunkHot();
        String color = getColor(hot, GOOD_HOT, Medium_HOT);
        hot = pct ? GOOD_HOT.test(hot) ? (hotCfg.phaseGood - hot) * 100 / hotCfg.phaseGood : (hotCfg.phaseMedium - hot) * 100 / hotCfg.phaseMedium : hot;
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%s", hot)));
    }

    private <T> String getColor(T value, Predicate<T> goodValue, Predicate<T> mediumValue) {
        if (goodValue.test(value)) {
            return GlobalConfiguration.get().kiocgConfig.commandTpsBarTextColorGood;
        } else if (mediumValue.test(value)) {
            return GlobalConfiguration.get().kiocgConfig.commandTpsBarTextColorMedium;
        } else {
            return GlobalConfiguration.get().kiocgConfig.commandTpsBarTextColorLow;
        }
    }
}
