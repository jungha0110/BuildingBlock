package xyz.jungha.buildingblock.event;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.jungha.buildingblock.service.ChunkService;

public class PlayerInteractListener implements Listener {

    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public PlayerInteractListener(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isValidChunkItem(event)) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (chunkService.createChunk(player, player.getLocation().getChunk())) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] 건차가 생성되었습니다."));
            decreaseItemInHand(player);
        } else {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>이미 건차가 존재합니다."));
        }
    }

    private boolean isValidChunkItem(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !event.getAction().isRightClick()) return false;
        return item.getType().equals(Material.BOOK);
    }

    private void decreaseItemInHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        int amount = item.getAmount();
        item.setAmount(Math.max(0, amount - 1));
    }
}