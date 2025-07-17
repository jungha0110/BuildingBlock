package xyz.jungha.buildingblock;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jungha.buildingblock.command.ManagerCommand;
import xyz.jungha.buildingblock.command.MenuCommand;
import xyz.jungha.buildingblock.command.sub.InfoCommand;
import xyz.jungha.buildingblock.command.sub.RemoveCommand;
import xyz.jungha.buildingblock.command.sub.TeleportCommand;
import xyz.jungha.buildingblock.event.BlockListener;
import xyz.jungha.buildingblock.event.InventoryListener;
import xyz.jungha.buildingblock.event.PlayerInteractListener;
import xyz.jungha.buildingblock.repository.ChunkRepository;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;

public class BuildingBlock extends JavaPlugin {

    @Getter
    private static BuildingBlock instance;

    @Getter
    private ChunkRepository chunkRepo;

    @Getter
    private ChunkService chunkService;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.chunkRepo = new ChunkRepository(this);
        this.chunkService = new ChunkService(this);

        getCommand("건차관리").setExecutor(new ManagerCommand(List.of(
                new InfoCommand(chunkService),
                new RemoveCommand(chunkService),
                new TeleportCommand(chunkService)
        ), chunkService));
        getCommand("건차").setExecutor(new MenuCommand(chunkService));

        registerEvents(
                new BlockListener(chunkService),
                new PlayerInteractListener(chunkService),
                new InventoryListener(chunkService)
        );
    }

    @Override
    public void onDisable() {
        chunkRepo.saveConfig();
        saveConfig();
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
