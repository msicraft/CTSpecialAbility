package me.msicraft.ctspecialability.PlayerData.Event;

import me.msicraft.ctcore.CustomEvent.PlayerChangeEquipmentEvent;
import me.msicraft.ctspecialability.CTSpecialAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRelatedEvent implements Listener {

    private final CTSpecialAbility plugin;

    public PlayerRelatedEvent(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void changeEquipment(PlayerChangeEquipmentEvent e) {
        Player player = e.getPlayer();
    }

}
