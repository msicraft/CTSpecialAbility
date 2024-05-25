package me.msicraft.ctspecialability.PlayerData.Event;

import me.msicraft.ctcore.CustomEvent.PlayerChangeEquipmentEvent;
import me.msicraft.ctplayerdata.PlayerData.CustomEvent.PlayerDataLoadEvent;
import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.PlayerData.PlayerStats;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerRelatedEvent implements Listener {

    private final CTSpecialAbility plugin;

    public PlayerRelatedEvent(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerDataLoad(PlayerDataLoadEvent e) {
        Player player = e.getPlayer();

        plugin.getPlayerStatsManager().addPlayerStats(player.getUniqueId(), new PlayerStats(player, e.getPlayerData()));
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        plugin.getPlayerStatsManager().removePlayerStats(player);
    }

    @EventHandler
    public void changeEquipment(PlayerChangeEquipmentEvent e) {
        Player player = e.getPlayer();
        EquipmentSlot slot = e.getEquipmentSlot();
        PlayerStats playerStats = plugin.getPlayerStatsManager().getPlayerStats(player.getUniqueId());

        playerStats.removeSlot(slot);

        ItemStack afterStack = e.getAfterItemStack();
        if (afterStack != null && afterStack.getType() != Material.AIR) {
            ItemMeta afterMeta = afterStack.getItemMeta();
            if (afterMeta!= null) {
                PersistentDataContainer dataContainer = afterMeta.getPersistentDataContainer();
                if (dataContainer.has(SpecialAbility.KEY)) {
                    String internalName = dataContainer.get(SpecialAbility.KEY, PersistentDataType.STRING);
                    if (internalName != null) {
                        playerStats.setSlotSpecialAbility(slot, internalName);
                    }
                }
            }
        }
    }

}
