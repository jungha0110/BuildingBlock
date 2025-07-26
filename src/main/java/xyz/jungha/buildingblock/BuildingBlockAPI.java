package xyz.jungha.buildingblock;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import xyz.jungha.buildingblock.service.ChunkService;

import java.util.List;

public final class BuildingBlockAPI {

    private static BuildingBlock plugin;

    private static BuildingBlock plugin() {
        if (plugin == null) plugin = BuildingBlock.getInstance();
        return plugin;
    }

    /**
     * 건차의 주인을 가져옵니다.
     * @param chunk 청크
     * @return 플레이어
     */
    public static OfflinePlayer getOwner(Chunk chunk) {
        return plugin().getChunkService().getOwner(chunk);
    }

    /**
     * 건차의 멤버를 가져옵니다.
     * @param chunk 청크
     * @return 멤버 플레이어 목록
     */
    public static List<OfflinePlayer> getMembers(Chunk chunk) {
        return plugin().getChunkService().getMembers(chunk);
    }

    /**
     * 건차에 멤버를 추가합니다.
     * @param chunk 청크
     * @param member 멤버 플레이어
     */
    public static void addMember(Chunk chunk, OfflinePlayer member) {
        plugin().getChunkService().addMember(chunk, member);
    }

    /**
     * 건차에서 멤버를 제거합니다.
     * @param chunk 청크
     * @param member 멤버 플레이어
     */
    public static void removeMember(Chunk chunk, OfflinePlayer member) {
        plugin().getChunkService().removeMember(chunk, member);
    }
}
