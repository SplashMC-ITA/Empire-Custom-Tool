
package me.dcabyssh.empireTool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class EmpireTool extends JavaPlugin {
    private static EmpireTool instance;
    public NamespacedKey toolKey;
    public NamespacedKey guiKey;
    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.toolKey = new NamespacedKey(this, "item_id");
        this.guiKey = new NamespacedKey(this, "gui_type");

        ToolCommand tc = new ToolCommand();
        if (getCommand("tools") != null) getCommand("tools").setExecutor(tc);

        getServer().getPluginManager().registerEvents(new PichaxeListener(), this);
        getServer().getPluginManager().registerEvents(new SecurityListener(), this);
        getServer().getPluginManager().registerEvents(tc, this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                ItemStack item = p.getInventory().getItemInMainHand();
                if (isCustom(item)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 40, 1, false, false, false));
                    updateActionBar(p, item);
                }
                if (p.isInWater()) repairItems(p);
            }
        }, 0L, 20L);
    }

    private boolean isCustom(ItemStack i) {
        return i != null && i.hasItemMeta() && i.getItemMeta().getPersistentDataContainer().has(toolKey, PersistentDataType.STRING);
    }

    private void updateActionBar(Player p, ItemStack i) {
        Damageable meta = (Damageable) i.getItemMeta();
        double percent = (double) (i.getType().getMaxDurability() - meta.getDamage()) / i.getType().getMaxDurability() * 100;

        String barPrefix = "<gradient:#3D93FF:#2169C7><bold>PRESSIONE IDRICA: </bold></gradient>";
        NamedTextColor c = percent > 50 ? NamedTextColor.AQUA : (percent > 20 ? NamedTextColor.YELLOW : NamedTextColor.RED);

        p.sendActionBar(mm.deserialize(barPrefix)
                .append(Component.text((int)percent + "%").color(c).decoration(TextDecoration.BOLD, true)));
    }

    private void repairItems(Player p) {
        int repairAmount = getConfig().getInt("Settings.water-repair-amount", 20);

        for (ItemStack item : p.getInventory().getContents()) {
            if (isCustom(item)) {
                Damageable meta = (Damageable) item.getItemMeta();
                if (meta.getDamage() > 0) {
                    meta.setDamage(Math.max(0, meta.getDamage() - repairAmount));
                    item.setItemMeta(meta);
                }
            }
        }
    }

    public static EmpireTool getInstance() { return instance; }
}