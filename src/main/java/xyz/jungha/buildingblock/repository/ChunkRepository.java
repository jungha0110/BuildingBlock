package xyz.jungha.buildingblock.repository;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.jungha.buildingblock.BuildingBlock;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class ChunkRepository {

    private final BuildingBlock plugin;
    private File file;
    private FileConfiguration config;

    public ChunkRepository(BuildingBlock plugin) {
        this.plugin = plugin;
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
    }

    public void saveConfig() {
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
        return config.contains(getChunkKey(chunk));
    }

    public void createChunk(Player owner, Chunk chunk) {
        config.set(getChunkKey(chunk) + ".owner", owner.getUniqueId().toString());
        config.set(getChunkKey(chunk) + ".members", Collections.emptyList());
    }

    public void removeChunk(Chunk chunk) {
        config.set(getChunkKey(chunk), null);
    }

    public OfflinePlayer getOwner(Chunk chunk) {
        return config.getOfflinePlayer(getChunkKey(chunk) + ".owner");
    }

    public List<OfflinePlayer> getMember(Chunk chunk) {
        return config.getStringList(getChunkKey(chunk) + ".members").stream()
                .map(uuidStr -> {
                    try {
                        return Bukkit.getOfflinePlayer(UUID.fromString(uuidStr));
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
