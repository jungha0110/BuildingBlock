package xyz.jungha.buildingblock.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.*;
import java.util.stream.Collectors;

public class ManagerCommand implements TabExecutor {

    private final Map<String, SubCommand> subCommands;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public ManagerCommand(List<SubCommand> subCommands, ChunkService chunkService) {
        this.subCommands = subCommands.stream()
                .collect(Collectors.toUnmodifiableMap(SubCommand::getName, subCommand -> subCommand));
        this.chunkService = chunkService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            showHelp(sender, label, subCommands);
            return true;
        }

        SubCommand mainCommand = subCommands.get(args[0]);
        if (mainCommand == null) {
            showHelp(sender, label, subCommands);
            return true;
        }

        if (!mainCommand.hasPermission(sender)) {
            sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>당신은 권한이 없습니다."));
            return true;
        }

        return mainCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    private void showHelp(CommandSender sender, String label, Map<String, SubCommand> commands) {
        sender.sendMessage(MINI_MESSAGE.deserialize(":yellow_q: <#f5f9cc>도움말"));
        commands.values().stream()
                .filter(sub -> sub.hasPermission(sender))
                .forEach(sub -> sender.sendMessage(MINI_MESSAGE.deserialize("- <#f5f9cc>/%label% %name% %usage%"
                        .replace("%label%", label)
                        .replace("%name%", sub.getName())
                        .replace("%usage%", sub.getUsage()))));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    subCommands.keySet().stream()
                            .filter(name -> subCommands.get(name).hasPermission(sender))
                            .toList(),
                    new ArrayList<>());
        }

        SubCommand mainCommand = subCommands.get(args[0].toLowerCase());
        if (mainCommand != null && mainCommand.hasPermission(sender)) {
            return mainCommand.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return Collections.emptyList();
    }
}