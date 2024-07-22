package org.rce4j.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.rce4j.command.stream.SpigotJLoggerCommandStream;
import org.rce4j.command.stream.CommandStream;

import java.util.UUID;

import static org.rce4j.StaticPersistingContainer.runTaskOnBukkitThread;

public class BukkitCommandLine implements CommandLine {
    private final CommandStream commandStream = new SpigotJLoggerCommandStream();
    
    @Override
    public void dispatchCommand(String command) {
        runTaskOnBukkitThread(() -> {
            CommandSender commandSender = Bukkit.getConsoleSender();
            Bukkit.dispatchCommand(commandSender, command);
        });
    }
    
    @Override
    public void dispatchCommand(String command, String as) {
        runTaskOnBukkitThread(() -> {
            CommandSender commandSender = Bukkit.getPlayer(UUID.fromString(as));
            if (commandSender == null)
                throw new IllegalArgumentException("Player \"%s\" does not exist".formatted(as));
            Bukkit.dispatchCommand(commandSender, command);
        });
    }
    
    @Override
    public CommandStream getCommandStream() {
        return commandStream;
    }
}
