package xyz.jungha.buildingblock.command.sub;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.command.AbstractSubCommand;
import xyz.jungha.buildingblock.service.ChunkService;

public class RemoveCommand extends AbstractSubCommand {

    public RemoveCommand(ChunkService chunkService) {
        super(chunkService, "제거", "[청크ID]", "buildingblock.remove");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = getPlayer(sender);
        if (player == null) {
            return true;
        }

        Chunk targetChunk;
        if (args.length == 1) {
            String[] id = args[0].split("-");
            if (id.length != 2) {
                sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>잘못된 ID 형식입니다. <red>(예: X-Z)"));
                return true;
            }
            try {
                targetChunk = player.getWorld().getChunkAt(Integer.parseInt(id[0]), Integer.parseInt(id[1]));
            } catch (NumberFormatException e) {
                sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>잘못된 ID입니다."));
                return true;
            }
        } else if (args.length == 0) {
            targetChunk = player.getChunk();
        } else {
            sender.sendMessage(MINI_MESSAGE.deserialize(":yellow_q: <#f5f9cc>/건차 제거 [청크ID] 또는 /건차 제거"));
            return true;
        }

        if (!chunkService.hasChunkData(targetChunk)) {
            sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>주인 없는 건차입니다."));
            return true;
        }

        chunkService.removeChunk(targetChunk);
        sender.sendMessage(MINI_MESSAGE.deserialize(":green_ex: <#d5f9cc>해당 건차를 <green>제거<#d5f9cc>했습니다."));
        return true;
    }
}
