package me.dcabyssh.empireTool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import java.util.EnumMap;
import java.util.Map;

public class PichaxeListener implements Listener {
    private final Map<Material, Material> smeltMap = new EnumMap<>(Material.class);

    public PichaxeListener() {
        smeltMap.put(Material.RAW_IRON, Material.IRON_INGOT); smeltMap.put(Material.IRON_ORE, Material.IRON_INGOT);
        smeltMap.put(Material.RAW_GOLD, Material.GOLD_INGOT); smeltMap.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        smeltMap.put(Material.RAW_COPPER, Material.COPPER_INGOT); smeltMap.put(Material.COPPER_ORE, Material.COPPER_INGOT);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!item.hasItemMeta()) return;

        String id = item.getItemMeta().getPersistentDataContainer().get(EmpireTool.getInstance().toolKey, PersistentDataType.STRING);
        if (id == null) return;

        Damageable dmg = (Damageable) item.getItemMeta();

        if (dmg.getDamage() >= (item.getType().getMaxDurability() * 0.98)) {
            e.setCancelled(true);
            String noEnergyMsg = EmpireTool.getInstance().getConfig().getString("Messages.energy-empty", "ENERGIA ESAURITA - RICARICA IN ACQUA");
            p.sendActionBar(Component.text(noEnergyMsg).color(NamedTextColor.RED));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            return;
        }

        if (id.equals("empire_pickaxe")) {
            e.setCancelled(true);
            breakArea(e.getBlock(), p, false);
            applyDamage(item);
        }
        else if (id.equals("empire_hoe")) {
            e.setCancelled(true);
            breakArea(e.getBlock(), p, true);
            applyDamage(item);
        }
        else {
            e.getBlock().getWorld().spawnParticle(Particle.SPLASH, e.getBlock().getLocation().add(0.5,0.5,0.5), 5);
        }
    }

    private void applyDamage(ItemStack item) {
        double chance = EmpireTool.getInstance().getConfig().getDouble("Settings.durability-loss-chance", 0.25);
        if (Math.random() <= chance) {
            Damageable meta = (Damageable) item.getItemMeta();
            meta.setDamage(meta.getDamage() + 1);
            item.setItemMeta(meta);
        }
    }

    private void breakArea(Block center, Player p, boolean hoe) {
        float pitch = p.getLocation().getPitch();
        float yaw = p.getLocation().getYaw();
        if (pitch > 45 || pitch < -45) {
            for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) process(center.getRelative(x, 0, z), p, hoe);
        } else {
            boolean axX = Math.abs(yaw) % 180 > 45 && Math.abs(yaw) % 180 < 135;
            for (int y = -1; y <= 1; y++) for (int d = -1; d <= 1; d++) process(axX ? center.getRelative(0, y, d) : center.getRelative(d, y, 0), p, hoe);
        }
    }

    private void process(Block b, Player p, boolean hoe) {
        if (b.getType().isAir() || b.getType() == Material.BEDROCK) return;
        if (hoe) {
            if (b.getBlockData() instanceof Ageable a && a.getAge() == a.getMaximumAge()) {
                b.getDrops(p.getInventory().getItemInMainHand()).forEach(d -> b.getWorld().dropItemNaturally(b.getLocation(), d));
                a.setAge(0); b.setBlockData(a);
            }
        } else {
            Material s = smeltMap.get(b.getType());
            if (s != null) { b.setType(Material.AIR); b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(s)); }
            else b.breakNaturally(p.getInventory().getItemInMainHand());
        }
        b.getWorld().spawnParticle(Particle.BUBBLE, b.getLocation().add(0.5,0.5,0.5), 2);
    }
}