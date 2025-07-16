package xyz.jungha.buildingblock.event;

import com.nexomc.nexo.api.NexoItems;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
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
            ArmorStand armorStand =  spawnCore(player.getLocation());
            player.sendMessage("§a구역이 생성되었습니다.");
            
            decreaseItemInHand(player);
        } else {
            player.sendMessage("§c이미 구역이 존재합니다.");
        }
    }

    private boolean isValidChunkItem(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !event.getAction().isRightClick()) return false;
        String itemId = NexoItems.idFromItem(item);
        String configItemId = chunkService.getConfig().getString("chunk-item");
        return itemId != null && itemId.equals(configItemId);
    }

    private void decreaseItemInHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        int amount = item.getAmount();
        item.setAmount(Math.max(0, amount - 1));
    }

    private ArmorStand spawnCore(Location location) {
        return location.getWorld().spawn(getChunkCenter(location.getChunk()), ArmorStand.class, armorStand -> {
            armorStand.customName(MINI_MESSAGE.deserialize("<blue>건차 코어"));
            armorStand.setVisible(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.setBasePlate(false);
            armorStand.setCanPickupItems(false);
        });
    }

    private Location getChunkCenter(Chunk chunk) {
        World world = chunk.getWorld();
        int centerX = (chunk.getX() << 4) + 8;
        int centerZ = (chunk.getZ() << 4) + 8;
        int centerY = world.getHighestBlockYAt(centerX, centerZ);
        return new Location(world, centerX + 0.5, centerY, centerZ + 0.5);
    }
}