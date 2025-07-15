package xyz.jungha.buildingblock.service;

import lombok.Getter;
import xyz.jungha.buildingblock.BuildingBlock;
import xyz.jungha.buildingblock.repository.ChunkRepository;

public class PluginServices {

    @Getter
    private final ChunkRepository chunkRepository;

    public PluginServices(BuildingBlock plugin) {
        this.chunkRepository = new ChunkRepository(plugin);
    }
}
