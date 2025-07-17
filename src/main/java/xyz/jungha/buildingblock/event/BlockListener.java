package xyz.jungha.buildingblock.event;

import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.jungha.buildingblock.service.ChunkService;

public class BlockListener implements Listener {

    private final ChunkService chunkService;

    public BlockListener(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (shouldCancel(event.getPlayer(), event.getBlock().getChunk()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (shouldCancel(event.getPlayer(), event.getBlock().getChunk()))
            event.setCancelled(true);
    }

    private boolean shouldCancel(Player player, Chunk chunk) {
        if (!chunkService.hasChunkData(chunk)) return false;
        if (player.isOp()) return false;

        OfflinePlayer owner = chunkService.getOwner(chunk);
        if (player.equals(owner)) return false;

        return !chunkService.isMember(chunk, player);
    }
}