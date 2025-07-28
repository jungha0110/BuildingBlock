package xyz.jungha.buildingblock.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.jungha.buildingblock.service.ChunkService;
import xyz.jungha.buildingblock.service.NicknameLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddMemberMenu extends AbstractMenu {

    public static Inventory getInventory(ChunkService chunkService, Chunk chunk, int page) {
        String title = chunkService.getConfig().getString("add-member-title");
        if (title == null) return null;
        Inventory inv = Bukkit.createInventory(null, 54, MINI_MESSAGE.deserialize(title + " - " + page));

        Set<UUID> memberUuids = chunkService.getMembers(chunk).stream()
                .map(OfflinePlayer::getUniqueId)
                .collect(Collectors.toSet());
        UUID ownerUuid = chunkService.getOwner(chunk).getUniqueId();

        List<OfflinePlayer> playersToAdd = Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(player -> !memberUuids.contains(player.getUniqueId()) && !player.getUniqueId().equals(ownerUuid))
                .toList();

        int playersPerPage = 45;
        int startIndex = page * playersPerPage;
        int endIndex = Math.min(startIndex + playersPerPage, playersToAdd.size());

        for (int i = startIndex; i < endIndex; i++) {
            OfflinePlayer player = playersToAdd.get(i);
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            String playerName = getPlayerName(player);

            playerHead.editMeta(SkullMeta.class, meta -> {
                meta.setOwningPlayer(player);
                meta.displayName(MINI_MESSAGE.deserialize("<white>" + playerName));
                meta.lore(List.of(
                        Component.empty(),
                        MINI_MESSAGE.deserialize("<white>:left_m: <gray>클릭하여 멤버로 추가합니다.")
                ));
            });
            inv.addItem(playerHead);
        }

        if (page > 0) {
            inv.setItem(47, createItem(Material.PAPER, "<red>이전 페이지", null));
        }

        if (endIndex < playersToAdd.size()) {
            inv.setItem(51, createItem(Material.PAPER, "<green>다음 페이지", null));
        }
        return inv;
    }

    private static String getPlayerName(OfflinePlayer player) {
        String name = NicknameLoader.getNickname(player);
        return (name.isEmpty() ? player.getName() : name);
    }
}