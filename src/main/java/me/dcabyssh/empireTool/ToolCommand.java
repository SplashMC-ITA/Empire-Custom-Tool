package me.dcabyssh.empireTool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ToolCommand implements CommandExecutor, Listener {

    private final String title = "<gradient:#1310FF:#93B9F6><bold>SPLASHMC CUSTOM TOOLS</bold></gradient>";

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if (!(s instanceof Player p)) return true;
        Inventory gui = Bukkit.createInventory(null, 54, MiniMessage.miniMessage().deserialize(title));

        // Marcatore GUI per sicurezza totale
        ItemMeta gm = Bukkit.getItemFactory().getItemMeta(Material.CYAN_STAINED_GLASS_PANE);
        ItemStack filler = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        gm.displayName(Component.empty());
        gm.getPersistentDataContainer().set(EmpireTool.getInstance().guiKey, PersistentDataType.STRING, "main_gui");
        filler.setItemMeta(gm);

        for (int i = 0; i < 54; i++) gui.setItem(i, filler);

        gui.setItem(19, ItemManager.getCustomPickaxe());
        gui.setItem(21, ItemManager.getCustomShovel());
        gui.setItem(23, ItemManager.getCustomAxe());
        gui.setItem(25, ItemManager.getCustomHoe());
        gui.setItem(37, ItemManager.getCustomHelmet());
        gui.setItem(39, ItemManager.getCustomChestplate());
        gui.setItem(41, ItemManager.getCustomLeggings());
        gui.setItem(43, ItemManager.getCustomBoots());

        p.openInventory(gui);
        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;

        // Controllo se è la nostra GUI tramite il filler (metodo infallibile)
        ItemStack filler = e.getInventory().getItem(0);
        if (filler == null || !filler.hasItemMeta() || !filler.getItemMeta().getPersistentDataContainer().has(EmpireTool.getInstance().guiKey, PersistentDataType.STRING)) return;

        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player p)) return;

        if (e.getRawSlot() < e.getView().getTopInventory().getSize()) {
            ItemStack item = e.getCurrentItem();
            if (item != null && item.getType() != Material.CYAN_STAINED_GLASS_PANE && item.getType() != Material.AIR) {
                p.getInventory().addItem(item.clone());
                p.sendMessage(Component.text("✅ Oggetto equipaggiato!").color(NamedTextColor.GREEN));
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        ItemStack filler = e.getInventory().getItem(0);
        if (filler != null && filler.hasItemMeta() && filler.getItemMeta().getPersistentDataContainer().has(EmpireTool.getInstance().guiKey, PersistentDataType.STRING)) {
            e.setCancelled(true);
        }
    }
}