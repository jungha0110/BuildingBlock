package xyz.jungha.buildingblock.command.sub;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.SubCommand;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InfoCommand implements SubCommand {

    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public InfoCommand(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @Override
    public String getName() {
        return "정보";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("buildingblock.info");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sendMessage(sender, "<red>플레이어만 사용할 수 있습니다.");
            return true;
        }

        Chunk chunk = player.getLocation().getChunk();
        if (!chunkService.hasChunkData(chunk)) {
            sendMessage(player, "주인 없는 구역 입니다");
            return true;
        }

        sendMessage(player, chunk.getX() + "-" + chunk.getZ());

        OfflinePlayer owner = chunkService.getOwner(chunk);
        sendMessage(player, "ㄴ 주인 : " + (owner != null ? owner.getName() : "없음"));

        String members = chunkService.getMembers(chunk).stream()
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        sendMessage(player, "ㄴ 멤버 : " + (members.isEmpty() ? "없음" : members));

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