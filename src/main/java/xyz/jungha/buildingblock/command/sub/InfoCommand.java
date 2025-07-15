package xyz.jungha.buildingblock.command.sub;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.SubCommand;
import xyz.jungha.buildingblock.repository.ChunkRepository;

import java.util.List;
import java.util.stream.Collectors;

public class InfoCommand implements SubCommand {

    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkRepository chunkRepository;

    public InfoCommand(ChunkRepository chunkRepository) {
        this.chunkRepository = chunkRepository;
    }
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
            sender.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>플레이어만 사용할 수 있습니다."));
            return true;
        }
        Chunk chunk = player.getLocation().getChunk();
        if (chunkRepository.contains(chunk)) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] " + chunk.getChunkKey()));
            player.sendMessage(MINI_MESSAGE.deserialize("ㄴ 주인 : " + chunkRepository.getOwner(chunk).getName()));
            String members = chunkRepository.getMember(chunk).stream()
                    .map(member -> member.getName() != null ? member.getName() : member.getUniqueId().toString())
                    .collect(Collectors.joining(", "));
            player.sendMessage(MINI_MESSAGE.deserialize("ㄴ 멤버 : " + (members.isEmpty() ? "없음" : members)));
        } else {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] 주인 없는 구역 입니다"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
