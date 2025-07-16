package xyz.jungha.buildingblock.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;

public class MainMenu extends AbstractMenu {

    public static Inventory getInventory(ChunkService chunkService) {
        String title = chunkService.getConfig().getString("menu-title");
        if (title == null) return null;
        Inventory inv = Bukkit.createInventory(null, 27, MINI_MESSAGE.deserialize(title));
        inv.setItem(11, createItem(Material.COMPASS, "<green>멤버", List.of(" ", "<gray>클릭 시 멤버 메뉴창이 열립니다.")));
        inv.setItem(15, createItem(Material.BARRIER, "<red>건차 회수", List.of(" ", "<gray>클릭 시 건차를 회수합니다.")));
        return inv;
    }
}