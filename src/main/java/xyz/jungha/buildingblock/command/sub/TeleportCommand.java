package xyz.jungha.buildingblock.command.sub;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.SubCommand;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;

public class TeleportCommand implements SubCommand {

    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public String getName() {
        return "이동";
    }

    @Override
    public String getUsage() {
        return "[청크ID]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("buildingblock.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>플레이어만 사용할 수 있습니다."));
            return true;
        }
        if (args.length != 1) return false;
        String[] id = args[0].split("-");
        if (id.length != 2) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>잘못된 ID 형식입니다."));
            return true;
        }
        try {
            int chunkX = Integer.parseInt(id[0]);
            int chunkZ = Integer.parseInt(id[1]);
            int blockX = (chunkX << 4) + 8;
            int blockZ = (chunkZ << 4) + 8;
            int blockY = player.getWorld().getHighestBlockYAt(blockX, blockZ);
            player.teleport(new org.bukkit.Location(player.getWorld(), blockX + 0.5, blockY, blockZ + 0.5));
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <aqua>청크로 이동했습니다."));
        } catch (NumberFormatException e) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>잘못된 ID 입니다."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
