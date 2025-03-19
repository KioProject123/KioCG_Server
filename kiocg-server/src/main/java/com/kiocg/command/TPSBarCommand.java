package com.kiocg.command;

import com.kiocg.task.TPSBarTask;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;

public class TPSBarCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpsbar")
                                    .requires(listener -> listener.hasPermission(2, "bukkit.command.tpsbar"))
                                    .executes(context -> execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())))
                                    .then(Commands.argument("targets", EntityArgument.players())
                                                  .requires(listener -> listener.hasPermission(2, "bukkit.command.tpsbar.other"))
                                                  .executes((context) -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                                         )
                           );
    }

    private static int execute(CommandSourceStack sender, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            TPSBarTask.instance().togglePlayer(player.getBukkitEntity());
        }
        return targets.size();
    }
}
