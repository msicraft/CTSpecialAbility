package me.msicraft.ctspecialability.SpecialAbility.Event;

import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.PlayerData.PlayerStats;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.Trigger;
import me.msicraft.ctspecialability.SpecialAbility.SpecialAbilityManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SpecialAbilityRegisterEvent implements Listener {

    private final CTSpecialAbility plugin;

    public SpecialAbilityRegisterEvent(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void entityDeath(EntityDeathEvent e) {
        Player player = e.getEntity().getKiller();
        if (player != null) {
            PlayerStats playerStats = plugin.getPlayerStatsManager().getPlayerStats(player);
            if (playerStats.hasSpecialAbility()) {
                SpecialAbilityManager specialAbilityManager = plugin.getSpecialAbilityManager();
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    String internalName = playerStats.getSlotSpecialAbility(slot);
                    SpecialAbility specialAbility = specialAbilityManager.getSpecialAbility(internalName);
                    if (specialAbility != null && specialAbility.getTrigger() == Trigger.KILL_ENTITY && specialAbility.isEnabled()) {
                        specialAbility.execute(e, slot, playerStats);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void damagedByEntity(EntityDamageByEntityEvent e) {
        Entity target = e.getDamager();
        if (target instanceof Player player) {
            PlayerStats playerStats = plugin.getPlayerStatsManager().getPlayerStats(player);
            if (playerStats.hasSpecialAbility()) {
                SpecialAbilityManager specialAbilityManager = plugin.getSpecialAbilityManager();
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    String internalName = playerStats.getSlotSpecialAbility(slot);
                    SpecialAbility specialAbility = specialAbilityManager.getSpecialAbility(internalName);
                    if (specialAbility != null && specialAbility.getTrigger() == Trigger.DAMAGED_BY_ENTITY && specialAbility.isEnabled()) {
                        specialAbility.execute(e, slot, playerStats);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void entityAttack(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        if (attacker instanceof Player player) {
            PlayerStats playerStats = plugin.getPlayerStatsManager().getPlayerStats(player);
            if (playerStats.hasSpecialAbility()) {
                SpecialAbilityManager specialAbilityManager = plugin.getSpecialAbilityManager();
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    String internalName = playerStats.getSlotSpecialAbility(slot);
                    SpecialAbility specialAbility = specialAbilityManager.getSpecialAbility(internalName);
                    if (specialAbility != null) {
                        if (specialAbility.getTrigger() == Trigger.ATTACK_ENTITY && specialAbility.isEnabled()) {
                            specialAbility.execute(e, slot, playerStats);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PlayerStats playerStats = plugin.getPlayerStatsManager().getPlayerStats(player);
        if (playerStats.hasSpecialAbility()) {
            SpecialAbilityManager specialAbilityManager = plugin.getSpecialAbilityManager();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                String internalName = playerStats.getSlotSpecialAbility(slot);
                SpecialAbility specialAbility = specialAbilityManager.getSpecialAbility(internalName);
                if (specialAbility != null && specialAbility.getTrigger() == Trigger.BLOCK_BREAK && specialAbility.isEnabled()) {
                    specialAbility.execute(e, slot, playerStats);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void harvestBlock(PlayerHarvestBlockEvent e) {
        Player player = e.getPlayer();
        PlayerStats playerStats = plugin.getPlayerStatsManager().getPlayerStats(player);
        if (playerStats.hasSpecialAbility()) {
            SpecialAbilityManager specialAbilityManager = plugin.getSpecialAbilityManager();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                String internalName = playerStats.getSlotSpecialAbility(slot);
                SpecialAbility specialAbility = specialAbilityManager.getSpecialAbility(internalName);
                if (specialAbility!= null && specialAbility.getTrigger() == Trigger.HARVEST && specialAbility.isEnabled()) {
                    specialAbility.execute(e, slot, playerStats);
                    return;
                }
            }
        }
    }

}
