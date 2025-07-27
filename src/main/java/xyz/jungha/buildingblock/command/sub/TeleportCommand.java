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
        if (player == null) return true;

        if (args.length != 2) {
            sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>잘못된 ID 형식입니다."));
            return false;
        }

        try {
            int chunkX = Integer.parseInt(args[0]);
            int chunkZ = Integer.parseInt(args[1]);
            int blockX = (chunkX << 4) + 8;
            int blockZ = (chunkZ << 4) + 8;
            int blockY = player.getWorld().getHighestBlockYAt(blockX, blockZ);
            player.teleport(new org.bukkit.Location(player.getWorld(), blockX + 0.5, blockY, blockZ + 0.5));
            sender.sendMessage(MINI_MESSAGE.deserialize(":green_ex: <#d5f9cc>청크로 이동했습니다."));
        } catch (NumberFormatException e) {
            sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>잘못된 ID 입니다."));
        }
        return true;
    }
}
