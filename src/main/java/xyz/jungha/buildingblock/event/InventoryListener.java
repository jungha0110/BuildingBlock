package xyz.jungha.buildingblock.event;

import com.github.nyaon08.rtustudio.nicknames.NickNames;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.jungha.buildingblock.menu.AddMemberMenu;
import xyz.jungha.buildingblock.menu.MainMenu;
import xyz.jungha.buildingblock.menu.MemberMenu;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.Optional;

public class InventoryListener implements Listener {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public InventoryListener(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = MINI_MESSAGE.serialize(event.getView().title());
        String mainMenuTitle = chunkService.getConfig().getString("menu-title", "");
        String memberMenuTitle = chunkService.getConfig().getString("member-title", "");
        String addMemberMenuTitle = chunkService.getConfig().getString("add-member-title", "");

        if (mainMenuTitle.isEmpty() || memberMenuTitle.isEmpty() || addMemberMenuTitle.isEmpty()) {
            return;
        }

        if (!title.equals(mainMenuTitle) && !title.startsWith(memberMenuTitle) && !title.startsWith(addMemberMenuTitle)) {
            return;
        }

        event.setCancelled(true);

        OfflinePlayer owner = chunkService.getOwner(player.getChunk());
        if (owner == null || !owner.equals(player)) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>당신은 이 건차의 주인이 아닙니다."));
            player.closeInventory();
            return;
        }

        if (title.equals(mainMenuTitle)) {
            handleMainMenuClick(event, player);
        } else if (title.startsWith(memberMenuTitle)) {
            handleMemberMenuClick(event, player);
        } else if (title.startsWith(addMemberMenuTitle)) {
            handleAddMemberMenuClick(event, player);
        }
    }

    private void handleMainMenuClick(InventoryClickEvent event, Player player) {
        Inventory menu = switch (event.getSlot()) {
            case 11 -> MemberMenu.getInventory(chunkService, player.getChunk(), 0);
            case 15 -> MainMenu.getInventory(chunkService);
            default -> null;
        };
        openInventory(player, menu);
    }

    private void handleMemberMenuClick(InventoryClickEvent event, Player player) {
        if (event.getSlot() == 49) {
            openInventory(player, AddMemberMenu.getInventory(chunkService, player.getChunk(), 0));
            return;
        }

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            handleRemoveMemberClick(event, player);
            return;
        }

        handlePageNavigation(event, player, (page) -> MemberMenu.getInventory(chunkService, player.getChunk(), page));
    }

    private void handleAddMemberMenuClick(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            handleAddMemberClick(event, player);
            return;
        }

        handlePageNavigation(event, player, (page) -> AddMemberMenu.getInventory(chunkService, player.getChunk(), page));
    }

    private void handleAddMemberClick(InventoryClickEvent event, Player player) {
        getClickedPlayer(event).ifPresent(clickedPlayer -> {
            String playerName = getPlayerName(clickedPlayer);
            chunkService.addMember(player.getChunk(), clickedPlayer);
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] " + playerName + " 님을 멤버로 추가했습니다."));
            openInventory(player, AddMemberMenu.getInventory(chunkService, player.getChunk(), getCurrentPage(event)));
        });
    }

    private void handleRemoveMemberClick(InventoryClickEvent event, Player player) {
        getClickedPlayer(event).ifPresent(clickedPlayer -> {
            String playerName = getPlayerName(clickedPlayer);
            chunkService.removeMember(player.getChunk(), clickedPlayer);
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] " + playerName + " 님을 멤버에서 제외했습니다."));
            openInventory(player, MemberMenu.getInventory(chunkService, player.getChunk(), getCurrentPage(event)));
        });
    }

    private void handlePageNavigation(InventoryClickEvent event, Player player, PageInventoryProvider provider) {
        int currentPage = getCurrentPage(event);
        int newPage = currentPage;

        if (event.getSlot() == 47) {
            newPage++;
        } else if (event.getSlot() == 51 && currentPage > 0) {
            newPage--;
        }

        if (newPage != currentPage) {
            openInventory(player, provider.getInventory(newPage));
        }
    }

    private Optional<OfflinePlayer> getClickedPlayer(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || !(event.getCurrentItem().getItemMeta() instanceof SkullMeta skullMeta)) {
            return Optional.empty();
        }
        return Optional.ofNullable(skullMeta.getOwningPlayer());
    }

    private int getCurrentPage(InventoryClickEvent event) {
        String title = MINI_MESSAGE.serialize(event.getView().title());
        String[] parts = title.split(" - ");
        if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    private void openInventory(Player player, Inventory inventory) {
        if (inventory == null) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>메뉴를 불러오는 데 실패했습니다."));
            return;
        }
        player.openInventory(inventory);
    }

    @FunctionalInterface
    private interface PageInventoryProvider {
        Inventory getInventory(int page);
    }

    private static String getPlayerName(OfflinePlayer player) {
        try {
            String nickName = NickNames.getInstance().getNickNamesManager().getName(player.getUniqueId());
            return (nickName != null && !nickName.isEmpty()) ? nickName : player.getName();
        } catch (NoClassDefFoundError e) {
            return player.getName();
        }
    }
}
