package me.msicraft.ctspecialability.SpecialAbility.Data;

import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.PlayerData.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public abstract class SpecialAbility {

    public static final NamespacedKey KEY = new NamespacedKey(CTSpecialAbility.getPlugin(), "CTSpecialAbility");
    private final NamespacedKey key;

    private boolean isEnabled = false;
    private final String internalName;
    private double coolDown;
    private String displayName;

    private final SpecialAbilityType specialAbilityType;
    private final Trigger trigger;
    private final Set<ToolCategory> allowsTools;

    public SpecialAbility(NamespacedKey key, Trigger trigger, SpecialAbilityType specialAbilityType, String internalName, Set<ToolCategory> allowsTools) {
        this.key = key;
        this.trigger = trigger;
        this.specialAbilityType = specialAbilityType;
        this.internalName = internalName;
        this.allowsTools = allowsTools;

        updateVariables();
    }

    public abstract void updateVariables();
    public abstract void applySpecialAbilityToItemStack(ItemStack itemStack);

    public void execute(EquipmentSlot slot, PlayerStats playerStats) {}
    public void execute(EntityDeathEvent e, EquipmentSlot slot, PlayerStats playerStats) {}
    public void execute(EntityDamageByEntityEvent e, EquipmentSlot slot, PlayerStats playerStats) {}
    public void execute(BlockBreakEvent e, EquipmentSlot slot, PlayerStats playerStats) {}
    public void execute(PlayerHarvestBlockEvent e, EquipmentSlot slot, PlayerStats playerStats) {}

    public boolean isAllowTool(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        return isAllowTool(itemStack.getType());
    }

    public boolean isAllowTool(Material material) {
        if (allowsTools.contains(ToolCategory.ALL)) {
            return true;
        }
        for (ToolCategory toolCategory : allowsTools) {
            if (toolCategory.getMaterials().contains(material)) {
                return true;
            }
        }
        return false;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public void setCoolDown(double coolDown) {
        this.coolDown = coolDown;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public SpecialAbilityType getSpecialAbilityType() {
        return specialAbilityType;
    }

    public String getInternalName() {
        return internalName;
    }

    public double getCoolDown() {
        return coolDown;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
