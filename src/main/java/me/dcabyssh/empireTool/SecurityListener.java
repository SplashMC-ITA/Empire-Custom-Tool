package me.dcabyssh.empireTool;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Iterator;

public class SecurityListener implements Listener {
    private final NamespacedKey key;

    public SecurityListener() {
        this.key = EmpireTool.getInstance().toolKey;
    }

    private boolean isCustom(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isCustom(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame || e.getRightClicked() instanceof ArmorStand) {
            ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
            ItemStack offHand = e.getPlayer().getInventory().getItemInOffHand();
            if (isCustom(mainHand) || isCustom(offHand)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        InventoryType type = e.getClickedInventory().getType();

        if (type == InventoryType.ANVIL || type == InventoryType.GRINDSTONE || type == InventoryType.SMITHING) {
            if (isCustom(e.getCurrentItem()) || isCustom(e.getCursor())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        for (ItemStack item : e.getInventory().getMatrix()) {
            if (isCustom(item)) {
                e.getInventory().setResult(null);
                break;
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Iterator<ItemStack> iter = e.getDrops().iterator();
        while (iter.hasNext()) {
            ItemStack drop = iter.next();
            if (isCustom(drop)) {
                e.getItemsToKeep().add(drop);
                iter.remove();
            }
        }
    }
}