package com.kiocg.command;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TickerViewCommand {
    private static final Set<ServerPlayer> players = Sets.newHashSet();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tickerview")
                                    .requires(listener -> listener.hasPermission(2, "bukkit.command.tickerview"))
                                    .executes(context -> execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())))
                                    .then(Commands.argument("targets", EntityArgument.players())
                                                  .requires(listener -> listener.hasPermission(2, "bukkit.command.tickerview.other"))
                                                  .executes(context -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                                         )
                           );
    }

    private static int execute(CommandSourceStack sender, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            if (!players.add(player)) {
                players.remove(player);
            }
            Component component = MiniMessage.miniMessage().deserialize("<green>[<aqua>豆渣子<green>] <gold>已<onoff>玩家 <target> 的方块实体刻监测.",
                                                                        Placeholder.parsed("target", player.getGameProfile().getName()),
                                                                        Placeholder.parsed("onoff", players.contains(player) ? "<green>开启</green>" : "<red>关闭</red>"));
            sender.sendSuccess(() -> PaperAdventure.asVanilla(component), false);
        }
        return targets.size();
    }

    public static void onBlockEntityTicking(Level level, BlockPos pos, BlockState state) {
        if (!players.isEmpty()) {
            Set<ServerPlayer> viewers = new HashSet<>(players);
            viewers.removeIf(serverPlayer -> !serverPlayer.level().equals(level) || serverPlayer.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 64 * 64);

            if (!viewers.isEmpty()) {
                GameTestAddMarkerDebugPayload payload = new GameTestAddMarkerDebugPayload(pos, ARGB.opaque(state.getMapColor(level, pos).col), "", 50 - 10);
                Packet<?> packet = new ClientboundCustomPayloadPacket(payload);
                viewers.forEach(serverPlayer -> serverPlayer.connection.send(packet));
            }
        }
    }

    public static void onPlayerQuit(ServerPlayer player) {
        players.remove(player);
    }
}
