package me.msicraft.ctspecialability.SpecialAbility.Combat;

import me.msicraft.ctcore.Utils.MathUtil;
import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.PlayerData.PlayerStats;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbilityType;
import me.msicraft.ctspecialability.SpecialAbility.Data.ToolCategory;
import me.msicraft.ctspecialability.SpecialAbility.Data.Trigger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlatLifeSteal extends SpecialAbility {

    private final NamespacedKey KEY = new NamespacedKey(CTSpecialAbility.getPlugin(), "CTSpecialAbility_FlatLifeSteal");

    private double minAmount = 0;
    private double maxAmount = 0;

    public FlatLifeSteal(String internalName) {
        super(Trigger.ATTACK_ENTITY, SpecialAbilityType.COMBAT, internalName, Set.of(ToolCategory.SWORD));
    }

    @Override
    public void updateVariables() {
        FileConfiguration config = CTSpecialAbility.getPlugin().getConfig();
        String path = "SpecialAbility.Combat." + getInternalName();

        setEnabled(config.contains(path + ".Enabled") && config.getBoolean(path + ".Enabled"));
        setCoolDown(config.contains(path + ".CoolDown") ? config.getDouble(path + ".CoolDown") : 1);
        setDisplayName(config.contains(path + ".DisplayName") ? config.getString(path + ".DisplayName") : "Unknown");

        this.minAmount = config.contains(path + ".MinAmount") ? config.getDouble(path + ".MinAmount") : 0;
        this.maxAmount = config.contains(path + ".MaxAmount") ? config.getDouble(path + ".MaxAmount") : 0;
    }

    @Override
    public void execute(EntityDamageByEntityEvent e, EquipmentSlot slot, PlayerStats playerStats) {
        super.execute(e, slot, playerStats);
        Entity attacker = e.getDamager();
        if (attacker instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItem(slot);

            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(KEY)) {
                if (!playerStats.isCoolDown(getInternalName())) {
                    String dataValue = dataContainer.get(KEY, PersistentDataType.STRING);
                    if (dataValue == null) {
                        return;
                    }
                    double value = Double.parseDouble(dataValue);

                    double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double current = player.getHealth();
                    double cal = current + value;
                    if (cal > max) {
                        cal = max;
                    }

                    player.setHealth(cal);

                    playerStats.setCoolDown(getInternalName(), getCoolDown());
                }
            }
        }
    }

    @Override
    public void applySpecialAbilityToItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        double value = Math.round(MathUtil.getRangeRandomDouble(maxAmount, minAmount) * 10) / 10.0;
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(SpecialAbility.KEY, PersistentDataType.STRING, getInternalName());
        dataContainer.set(KEY, PersistentDataType.STRING, String.valueOf(value));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GREEN + "특수능력 (" + getDisplayName() + ChatColor.GREEN
                + "): 공격시 " + ChatColor.AQUA + value + ChatColor.GREEN + " 회복 (" + getCoolDown() + ")"));
        if (itemMeta.hasLore()) {
            PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
            List<Component> original = itemMeta.lore();
            for (Component c : original) {
                String s = plainTextComponentSerializer.serialize(c);
                if (s.contains(ChatColor.GREEN + "특수능력")) {
                    continue;
                }
                lore.add(c);
            }
        }
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);
    }

}
