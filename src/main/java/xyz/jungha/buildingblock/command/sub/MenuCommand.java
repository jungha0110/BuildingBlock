package xyz.jungha.buildingblock.command.sub;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xyz.jungha.buildingblock.command.SubCommand;
import xyz.jungha.buildingblock.menu.MainMenu;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;

public class MenuCommand implements SubCommand {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final ChunkService chunkService;

    public MenuCommand(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @Override
    public String getName() {
        return "메뉴";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("buildingblock.menu");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sendMessage(sender, "<red>플레이어만 사용할 수 있습니다.");
            return true;
        }

        Inventory menu = MainMenu.getInventory(chunkService);
        if (menu == null) {
            sendMessage(player, "<red>메뉴를 불러오는 데 실패했습니다. (설정 파일 확인)");
            return true;
        }
        player.openInventory(menu);
        return true;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MINI_MESSAGE.deserialize("[<green>건차<white>] " + message));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
