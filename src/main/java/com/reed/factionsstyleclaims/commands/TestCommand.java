package com.reed.factionsstyleclaims.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.reed.factionsstyleclaims.FactionsStyleClaims;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

public class TestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher
                .register(Commands.literal("runboyrun")
                .requires((disp) -> {return disp.hasPermission(2);})
                .executes((cmd) -> {
                    FactionsStyleClaims.LOGGER.info("The boy ran");
                    FactionsStyleClaims.SERVER.getPlayerList().broadcastMessage(new TextComponent("That boy RAN"), ChatType.SYSTEM, Util.NIL_UUID);
                    return 1;
                }));
    }
}
