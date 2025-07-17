package xyz.jungha.buildingblock.service;

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
        chunkRepository.removeChunk(chunk);
    }

    public OfflinePlayer getOwner(Chunk chunk) {
        return chunkRepository.getOwner(chunk);
    }

    public List<OfflinePlayer> getMembers(Chunk chunk) {
        return chunkRepository.getMembers(chunk);
    }

    public boolean isMember(Chunk chunk, OfflinePlayer player) {
        return chunkRepository.isMember(chunk, player);
    }

    public void addMember(Chunk chunk, OfflinePlayer member) {
        chunkRepository.addMember(chunk, member);
    }

    public void removeMember(Chunk chunk, OfflinePlayer member) {
        chunkRepository.removeMember(chunk, member);
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void saveChunkData() {
        chunkRepository.saveConfig();
    }
}
