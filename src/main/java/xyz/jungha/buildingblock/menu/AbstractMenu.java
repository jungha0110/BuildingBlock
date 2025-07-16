package xyz.jungha.buildingblock.menu;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMenu {

    protected static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    protected static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(MINI_MESSAGE.deserialize(name));
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(MINI_MESSAGE::deserialize).collect(Collectors.toList()));
            }
        });
        return item;
    }
}