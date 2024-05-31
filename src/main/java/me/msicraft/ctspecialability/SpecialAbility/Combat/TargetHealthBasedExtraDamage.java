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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

public class TargetHealthBasedExtraDamage extends SpecialAbility {

    public TargetHealthBasedExtraDamage(String internalName) {
        super(new NamespacedKey(CTSpecialAbility.getPlugin(),"CTSpecialAbility_TargetHealthBasedExtraDamage"),
               Trigger.ATTACK_ENTITY, SpecialAbilityType.COMBAT, internalName, Set.of(ToolCategory.SWORD));
    }

    private double minHealthRate = 1;
    private double maxHealthRate = 1;

    private double minExtraDamageRate = 0;
    private double maxExtraDamageRate = 0;

    @Override
    public void updateVariables() {
        FileConfiguration config = CTSpecialAbility.getPlugin().getConfig();
        String path = "SpecialAbility.Combat." + getInternalName();

        setEnabled(config.contains(path + ".Enabled") && config.getBoolean(path + ".Enabled"));
        setCoolDown(config.contains(path + ".CoolDown") ? config.getDouble(path + ".CoolDown") : 1);
        setDisplayName(config.contains(path + ".DisplayName") ? config.getString(path + ".DisplayName") : "Unknown");

        this.minHealthRate = config.contains(path + ".MinHealthRate") ? config.getDouble(path + ".MinHealthRate") : 1;
        this.maxHealthRate = config.contains(path + ".MaxHealthRate") ? config.getDouble(path + ".MaxHealthRate") : 1;

        this.minExtraDamageRate = config.contains(path + ".MinExtraDamageRate")? config.getDouble(path + ".MinExtraDamageRate") : 0;
        this.maxExtraDamageRate = config.contains(path + ".MaxExtraDamageRate")? config.getDouble(path + ".MaxExtraDamageRate") : 0;
    }

    @Override
    public void execute(EntityDamageByEntityEvent e, EquipmentSlot slot, PlayerStats playerStats) {
        super.execute(e, slot, playerStats);
        Entity attacker = e.getDamager();
        if (attacker instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItem(slot);

            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(getKey())) {
                if (!playerStats.isCoolDown(getInternalName())) {
                    String dataValue = dataContainer.get(getKey(), PersistentDataType.STRING);
                    if (dataValue == null) {
                        return;
                    }
                    String[] a = dataValue.split(":");
                    double targetHealthRate = Double.parseDouble(a[0]);
                    Entity target = e.getEntity();
                    if (target instanceof LivingEntity livingEntity) {
                        double healthRate = livingEntity.getHealth() / livingEntity.getMaxHealth();
                        if (healthRate >= targetHealthRate) {
                            double extraDamageRate = Double.parseDouble(a[1]);

                            double originalDamage = e.getDamage();
                            double cal = originalDamage + (originalDamage * extraDamageRate);

                            e.setDamage(cal);

                            playerStats.setCoolDown(getInternalName(), getCoolDown());
                        }
                    }
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
        double targetHealthRate = (MathUtil.getRangeRandomDouble(maxHealthRate,  minHealthRate) * 10.0) / 10.0;
        double extraDamageRate = (MathUtil.getRangeRandomDouble(maxExtraDamageRate,  minExtraDamageRate) * 10.0) / 10.0;
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(SpecialAbility.KEY, PersistentDataType.STRING, getInternalName());
        dataContainer.set(getKey(), PersistentDataType.STRING, targetHealthRate + ":" + extraDamageRate);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GREEN + "특수능력 (" + getDisplayName() + ChatColor.GREEN
                + "): 타켓의 체력이 " + ChatColor.AQUA + (targetHealthRate * 100) + ChatColor.GREEN + "% 이상이라면 " + ChatColor.AQUA
                + extraDamageRate + "% " + ChatColor.GREEN + " 추가 데미지"));
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
