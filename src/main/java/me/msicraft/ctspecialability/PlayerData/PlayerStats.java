package me.msicraft.ctspecialability.PlayerData;

import me.msicraft.ctplayerdata.PlayerData.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats {

    private final Player player;
    private final PlayerData playerData;

    private final Map<EquipmentSlot, String> applySpeciailAbilityMap = new HashMap<>();

    private final Map<String, Long> lastUseTimeMap = new HashMap<>();

    public PlayerStats(Player player, PlayerData playerData) {
        this.player = player;
        this.playerData = playerData;
    }

    public boolean isCoolDown(String internalName) {
        if (lastUseTimeMap.containsKey(internalName)) {
            return lastUseTimeMap.get(internalName) > System.currentTimeMillis();
        }
        return false;
    }

    public void setCoolDown(String internalName, double seconds) {
        lastUseTimeMap.put(internalName, (long) (System.currentTimeMillis() + (seconds * 1000)));
    }

    public boolean hasSpecialAbility() {
        return !applySpeciailAbilityMap.isEmpty();
    }

    public void setSlotSpecialAbility(EquipmentSlot slot, String internalName) {
        applySpeciailAbilityMap.put(slot, internalName);
    }

    public String getSlotSpecialAbility(EquipmentSlot slot) {
        return applySpeciailAbilityMap.get(slot);
    }

    public void removeSlot(EquipmentSlot slot) {
        applySpeciailAbilityMap.remove(slot);
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

}
