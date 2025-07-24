package xyz.jungha.buildingblock.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import xyz.jungha.buildingblock.menu.MainMenu;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.*;

public class MenuCommand implements TabExecutor {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public MenuCommand(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[<green>건차<white>] <red>플레이어만 사용할 수 있습니다.");
            return true;
        }
        Inventory mainMenu = MainMenu.getInventory(chunkService);
        if (mainMenu == null) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>메뉴를 불러오는 데 실패했습니다."));
            return true;
        }
        OfflinePlayer owner = chunkService.getOwner(player.getChunk());
        if (owner == null) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>이 건차는 주인 없는 건차입니다."));
            return true;
        }
        if (!owner.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] <red>당신은 이 건차의 주인이 아닙니다."));
            return true;
        }
        player.openInventory(mainMenu);
        player.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] 메뉴를 열었습니다."));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
