package me.msicraft.ctspecialability.SpecialAbility.Event;

import me.msicraft.ctcore.Utils.MathUtil;
import me.msicraft.ctspecialability.CTSpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbility;
import me.msicraft.ctspecialability.SpecialAbility.Data.SpecialAbilityType;
import me.msicraft.ctspecialability.SpecialAbility.SpecialAbilityManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SpecialAbilityApplyEvent implements Listener {

    private final CTSpecialAbility plugin;

    public SpecialAbilityApplyEvent(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        ItemStack cursor = e.getCursor();

        if (cursor == null || cursor.getType() == Material.AIR) {
            return;
        }
        SpecialAbilityManager specialAbilityManager = plugin.getSpecialAbilityManager();
        ItemMeta cursorMeta = cursor.getItemMeta();
        PersistentDataContainer dataContainer = cursorMeta.getPersistentDataContainer();
        if (dataContainer.has(new NamespacedKey(plugin, "CTSpecialAbility_Apply_Item"))) {
            String data = dataContainer.get(new NamespacedKey(plugin, "CTSpecialAbility_Apply_Item"), PersistentDataType.STRING);
            if (data != null) {
                Player player = (Player) e.getWhoClicked();
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    return;
                }
                e.setCancelled(true);
                switch (data) {
                    case "combat" -> {
                        Set<SpecialAbility> sets = specialAbilityManager.getSpecialAbilities(SpecialAbilityType.COMBAT);
                        List<SpecialAbility> allowAbilities = new ArrayList<>();
                        for (SpecialAbility specialAbility : sets) {
                            if (specialAbility.isAllowTool(itemStack)) {
                                allowAbilities.add(specialAbility);
                            }
                        }
                        int max = allowAbilities.size();
                        if (max == 0) {
                            player.sendMessage(Component.text(ChatColor.RED + "해당 아이템에는 적용할 수 있는 특수능력이 없습니다"));
                            return;
                        }
                        SpecialAbility specialAbility = allowAbilities.get(new Random().nextInt(max));
                        specialAbility.applySpecialAbilityToItemStack(itemStack);

                        cursor.setAmount(cursor.getAmount() - 1);
                        player.sendMessage(Component.text(ChatColor.GREEN + "해당아이템에 특수능력이 적용되었습니다."));
                    }
                    case "life" -> {
                        Set<SpecialAbility> sets = specialAbilityManager.getSpecialAbilities(SpecialAbilityType.LIFE);
                        List<SpecialAbility> allowAbilities = new ArrayList<>();
                        for (SpecialAbility specialAbility : sets) {
                            if (specialAbility.isAllowTool(itemStack)) {
                                allowAbilities.add(specialAbility);
                            }
                        }
                        int max = allowAbilities.size();
                        if (max == 0) {
                            player.sendMessage(Component.text(ChatColor.RED + "해당 아이템에는 적용할 수 있는 특수능력이 없습니다"));
                            return;
                        }
                        int r = MathUtil.getRangeRandomInt(max, 0);
                        SpecialAbility specialAbility = allowAbilities.get(new Random().nextInt(max));
                        specialAbility.applySpecialAbilityToItemStack(itemStack);

                        cursor.setAmount(cursor.getAmount() - 1);
                        player.sendMessage(Component.text(ChatColor.GREEN + "해당아이템에 특수능력이 적용되었습니다."));
                    }
                }
            }
        }
    }

}
