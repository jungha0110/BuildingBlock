package xyz.jungha.buildingblock.service;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.BuildingBlock;
import xyz.jungha.buildingblock.repository.ChunkRepository;

import java.util.List;

public class ChunkService {

    private final BuildingBlock plugin;
    private final ChunkRepository chunkRepository;

    public ChunkService(BuildingBlock plugin) {
        this.plugin = plugin;
        this.chunkRepository = plugin.getChunkRepo();
    }

    public boolean hasChunkData(Chunk chunk) {
        return chunkRepository.contains(chunk);
    }

    public boolean createChunk(Player player, Chunk chunk) {
        if (hasChunkData(chunk)) return false;
        chunkRepository.createChunk(player, chunk);
        return true;
    }

    public void removeChunk(Chunk chunk) {
        if (!hasChunkData(chunk)) return;
        chunkRepository.removeChunk(chunk);
    }

    public OfflinePlayer getOwner(Chunk chunk) {
        if (!hasChunkData(chunk)) return null;
        return chunkRepository.getOwner(chunk);
    }

    public List<OfflinePlayer> getMembers(Chunk chunk) {
        if (!hasChunkData(chunk)) return null;
        return chunkRepository.getMembers(chunk);
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }
}
