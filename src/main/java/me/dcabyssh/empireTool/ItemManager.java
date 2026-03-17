package me.dcabyssh.empireTool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static final String BLUE_G = "<gradient:#3D93FF:#2F7EE3:#2169C7><bold>";
    private static final String AQUA_RARITY = "<gradient:#1310FF:#2426FE:#353DFD:#4653FC:#5769FB:#677FF9:#7896F8:#89ACF7:#9AC2F6><bold>ACQUATICO</bold></gradient>";
    private static final String SEP = "<color:#2169C7>» ══════════════════════ «</color>";

    private static ItemStack create(Material m, String nameHex, String id, String pwr, String pwrDesc, String l1, String l2, boolean armor) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(MM.deserialize(nameHex).decoration(TextDecoration.ITALIC, false));

        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
        if (armor) {
            meta.addEnchant(Enchantment.PROTECTION, 6, true);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            if (m == Material.NETHERITE_BOOTS) meta.addEnchant(Enchantment.FEATHER_FALLING, 4, true);
        } else {
            meta.addEnchant(Enchantment.EFFICIENCY, 7, true);
        }

        List<Component> lore = new ArrayList<>();
        lore.add(MM.deserialize(SEP));
        lore.add(MM.deserialize(BLUE_G + "➥ Rarità:</bold></gradient> " + AQUA_RARITY));
        lore.add(MM.deserialize(BLUE_G + "➥ </bold></gradient><white>" + l1));
        lore.add(Component.text("   " + l2).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false));

        lore.add(Component.empty());
        lore.add(MM.deserialize(BLUE_G + "➥ Potere:</bold></gradient> <white>" + pwr));
        lore.add(MM.deserialize(BLUE_G + "➥ Effetto:</bold></gradient> <white>" + pwrDesc));

        if (!armor) {
            lore.add(MM.deserialize(BLUE_G + "➥ Condizione:</bold></gradient> <white>Ricarica in <color:#3D93FF>Acqua</color>"));
        }

        lore.add(MM.deserialize(SEP));

        meta.lore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(EmpireTool.getInstance(), "item_id"), PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCustomPickaxe() { return create(Material.NETHERITE_PICKAXE, "<#1310FF><b>P</b><#1A19FF><b>I</b><#2123FE><b>C</b><#282CFE><b>C</b><#2F35FD><b>O</b><#373FFD><b>N</b><#3E48FC><b>E</b> <#4C5BFB><b>D</b><#5364FB><b>E</b><#5A6EFA><b>L</b><#6177FA><b>L</b><#6880F9><b>E</b> <#7693F8><b>M</b><#7E9DF8><b>A</b><#85A6F7><b>R</b><#8CAFF7><b>E</b><#93B9F6><b>E</b>", "empire_pickaxe", "Fornace", "Scavo 3x3 e Fusione", "Forgiato dalle pressioni", "degli abissi oscuri.", false); }
    public static ItemStack getCustomShovel() { return create(Material.NETHERITE_SHOVEL, "<#1310FF><b>P</b><#1C1CFE><b>A</b><#2528FE><b>L</b><#2E34FD><b>A</b> <#404BFC><b>D</b><#4957FB><b>E</b><#5263FB><b>L</b><#5B6FFA><b>L</b><#647BFA><b>E</b> <#7693F8><b>M</b><#7F9EF8><b>A</b><#88AAF7><b>R</b><#91B6F7><b>E</b><#9AC2F6><b>E</b>", "empire_shovel", "Erosione", "Scavo Rapido", "Scava le fosse oceaniche", "con la forza di un'onda.", false); }
    public static ItemStack getCustomAxe() { return create(Material.NETHERITE_AXE, "<#1310FF><b>A</b><#1B1BFE><b>S</b><#2426FE><b>C</b><#2C31FD><b>I</b><#353DFD><b>A</b> <#4653FC><b>D</b><#4E5EFB><b>E</b><#5769FB><b>L</b><#5F74FA><b>L</b><#677FF9><b>E</b> <#7896F8><b>M</b><#81A1F8><b>A</b><#89ACF7><b>R</b><#92B7F7><b>E</b><#9AC2F6><b>E</b>", "empire_axe", "Furia", "Taglio Istantaneo", "Spezza i vecchi tronchi", "come correnti impetuose.", false); }
    public static ItemStack getCustomHoe() { return create(Material.NETHERITE_HOE, "<#1310FF><b>Z</b><#1B1BFE><b>A</b><#2426FE><b>P</b><#2C31FD><b>P</b><#353DFD><b>A</b> <#4653FC><b>D</b><#4E5EFB><b>E</b><#5769FB><b>L</b><#5F74FA><b>L</b><#677FF9><b>E</b> <#7896F8><b>M</b><#81A1F8><b>A</b><#89ACF7><b>R</b><#92B7F7><b>E</b><#9AC2F6><b>E</b>", "empire_hoe", "Sorgente", "Raccolto 3x3", "Dona vita e crescita", "perpetua alle tue messi.", false); }
    public static ItemStack getCustomHelmet() { return create(Material.NETHERITE_HELMET, "<#1310FF><b>E</b><#1B1BFE><b>L</b><#2426FE><b>M</b><#2C31FD><b>O</b> <#4653FC><b>D</b><#4E5EFB><b>E</b><#5769FB><b>L</b><#5F74FA><b>L</b><#677FF9><b>E</b> <#7896F8><b>M</b><#81A1F8><b>A</b><#89ACF7><b>R</b><#92B7F7><b>E</b><#9AC2F6><b>E</b>", "empire_helmet", "Abissi", "Respiro e Visione", "Indossato dai re perduti", "per dominare l'oscurità.", true); }
    public static ItemStack getCustomChestplate() { return create(Material.NETHERITE_CHESTPLATE, "<#1310FF><b>C</b><#1B1BFE><b>O</b><#2426FE><b>R</b><#2C31FD><b>A</b><#353DFD><b>Z</b><#3D49FC><b>Z</b><#4653FC><b>A</b> <#4E5EFB><b>D</b><#5769FB><b>E</b><#5F74FA><b>L</b><#677FF9><b>L</b><#6F8AF9><b>E</b> <#7896F8><b>M</b><#81A1F8><b>A</b><#89ACF7><b>R</b><#92B7F7><b>E</b><#9AC2F6><b>E</b>", "empire_chestplate", "Pressione", "Resistenza Estrema", "Rende chi la indossa in", "battaglia inamovibile.", true); }
    public static ItemStack getCustomLeggings() { return create(Material.NETHERITE_LEGGINGS, "<#1310FF><b>G</b><#1B1BFE><b>A</b><#2426FE><b>M</b><#2C31FD><b>B</b><#353DFD><b>A</b><#3D49FC><b>L</b><#4653FC><b>I</b> <#4E5EFB><b>D</b><#5769FB><b>E</b><#5F74FA><b>L</b><#677FF9><b>L</b><#6F8AF9><b>E</b> <#7896F8><b>M</b><#81A1F8><b>A</b><#89ACF7><b>R</b><#92B7F7><b>E</b><#9AC2F6><b>E</b>", "empire_leggings", "Correnti", "Agilità Marina", "Squame resistenti che", "deviano ogni lama nemica.", true); }
    public static ItemStack getCustomBoots() { return create(Material.NETHERITE_BOOTS, "<#1310FF><b>P</b><#1B1BFE><b>I</b><#2426FE><b>E</b><#2C31FD><b>D</b><#353DFD><b>I</b><#3D49FC><b>N</b><#4653FC><b>I</b> <#4E5EFB><b>D</b><#5769FB><b>E</b><#5F74FA><b>L</b><#677FF9><b>L</b><#6F8AF9><b>E</b> <#7896F8><b>M</b><#81A1F8><b>A</b><#89ACF7><b>R</b><#92B7F7><b>E</b><#9AC2F6><b>E</b>", "empire_boots", "Flusso", "Caduta Attutita", "Cammina leggero sopra", "le maree più tumultuose.", true); }
}