package me.msicraft.ctspecialability.PlayerData;

import me.msicraft.ctplayerdata.PlayerData.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerStats {

    private final Player player;
    private final PlayerData playerData;

    private final Set<String> applySpecialAbilities = new HashSet<>();

    public PlayerStats(Player player, PlayerData playerData) {
        this.player = player;
        this.playerData = playerData;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

}
