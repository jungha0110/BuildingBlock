package xyz.jungha.buildingblock.menu;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.ArrayList;
import java.util.List;

public class MemberMenu extends AbstractMenu {

    public static Inventory getInventory(ChunkService chunkService, Chunk chunk, int page) {
        String title = chunkService.getConfig().getString("member-title");
        if (title == null) return null;
        Inventory inv = Bukkit.createInventory(null, 54, MINI_MESSAGE.deserialize(title + " - " + page));

        List<OfflinePlayer> members = new ArrayList<>(chunkService.getMembers(chunk));

        int playersPerPage = 45;
        int startIndex = page * playersPerPage;
        int endIndex = Math.min(startIndex + playersPerPage, members.size());

        for (int i = startIndex; i < endIndex; i++) {
            OfflinePlayer player = members.get(i);
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            String playerName = getPlayerName(player);
            playerHead.editMeta(SkullMeta.class, meta -> {
                meta.setOwningPlayer(player);
                meta.displayName(MINI_MESSAGE.deserialize("<white>" + playerName));
                meta.lore(List.of(
                        Component.empty(),
                        MINI_MESSAGE.deserialize("<gray>클릭하여 이 멤버를 제외합니다.")
                ));
            });
            inv.addItem(playerHead);
        }

        inv.setItem(49, createItem(Material.PAPER, "멤버 추가", List.of(" ", "<gray>클릭 시 멤버 추가 메뉴창이 열립니다.")));

        if (page > 0) {
            inv.setItem(47, createItem(Material.PAPER, "<red>이전 페이지", null));
        }

        if (endIndex < members.size()) {
            inv.setItem(51, createItem(Material.PAPER, "<green>다음 페이지", null));
        }
        return inv;
    }

    private static String getPlayerName(OfflinePlayer player) {
        String name = PlaceholderAPI.setPlaceholders(player, "%names_display%");
        return (name.isEmpty() ? player.getName() : name);
    }
}