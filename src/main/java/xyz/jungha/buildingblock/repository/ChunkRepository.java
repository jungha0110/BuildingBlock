package xyz.jungha.buildingblock.repository;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.BuildingBlock;
import xyz.jungha.buildingblock.data.ChunkData;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ChunkRepository {

    private final BuildingBlock plugin;
    private File file;
    private FileConfiguration config;
    private final Map<String, ChunkData> chunkDataMap;

    public ChunkRepository(BuildingBlock plugin) {
        this.plugin = plugin;
        this.chunkDataMap = new HashMap<>();
        loadConfig();
    }

    public void loadConfig() {
        file = new File(plugin.getDataFolder(), "chunk.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load or create chunk.yml", e);
        }
        config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            String ownerUuidStr = config.getString(key + ".owner");
            List<String> memberUuidStrs = config.getStringList(key + ".members");

            if (ownerUuidStr != null) {
                try {
                    UUID ownerUuid = UUID.fromString(ownerUuidStr);
                    List<UUID> memberUuids = memberUuidStrs.stream()
                            .map(UUID::fromString)
                            .collect(Collectors.toList());
                    chunkDataMap.put(key, new ChunkData(ownerUuid, memberUuids));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().log(Level.WARNING, "Invalid UUID found in chunk.yml for key: " + key, e);
                }
            }
        }
    }

    public void saveConfig() {
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }

        for (Map.Entry<String, ChunkData> entry : chunkDataMap.entrySet()) {
            String key = entry.getKey();
            ChunkData data = entry.getValue();
            config.set(key + ".owner", data.getOwnerUuid().toString());
            config.set(key + ".members", data.getMemberUuids().stream()
                    .map(UUID::toString)
                    .collect(Collectors.toList()));
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save chunk.yml", e);
        }
    }

    private String getChunkKey(Chunk chunk) {
        return chunk.getX() + "-" + chunk.getZ();
    }

    public boolean contains(Chunk chunk) {
        return chunkDataMap.containsKey(getChunkKey(chunk));
    }

    public void createChunk(Player owner, Chunk chunk) {
        String key = getChunkKey(chunk);
        if (!chunkDataMap.containsKey(key)) {
            chunkDataMap.put(key, new ChunkData(owner.getUniqueId()));
            saveConfig();
        }
    }

    public void removeChunk(Chunk chunk) {
        String key = getChunkKey(chunk);
        if (chunkDataMap.containsKey(key)) {
            chunkDataMap.remove(key);
            saveConfig();
        }
    }

    public OfflinePlayer getOwner(Chunk chunk) {
        String key = getChunkKey(chunk);
        ChunkData data = chunkDataMap.get(key);
        return (data != null) ? Bukkit.getOfflinePlayer(data.getOwnerUuid()) : null;
    }

    public List<OfflinePlayer> getMembers(Chunk chunk) {
        String key = getChunkKey(chunk);
        ChunkData data = chunkDataMap.get(key);
        if (data == null) return Collections.emptyList();
        return data.getMemberUuids().stream()
                .map(Bukkit::getOfflinePlayer)
                .collect(Collectors.toList());
    }

    public void addMember(Chunk chunk, OfflinePlayer player) {
        String key = getChunkKey(chunk);
        ChunkData data = chunkDataMap.get(key);
        if (data != null) {
            data.addMember(player.getUniqueId());
            saveConfig();
        }
    }

    public void removeMember(Chunk chunk, OfflinePlayer player) {
        String key = getChunkKey(chunk);
        ChunkData data = chunkDataMap.get(key);
        if (data != null) {
            data.removeMember(player.getUniqueId());
            saveConfig();
        }
    }
}
