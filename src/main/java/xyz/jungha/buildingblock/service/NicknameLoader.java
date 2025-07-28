package xyz.jungha.buildingblock.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class NicknameLoader {

    public static class NicknameEntry {
        public String uuid;
        public String name;
    }

    private static final Map<OfflinePlayer, String> nicknameMap = new HashMap<>();

    public static void loadNicknameJson() {
        File file = new File("plugins/Names/Data/Nickname.json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<NicknameEntry>>() {}.getType();
            List<NicknameEntry> entries = gson.fromJson(reader, listType);

            nicknameMap.clear();
            for (NicknameEntry entry : entries) {
                try {
                    UUID uuid = UUID.fromString(entry.uuid);
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    nicknameMap.put(player, entry.name);
                } catch (IllegalArgumentException ignored) {}
            }
        } catch (Exception ignored) {}
    }

    public static String getNickname(OfflinePlayer player) {
        return nicknameMap.getOrDefault(player, "");
    }
}

