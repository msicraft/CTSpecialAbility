package me.msicraft.ctspecialability.PlayerData;

import me.msicraft.ctspecialability.CTSpecialAbility;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatsManager {

    private final CTSpecialAbility plugin;

    public PlayerStatsManager(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, PlayerStats> statsMap = new HashMap<>();

    public void addPlayerStats(UUID uuid, PlayerStats playerstats) {
        statsMap.put(uuid, playerstats);
    }

    public void removePlayerStats(Player player) {
        removePlayerStats(player.getUniqueId());
    }

    public void removePlayerStats(UUID uuid) {
        statsMap.remove(uuid);
    }

    public PlayerStats getPlayerStats(Player player) {
        return getPlayerStats(player.getUniqueId());
    }

    public PlayerStats getPlayerStats(UUID uuid) {
        return statsMap.get(uuid);
    }

}
