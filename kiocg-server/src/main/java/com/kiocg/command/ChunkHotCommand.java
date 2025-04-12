package com.kiocg.command;

import com.kiocg.task.TPSBarTask;
import com.mojang.brigadier.CommandDispatcher;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Collection;
import java.util.Collections;

public class ChunkHotCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chunksan")
                                    .requires(listener -> listener.hasPermission(2, "bukkit.command.chunksan"))
                                    .executes(context -> execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())))
                                    .then(Commands.argument("targets", EntityArgument.players())
                                                  .requires(listener -> listener.hasPermission(2, "bukkit.command.chunksan.other"))
                                                  .executes(context -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                                         )
                           );
    }

    private static int execute(CommandSourceStack sender, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            LevelChunk chunk = player.level().getChunkIfLoaded(player.blockPosition());
            long chunkhot = chunk != null ? chunk.getChunkHot().getAverage() : 0L;
            Component component = MiniMessage.miniMessage().deserialize("<green>[<aqua>豆渣子<green>] <gold>玩家<target>的区域SAN: <totalhot>, 所处区块SAN: <white><chunkhot>",
                                                                        Placeholder.parsed("target", player.getGameProfile().getName()),
                                                                        Placeholder.component("totalhot", TPSBarTask.instance().getChunkHotColor(player, false)),
                                                                        Placeholder.parsed("chunkhot", String.valueOf(chunkhot)));
            sender.sendSuccess(() -> PaperAdventure.asVanilla(component), false);
        }
        return targets.size();
    }
}
