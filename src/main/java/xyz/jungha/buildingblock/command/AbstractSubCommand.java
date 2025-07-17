package xyz.jungha.buildingblock.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSubCommand implements SubCommand {

    protected static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    protected final ChunkService chunkService;
    private final String name;
    private final String usage;
    private final String permission;

    public AbstractSubCommand(ChunkService chunkService, String name, String usage, String permission) {
        this.chunkService = chunkService;
        this.name = name;
        this.usage = usage;
        this.permission = permission;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    protected void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] " + message));
    }

    protected Player getPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, "<red>플레이어만 사용할 수 있습니다.");
            return null;
        }
        return (Player) sender;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
