package xyz.jungha.buildingblock.command.sub;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.SubCommand;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;

public class RemoveCommand implements SubCommand {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public RemoveCommand(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @Override
    public String getName() {
        return "제거";
    }

    @Override
    public String getUsage() {
        return "[청크ID]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("buildingblock.remove");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sendMessage(sender, "<red>플레이어만 사용할 수 있습니다.");
            return true;
        }
        Chunk targetChunk;
        if (args.length == 1) {
            String[] id = args[0].split("-");
            if (id.length != 2) {
                sendMessage(player, "<red>잘못된 ID 형식입니다. (예: X-Z)");
                return true;
            }
            try {
                targetChunk = player.getWorld().getChunkAt(Integer.parseInt(id[0]), Integer.parseInt(id[1]));
            } catch (NumberFormatException e) {
                sendMessage(player, "<red>잘못된 ID입니다.");
                return true;
            }
        } else if (args.length == 0) {
            targetChunk = player.getChunk();
        } else {
            sendMessage(player, "<red>사용법: /건차 제거 [청크ID] 또는 /건차 제거");
            return true;
        }

        if (!chunkService.hasChunkData(targetChunk)) {
            sendMessage(player, "<red>주인 없는 건차입니다.");
            return true;
        }

        chunkService.removeChunk(targetChunk);
        sendMessage(player, "해당 건차를 제거했습니다.");
        return true;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] " + message));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
