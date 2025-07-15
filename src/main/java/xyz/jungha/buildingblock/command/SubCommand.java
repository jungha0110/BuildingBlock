package xyz.jungha.buildingblock.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    String getName();

    String getUsage();

    boolean hasPermission(CommandSender sender);

    boolean execute(CommandSender sender, String[] args);

    List<String> onTabComplete(CommandSender sender, String[] args);
}