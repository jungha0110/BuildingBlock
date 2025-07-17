package xyz.jungha.buildingblock.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.AbstractSubCommand;
import xyz.jungha.buildingblock.service.ChunkService;

public class TeleportCommand extends AbstractSubCommand {

    public TeleportCommand(ChunkService chunkService) {
        super(chunkService, "이동", "[청크ID]", "buildingblock.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = getPlayer(sender);
        if (player == null) {
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        String[] id = args[0].split("-");
        if (id.length != 2) {
            sendMessage(player, "<red>잘못된 ID 형식입니다.");
            return true;
        }

        try {
            int chunkX = Integer.parseInt(id[0]);
            int chunkZ = Integer.parseInt(id[1]);
            int blockX = (chunkX << 4) + 8;
            int blockZ = (chunkZ << 4) + 8;
            int blockY = player.getWorld().getHighestBlockYAt(blockX, blockZ);
            player.teleport(new org.bukkit.Location(player.getWorld(), blockX + 0.5, blockY, blockZ + 0.5));
            sendMessage(player, "<aqua>청크로 이동했습니다.");
        } catch (NumberFormatException e) {
            sendMessage(player, "<red>잘못된 ID 입니다.");
        }
        return true;
    }
}
