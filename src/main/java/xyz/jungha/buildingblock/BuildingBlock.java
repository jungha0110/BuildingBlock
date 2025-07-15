package xyz.jungha.buildingblock;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jungha.buildingblock.command.ManagerCommand;
import xyz.jungha.buildingblock.command.sub.InfoCommand;
import xyz.jungha.buildingblock.command.sub.RemoveCommand;
import xyz.jungha.buildingblock.service.PluginServices;

import java.util.List;

public class BuildingBlock extends JavaPlugin {

    @Getter
    private static BuildingBlock instance;

    @Getter
    private PluginServices pluginServices;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginServices = new PluginServices(this);

        getCommand("건차").setExecutor(new ManagerCommand(List.of(
                new InfoCommand(pluginServices.getChunkRepository()),
                new RemoveCommand(pluginServices.getChunkRepository())
        )));
    }

    @Override
    public void onDisable() {
        pluginServices.getChunkRepository().saveConfig();
    }
}