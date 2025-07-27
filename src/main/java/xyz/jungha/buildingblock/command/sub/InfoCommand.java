package xyz.jungha.buildingblock.command.sub;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.AbstractSubCommand;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.Objects;
import java.util.stream.Collectors;

public class InfoCommand extends AbstractSubCommand {

    public InfoCommand(ChunkService chunkService) {
        super(chunkService, "정보", "", "buildingblock.info");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = getPlayer(sender);
        if (player == null) {
            return true;
        }

        Chunk chunk = player.getLocation().getChunk();
        if (!chunkService.hasChunkData(chunk)) {
            sendMessage(player, "주인 없는 구역 입니다");
            return true;
        }

        sendMessage(player, "아이디 : " + chunk.getX() + "-" + chunk.getZ());

        OfflinePlayer owner = chunkService.getOwner(chunk);
        String ownerName = "없음";
        if (owner != null) {
            String name = PlaceholderAPI.setPlaceholders(owner, "%names_display%");
            ownerName = owner.getName() + " / " + (name.isEmpty() ? "없음" : name);
        }
        sendMessage(player, "ㄴ 주인 : " + ownerName);

        String members = chunkService.getMembers(chunk).stream()
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        sendMessage(player, "ㄴ 멤버 : " + (members.isEmpty() ? "없음" : members));

        return true;
    }
}